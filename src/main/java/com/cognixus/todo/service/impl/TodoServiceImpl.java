package com.cognixus.todo.service.impl;

import com.cognixus.todo.exception.BadRequestException;
import com.cognixus.todo.model.Todo;
import com.cognixus.todo.repository.TodoRepo;
import com.cognixus.todo.service.api.TodoService;
import com.cognixus.todo.snowflake.SnowflakeHelper;
import com.cognixus.todo.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Todo service for saving, listing and delete
 */
@Service
@Transactional
@Slf4j
public class TodoServiceImpl implements TodoService {

    @Autowired
    TodoRepo todoRepo;

    @Override
    public List<Todo> saveAll(List<Todo> todos) {
        //assign id
        SnowflakeHelper.assignLongIds(todos);
        //assign createdId
        AuthUtil.assignCreatedBy(todos);
        //validate
        validateData(todos);
        //save and return
        return todoRepo.saveAll(todos);
    }

    private void validateData(List<Todo> todos) {
        if (todos.stream().anyMatch(x -> x.getName() == null || x.getName().isBlank())) {
            throw new BadRequestException("Name cannot be blank");
        }
        if (todos.stream().anyMatch(x -> x.getDescription() == null || x.getDescription().isBlank())) {
            throw new BadRequestException("Description cannot be blank");
        }
    }

    @Override
    public List<Todo> findByUser(Boolean completed) {
        var userId = AuthUtil.getLoginId();
        return completed != null ? todoRepo.findByCreatedByAndCompleted(userId, completed) : todoRepo.findByCreatedBy(userId);
    }

    @Override
    @Transactional
    public void deleteAllByIdIn(List<Long> ids) {
        //validate delete
        validateOwnData(ids);
        //delete
        todoRepo.deleteAllByIdIn(ids);
    }

    @Override
    public List<Todo> findAllByIdIn(List<Long> ids) {
        return todoRepo.findAllByIdIn(ids);
    }

    @Override
    @Transactional()
    public void updateCompletedByIdIn(List<Long> ids) {
        //validate
        validateOwnData(ids);
        //update complete
        todoRepo.updateCompletedByIdIn(ids);

    }


    private void validateOwnData(List<Long> ids) {
        var userId = AuthUtil.getLoginId();
        var list = this.findAllByIdIn(ids);
        if (list.stream().anyMatch(s -> !s.getCreatedBy().equals(userId))) {
            throw new BadRequestException("Access denied");
        }
    }
}
