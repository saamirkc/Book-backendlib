package com.samir.book_backend.book;

import com.samir.book_backend.history.BookTransactionalHistory;
import lombok.Builder;
import org.springframework.stereotype.Service;
@Builder
@Service
public class BookMapper {
    public Book toBook(BookRequest request) {
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .authorName(request.authorName())
                .isbn(request.isbn())
                .synopsis(request.synopsis())
                .archived(false)
                .shareable(request.shareable())
                .build();
}

    public  BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .rate(book.getRate())
                .archived(book.isArchived())
                .owner(book.getOwner().getFullname())
//               FOR  COVER , WE IMPLEMENT LATER
                .shareable(book.isShareable())


                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionalHistory bookTransactionalHistory) {
        return BorrowedBookResponse.builder()
                .id(bookTransactionalHistory.getId())
                .title(bookTransactionalHistory.getBook().getTitle())
                .authorName(bookTransactionalHistory.getBook().getAuthorName())
                .isbn(bookTransactionalHistory.getBook().getIsbn())
                .rate(bookTransactionalHistory.getBook().getRate())
                .returned(bookTransactionalHistory.isReturnedBook())
                .returnApproved(bookTransactionalHistory.isReturnedApproved())

                .build();
    }

}
