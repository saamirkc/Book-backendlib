package com.samir.book_backend.feedback;

import com.samir.book_backend.common.PageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;



//    saving feedback
    @PostMapping
    public ResponseEntity<Integer> saveFeedback(
            @RequestBody FeedbackRequest request,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(feedbackService.saveFeedback(request,connectedUser));
    }

//    finding   feedback by book.

    @GetMapping("/book/{bookId}")
    public ResponseEntity<PageResponse<FeedbackResponse>> findFeedbackByBook(
            @PathVariable Integer bookId,
            @RequestParam(name="page",defaultValue = "0",required = false) int page,
            @RequestParam(name="size",defaultValue = "10",required = false)int size,
            Authentication connectedUser
    )
    {
        return ResponseEntity.ok(feedbackService.findFeedbacKByBook(bookId,page,size,connectedUser));
    }


}
