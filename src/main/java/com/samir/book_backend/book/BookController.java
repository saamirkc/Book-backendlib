package com.samir.book_backend.book;

import com.samir.book_backend.common.PageResponse;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    @PostMapping
    public ResponseEntity<Integer>saveBook(
            @Valid @RequestBody BookRequest request, Authentication connectedUser
            ){
return ResponseEntity.ok(bookService.save(request,connectedUser));
    }

//    get book by bookid
    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponse> findBookById(@PathVariable Integer bookId ){
        return ResponseEntity.ok(bookService.findById(bookId));
    }


//    get all books . since getting all books will be time-consuming if there are large number of groups . so we have to use pagination.

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
            @RequestParam(name="page",defaultValue = "0",required = false) int page,   //@RequestParam is used for query parameters.that means if we want to retrieve specific data as in pagination we want specific data.
            @RequestParam(name="size",defaultValue = "10",required = false)int size,
            Authentication connectedUser
    ){
return ResponseEntity.ok(bookService.findAllBooks(page,size,connectedUser));
    }

//    getting books by owner

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name="page",defaultValue = "0",required = false) int page,
            @RequestParam(name="size",defaultValue = "10",required = false)int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.findAllBooksByOwner(page,size,connectedUser));
    }


//    getting borrowed books.
    @GetMapping("/borrow")
    public  ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(
            @RequestParam(name = "page",defaultValue = "0" ,required = false) int page,
            @RequestParam(name = "size",defaultValue = "10",required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.findAllBorrowedBooks(page,size,connectedUser));
    }

//    getting returned books.
@GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(
            @RequestParam(name = "page",defaultValue = "0" ,required = false) int page,
            @RequestParam(name = "size",defaultValue = "10",required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.findAllReturnedBooks(page,size,connectedUser));
    }

//    update shareable status.
@PatchMapping("/shareable/{bookId]")     //PatchMapping is used when you want to update specific fields or portions of a resource rather than replacing
                                            // entire resource as you do with Put mapping.
    public ResponseEntity<Integer>  updateShareableStatus(
            @PathVariable Integer bookId ,Authentication connectedUser
){
        return ResponseEntity.ok( bookService.updateShareableStatus(bookId,connectedUser));

    }

//    update archived status .
@PatchMapping("/archived/{bookId]")     //PatchMapping is used when you want to update specific fields or portions of a resource rather than replacing
// entire resource as you do with Put mapping.
public ResponseEntity<Integer>  updateArchivedStatus(
        @PathVariable Integer bookId ,Authentication connectedUser
){
    return ResponseEntity.ok( bookService.updateArchivedStatus(bookId,connectedUser));

}

// implementing borrowed book.

    @PostMapping("/borrow/{bookId}")
    public ResponseEntity<Integer> borrowBook(
            @PathVariable Integer bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.borrowBook(bookId,connectedUser));
    }

//return borrowed book

    @PatchMapping("/borrow/return/{bookId}")
    public ResponseEntity<Integer> returnBorrowBook(
            @PathVariable Integer bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.returnBorrowedBook(bookId,connectedUser));
    }

    @PatchMapping("/borrow/return/approve/{bookId}")
    public ResponseEntity<Integer> approveReturnBorrowBook(
            @PathVariable Integer bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.approveReturnBorrowedBook(bookId,connectedUser));
    }

//    photo adding book controller

    @PostMapping(value = "/cover/{bookId}",consumes = "multipart/form-data")
    public ResponseEntity<?> uploadBookCoverPicture(
            @PathVariable Integer bookId,
            @Parameter()
            @RequestPart("file") MultipartFile file,
            Authentication connectedUser


    ){
      bookService.uploadBookCoverPicture(file,connectedUser,bookId);
        return ResponseEntity.accepted().build();

    }



}

