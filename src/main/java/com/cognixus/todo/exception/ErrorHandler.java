package com.cognixus.todo.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ErrorHandler {
    String error;

    //todo: implement error code in future
}
