//package com.samir.book_backend.book;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.samir.book_backend.feedback.Feedback;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.CreatedBy;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedBy;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Entity
//@Table(name = "book")
//@EntityListeners(AuditingEntityListener.class)
//public class Book {
//    @Id
//    @GeneratedValue
//    private Integer id;
//    private String title;
//    private String authorName;
//    private String isbn;
//    private String synopsis;
//    private String bookCover;
//    private boolean archived;
//    private boolean shareable;
//    @CreatedDate
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime creationDate;
//    @LastModifiedDate
//    @Column(insertable  = false)
//    private LocalDateTime lastModifiedDate;
//    @CreatedBy
//    @Column(nullable = false, updatable = false)
//    private Integer createdBy;
//    @LastModifiedBy
//    @Column(insertable = false)
//    private Integer lastModifiedBy;
//
//
//
//    @OneToMany(mappedBy = "book")
//    @JsonIgnore
//    private List<Feedback> feedbacks = new ArrayList<>();
//
//}

//since  ,
//    @EntityListeners(AuditingEntityListener.class)
//    @Id
//    @GeneratedValue
//    private Integer id;
//  @CreatedDate
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime creationDate;
//    @LastModifiedDate
//    @Column(insertable  = false)
//    private LocalDateTime lastModifiedDate;
//    @CreatedBy
//    @Column(nullable = false, updatable = false)
//    private Integer createdBy;
//    @LastModifiedBy
//    @Column(insertable = false)
//    private Integer lastModifiedBy;

//these are repeated in both Book class and Feedback class , so in order to avoid repetition we build base class and extends that base class
//int these two book and feedback class.




package com.samir.book_backend.book;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samir.book_backend.common.Base;
import com.samir.book_backend.feedback.Feedback;
import com.samir.book_backend.history.BookTransactionalHistory;
import com.samir.book_backend.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity


public class Book extends Base {


    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;








//relationship of book entity with feedback entity
    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<Feedback> feedbacks = new ArrayList<>();

//    relationship of book entity with book transactional history

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private List<BookTransactionalHistory> bookTransactionalHistories = new ArrayList<>();




    @ManyToOne
    private User owner;



    @Transient       //transient annotation makes sure that the below filed of transient wont be saved in database.
    public  double getRate(){
        if(feedbacks==null || feedbacks.isEmpty()){
            return 0.0;
        }
        var rate=this.feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);

        double roundedRate =Math.round(rate*10.0)/10.0;
        return roundedRate;
    }


}