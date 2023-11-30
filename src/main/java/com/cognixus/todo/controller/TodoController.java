package com.cognixus.todo.controller;

import com.cognixus.todo.model.Todo;
import com.cognixus.todo.service.api.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping()
    @Operation(summary = "Insert todo list data")
    public List<Todo> saveAll(@RequestBody List<Todo> todos) {
        return todoService.saveAll(todos);
    }

    @GetMapping()
    @Operation(summary = "Get own todo list either all, pending or completed")
    public List<Todo> findByUser(@RequestParam(required = false, defaultValue = "") Boolean completed) {
        return todoService.findByUser(completed);
    }

    @DeleteMapping()
    @Operation(summary = "Delete own to do data")
    public void deleteAllByIdIn(@RequestBody List<Long> ids) {
        todoService.deleteAllByIdIn(ids);
    }

    @PostMapping("/completed")
    @Operation(summary = "update to do item as completed")
    public List<Todo> updateCompletedByIdIn(@RequestBody List<Long> ids) {
        todoService.updateCompletedByIdIn(ids);
        //need to let update completed committed then retrieve results
        return todoService.findAllByIdIn(ids);
    }

}
