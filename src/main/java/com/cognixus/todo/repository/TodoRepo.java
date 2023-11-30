package com.cognixus.todo.repository;

import com.cognixus.todo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepo extends JpaRepository<Todo, Long> {
    /**
     * find todo by user id and complete status
     */
    List<Todo> findByCreatedByAndCompleted(Long createdBy, Boolean completed);

    /**
     * find todo by user id
     */
    List<Todo> findByCreatedBy(Long createdBy);

    /**
     * delete by ids
     *
     * @param ids id list
     */
    void deleteAllByIdIn(List<Long> ids);

    /**
     * find by ids
     *
     * @param ids id list
     * @return results
     */
    List<Todo> findAllByIdIn(List<Long> ids);

    @Modifying
    @Query("UPDATE T_TODO t SET t.completed = true WHERE t.id IN (?1)")
    void updateCompletedByIdIn(List<Long> ids);
}
