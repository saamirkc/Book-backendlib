package com.samir.book_backend.book;

import com.samir.book_backend.common.PageResponse;
import com.samir.book_backend.exception.OperationNotPermittedException;
import com.samir.book_backend.file.FileStorageService;
import com.samir.book_backend.history.BookTransactionalHistory;
import com.samir.book_backend.history.BookTransactionalHistoryRepository;
import com.samir.book_backend.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionalHistoryRepository bookTransactionalHistoryRepository;
    private final FileStorageService fileStorageService;
    public  Integer save(BookRequest request , Authentication connectedUser){
        User user = (User) connectedUser.getPrincipal();
        Book book=bookMapper.toBook(request);
        book.setOwner(user);
        return bookRepository.save(book).getId();

    }
    public BookResponse findById(Integer bookId){
      return bookRepository.findById(bookId)
              .map(bookMapper::toBookResponse)
              .orElseThrow(()->new EntityNotFoundException("No book found with id : "+bookId));

    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<Book> books=bookRepository.findAllDisplayableBooks(pageable,user.getId());
        List<BookResponse>  bookResponse= books
                .stream()
                .map(bookMapper::toBookResponse)
                .toList();

      return new PageResponse<>(
              bookResponse,
              books.getNumber(),
              books.getSize(),
              books.getTotalElements(),
              books.getTotalPages(),
              books.isFirst(),
              books.isLast()

      );


    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<Book> books=bookRepository.findAll(BookSpecification.withOwnerId(user.getId()),pageable);
        List<BookResponse>  bookResponse= books
                .stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()

        );


    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<BookTransactionalHistory> allBorrowedBooks =bookTransactionalHistoryRepository.findBorrowedBook(user.getId(), pageable);
        List<BorrowedBookResponse> borrowedBookResponses=allBorrowedBooks
                .stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                borrowedBookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()

        );


    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable=PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<BookTransactionalHistory> allReturnedBooks=bookTransactionalHistoryRepository.findReturnedBook(user.getId(),pageable);
        List<BorrowedBookResponse> borrowedBookResponses=allReturnedBooks
                .stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                borrowedBookResponses,
                allReturnedBooks.getNumber(),
                allReturnedBooks.getSize(),
                allReturnedBooks.getTotalElements(),
                allReturnedBooks.getTotalPages(),
                allReturnedBooks.isFirst(),
                allReturnedBooks.isLast()

        );

    }

    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        User user =(User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()->new EntityNotFoundException("No book found with id : "+bookId));

        if(!Objects.equals(book.getOwner().getId(),user.getId())){
            throw new OperationNotPermittedException("You cannot update others' books shareable status");
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return book.getId();

    }


    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
        User user =(User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()->new EntityNotFoundException("No book found with id : "+bookId));

        if(!Objects.equals(book.getOwner().getId(),user.getId())){
            throw new OperationNotPermittedException("You cannot update others' books archived status");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return book.getId();
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book book =bookRepository.findById(bookId)
                .orElseThrow(()->new EntityNotFoundException("No book found with id : "+bookId));

        if(!book.isShareable() || book.isArchived()){
            throw new OperationNotPermittedException("The requested book  can not be borrowed since it is either not shareable or is archived.");
        }
        User user =(User) connectedUser.getPrincipal();
        if(Objects.equals(book.getOwner().getId(),user.getId())){
            throw  new OperationNotPermittedException("You can not borrow your own book");
        }
//        check whether it is borrowed already ?
        final boolean isAlreadyBorrowed=bookTransactionalHistoryRepository.isAlreadyBorrowedByUser(bookId,user.getId());    //here bookId and userId is passed as parameter because bookId uniquely identifies the book you are checking and user.getId uniquely identifies the user
                                                                                                                           //The use of user.getId() here is likely due to the fact that the bookTransactionalHistory entity doesn’t have a direct field for User, but instead uses
                                                                                                                          // a relationship mapping (such as a foreign key) to link back to the User entity. And  there won’t be any error due to the difference in the variable name (id in BookTransactionHistory vs. bookId in the method parameter).
        if(isAlreadyBorrowed){
            throw new OperationNotPermittedException("The requested book is already borrowed");
        }
        BookTransactionalHistory bookTransactionalHistory=BookTransactionalHistory.builder()
                .user(user)
                .book(book)
                .returnedBook(false)
                .returnedApproved(false)
                .build();

//        without using builder class.
//        BookTransactionalHistory bookTransactionalHistory = new BookTransactionalHistory();
//        bookTransactionalHistory.setUser(user);
//        bookTransactionalHistory.setBook(book);
//        bookTransactionalHistory.setReturnedBook(false);
//        bookTransactionalHistory.setReturnedApproved(false);

return bookTransactionalHistoryRepository.save(bookTransactionalHistory).getId();
    }


    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
//        getting book
        Book book=bookRepository.findById(bookId)
                .orElseThrow(()->new EntityNotFoundException("No book found with id : "+bookId));
//checking whether book is shareable or not archived.
        if(!book.isShareable() || book.isArchived()){
            throw new OperationNotPermittedException("The requested book cannot be borrowed or returned since it is either not shareable or is archived.");
        }
//        getting user
        User user =(User) connectedUser.getPrincipal();

        if(Objects.equals(book.getOwner().getId(),user.getId())){
            throw new OperationNotPermittedException("You cannot borrow /return your own book ");
        }

//        finding whether user has borrow book or not.
        BookTransactionalHistory bookTransactionalHistory = bookTransactionalHistoryRepository.findByBookIdAndUserId(bookId,user.getId())
                .orElseThrow(()->new OperationNotPermittedException("You did not borrow this book "));

bookTransactionalHistory.setReturnedBook(!bookTransactionalHistory.isReturnedBook());
return bookTransactionalHistoryRepository.save(bookTransactionalHistory).getId();


    }

    public Integer approveReturnBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book book=bookRepository.findById(bookId)
                .orElseThrow(()->new EntityNotFoundException("No book found with id : "+bookId));

        if(!book.isShareable() || book.isArchived()){
            throw new OperationNotPermittedException("The requested book cannot be borrowed or returned since it is either not shareable or is archived.");
        }

        User user =(User) connectedUser.getPrincipal();

        if(Objects.equals(book.getOwner().getId(),user.getId())){
            throw new OperationNotPermittedException("You cannot borrow /return your own book ");
        }
        BookTransactionalHistory bookTransactionalHistory = (BookTransactionalHistory) bookTransactionalHistoryRepository.findByBookIdAndOwnerId(bookId,user.getId())
                .orElseThrow(()->new OperationNotPermittedException("The book is not returned yet. You cannot approve this return"));
//bookTransactionalHistory.setReturnedApproved(!bookTransactionalHistory.isReturnedApproved());
        bookTransactionalHistory.setReturnedBook(true);  // both 239 and 240 same
        return bookTransactionalHistoryRepository.save(bookTransactionalHistory).getId();
    }

//uploading new cover photos of book
    public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, Integer bookId) {
        Book book=bookRepository.findById(bookId)
                .orElseThrow(()->new EntityNotFoundException("No book found with id : "+bookId));
        User user =(User) connectedUser.getPrincipal();

        var bookCover=fileStorageService.saveFile(file,user.getId());
        book.setBookCover(bookCover);
        bookRepository.save(book);
    }


}
