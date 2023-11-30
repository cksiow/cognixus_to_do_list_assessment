package com.cognixus.todo.dto;

import com.cognixus.todo.enums.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.function.Function;

/**
 * Mini version of unit test request dto
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UnitTestRequest {

    String requestFile;
    String responseFile;
    String url;
    String token;
    ResultMatcher statusResult;
    HttpMethod method;

    CustomComparator customComparator;

    private Function<String, String> requestContextInvoker;
}
