package com.samir.book_backend.feedback;


import com.samir.book_backend.book.Book;
import com.samir.book_backend.book.BookRepository;
import com.samir.book_backend.book.BookResponse;
import com.samir.book_backend.common.PageResponse;
import com.samir.book_backend.exception.OperationNotPermittedException;
import com.samir.book_backend.user.User;
import com.samir.book_backend.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

private final BookRepository bookRepository;
private final FeedbackMapper feedbackMapper;
private final FeedbackRepository feedbackRepository;


    public Integer saveFeedback(FeedbackRequest request, Authentication connectedUser) {

        Book book=bookRepository.findById(request.bookId())
                .orElseThrow(()-> new EntityNotFoundException("No book found with id "+request.bookId()));

        User user=(User) connectedUser.getPrincipal();

        if(!book.isShareable()||book.isArchived()){
            throw new OperationNotPermittedException("You cannot add your feedback as the book  with" + request.bookId() + "is either not shareable or archived ");
        }

        if(Objects.equals(book.getOwner().getId(),user.getId())){
            throw new OperationNotPermittedException("You cannot add feedback in your own book");
        }

       Feedback feedback=feedbackMapper.tofeedback(request);
        return feedbackRepository.save(feedback).getId();

    }


    public PageResponse<FeedbackResponse> findFeedbacKByBook(Integer bookId, int page, int size, Authentication connectedUser) {
        User user=(User) connectedUser.getPrincipal();
        Pageable pageable=PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId,pageable);
        List<FeedbackResponse> feedbackResponse=feedbacks
                .stream()
//                .map(feedbackMapper::toFeedbackResponse)
                .map(f->feedbackMapper.toFeedbackResponse(f,user.getId()))
                .toList();

        return  new PageResponse<>(
                feedbackResponse,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );


    }
}
