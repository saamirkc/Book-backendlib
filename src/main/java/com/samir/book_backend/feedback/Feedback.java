//package com.samir.book_backend.feedback;
//import com.samir.book_backend.book.Book;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import java.time.LocalDateTime;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Entity
//@Table(name = "feedback")
//@EntityListeners(AuditingEntityListener.class)
//public class Feedback {
//    @Id
//    @GeneratedValue
//    private Integer id;
//    private Double note;   //for suppose 1 to 5 star
//    private String comment;
//
//    @CreatedDate
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//    @LastModifiedDate
//    @Column(insertable = false)
//    private LocalDateTime  lastModifiedAt;
//
//    @ManyToOne
//    private Book book;
//}


//above one is also correct but due to code repetition below one is optimized where code is not repeated..

package com.samir.book_backend.feedback;

import com.samir.book_backend.book.Book;
import com.samir.book_backend.common.Base;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "feedback")
@EntityListeners(AuditingEntityListener.class)
public class Feedback extends Base {

    private Double note;   //for suppose 1 to 5 star
    private String comment;



    @ManyToOne
    private Book book;
}

