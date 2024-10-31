package com.samir.book_backend.history;

import com.samir.book_backend.book.Book;
import com.samir.book_backend.common.Base;
import com.samir.book_backend.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
//@Table(name = "book_transaction_history")
public class BookTransactionalHistory extends Base {

    private boolean returnedBook;
    private boolean returnedApproved;

//    relationship of book tranactional history with book

    @ManyToOne
    private Book book;


//    relationship of book tranactional history with user
    @ManyToOne
    private User user;

}
