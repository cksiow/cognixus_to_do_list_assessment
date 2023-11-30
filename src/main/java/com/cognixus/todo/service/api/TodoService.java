package com.cognixus.todo.service.api;

import com.cognixus.todo.model.Todo;

import java.util.List;

public interface TodoService {
    /**
     * Save all todo list
     *
     * @param todos input
     * @return saved data
     */
    List<Todo> saveAll(List<Todo> todos);

    /**
     * Find all to do data based on login user, either pending or completed or full (Null) list
     *
     * @param completed pending or completed
     */
    List<Todo> findByUser(Boolean completed);

    /**
     * Delete todo by ids
     *
     * @param ids delete id list
     */
    void deleteAllByIdIn(List<Long> ids);

    List<Todo> findAllByIdIn(List<Long> ids);

    void updateCompletedByIdIn(List<Long> ids);
}
