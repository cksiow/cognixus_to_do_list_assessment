package com.cognixus.todo.controller;

import com.cognixus.todo.model.Todo;
import com.cognixus.todo.service.api.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping()
    public List<Todo> saveAll(@RequestBody List<Todo> users) {
        return todoService.saveAll(users);
    }

    @GetMapping()
    public List<Todo> findByUser(@RequestParam(required = false, defaultValue = "") Boolean completed) {
        return todoService.findByUser(completed);
    }

    @DeleteMapping()
    public void deleteAllByIdIn(@RequestBody List<Long> ids) {
        todoService.deleteAllByIdIn(ids);
    }

    @PostMapping("/completed")
    public List<Todo> updateCompletedByIdIn(@RequestBody List<Long> ids) {
        todoService.updateCompletedByIdIn(ids);
        //need to let update completed committed then retrieve results
        return todoService.findAllByIdIn(ids);
    }

}
