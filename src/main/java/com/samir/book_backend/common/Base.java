package com.samir.book_backend.common;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder         //this @SuperBuilder annotation is used in case of normal builder annotation in base class and child class . normal @Builder annotations wont work in case of inheritance
@MappedSuperclass     //this annotations mapped the field in base class to the child class(Class that extends the base class) .
                      //Without @MappedSuperclass, the fields in the base class will essentially act as regular Java fields, meaning they won’t be mapped to the database, won’t inherit JPA annotations, and won’t be managed by JPA.
@EntityListeners(AuditingEntityListener.class)
public class Base {


    @Id
    @GeneratedValue
    private Integer id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable  = false)
    private LocalDateTime lastModifiedDate;
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Integer createdBy;
    @LastModifiedBy
    @Column(insertable = false)
    private Integer lastModifiedBy;




}


//@Created Date and @LastModified Date works fine as we use  @EntityListeners(AuditingEntityListener.class) and @EnableJpaAuditing
//in BookBackend Application(main application).
//for @CreateBy and @LastModifiedBY to work properly , only @EnableJpaAuditing does not work ,for that we need to add
//application audit aware.So we will add ApplicationAuditAware in config class.
