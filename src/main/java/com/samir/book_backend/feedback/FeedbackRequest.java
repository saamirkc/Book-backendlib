package com.samir.book_backend.feedback;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.*;

public record FeedbackRequest(
@Positive(message = "200")
        @Min(value = 0,message = "201")
        @Max(value = 5,message = "202")
        Double note,

        @NotNull(message = "203")
        @NotEmpty(message = "203")
        @NotBlank(message = "203")
        String comment,

        @NotNull(message = "204")
        Integer bookId


        ) {
}

//FeedbackRequest is used to capture incoming data from the client (like note, comment, and book ID)
// and ensures that it adheres to validation rules defined through annotations.
// The request is typically transient, representing a single transaction (creating feedback), making records a good fit.
//you can also make Feedback Request which is record into class as in FeedbackResponse.