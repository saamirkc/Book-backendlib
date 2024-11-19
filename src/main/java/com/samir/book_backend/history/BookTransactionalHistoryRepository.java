package com.samir.book_backend.history;

import com.samir.book_backend.book.BorrowedBookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookTransactionalHistoryRepository extends JpaRepository<BookTransactionalHistory,Integer> {
@Query("""
SELECT borrowedBook
FROM BookTransactionalHistory borrowedBook
WHERE borrowedBook.user.id=:userId
""")

    Page<BookTransactionalHistory> findBorrowedBook(Integer userId, Pageable pageable);
@Query("""
SELECT returnedBook
FROM BookTransactionalHistory returnedBook
WHERE returnedBook.book.owner.id=:userId
""")
    Page<BookTransactionalHistory> findReturnedBook(Integer userId, Pageable pageable);



@Query("""
SELECT 
(COUNT(*) > 0) AS isborrowed
FROM BookTransactionalHistory booktransactionalhistory
WHERE booktransactionalhistory.user.id=:userId
AND booktransactionalhistory.book.id=:bookId
AND booktransactionalhistory.returnedApproved=false 

"""
)
    boolean isAlreadyBorrowedByUser(Integer bookId, Integer userId);

@Query("""
SELECT history
FROM BookTransactionalHistory history
WHERE history.user.id=:userId
AND history.book.id=:bookId
AND history.returnedBook=false
AND history.returnedApproved=false

""")
Optional<  BookTransactionalHistory > findByBookIdAndUserId(Integer bookId, Integer userId);

@Query("""
SELECT history
FROM BookTransactionalHistory history
WHERE history.book.owner.id=:userId
AND history.book.id=:bookId
AND history.returnedBook=true
AND history.returnedApproved=false
""")
    Optional<Object> findByBookIdAndOwnerId(Integer bookId, Integer userId);
}
