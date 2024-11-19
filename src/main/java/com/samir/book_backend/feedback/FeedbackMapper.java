package com.samir.book_backend.feedback;

import com.samir.book_backend.book.Book;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service

public class FeedbackMapper {



    public Feedback tofeedback(FeedbackRequest request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .book(Book.builder()
                        .id(request.bookId())              //making relationship with book entity (feedbak and book entity mapped)
                        .archived(false)
                        .shareable(false)
                        .build())


                .build();


    }

    public FeedbackResponse toFeedbackResponse(Feedback f, Integer id) {
        return FeedbackResponse.builder()
                .note(f.getNote())
                .comment(f.getComment())
                .ownFeedback(Objects.equals(f.getCreatedBy(),id))
                .build();
    }

//    public FeedbackResponse toFeedbackResponse(Feedback feedback) {
//        return FeedbackResponse.builder()
//                .note(feedback.getNote())
//                .comment(feedback.getComment())
//
//                .build();
//
//    }


}


//tofeedback Method: This method is used to create and save a new Feedback entity in the database.
// When you create feedback, the system needs a complete entity to store all relevant fields,
// ensuring all required data is captured accurately. However, this Feedback entity is only used
// internally—it’s never exposed directly to the client as a response.

//toFeedbackResponse Method: This method is used to retrieve feedback and map it to a FeedbackResponse object
// , which contains only selected fields needed for the client. This prevents exposure of the entire Feedback class,
// limiting the client to viewing only the safe, relevant fields (like note, comment, and ownFeedback).

//When retrieving data, you may not want to expose the entire Feedback entity as it is stored in the database.
// The FeedbackResponse object is designed to provide only relevant, pre-processed information to clients.
