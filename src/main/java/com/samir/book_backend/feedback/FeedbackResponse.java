package com.samir.book_backend.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackResponse {

    private Integer bookId;
    private Double note;
    private String comment;
//    private String feedback;
    private boolean ownFeedback;

}
