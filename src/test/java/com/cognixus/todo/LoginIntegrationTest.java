package com.cognixus.todo;

import com.cognixus.todo.base.TestBase;
import com.cognixus.todo.comparator.RegexNullableValueMatcher;
import com.cognixus.todo.dto.UnitTestRequest;
import com.cognixus.todo.enums.HttpMethod;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginIntegrationTest extends TestBase {
    

    @Test
    @Order(10)
    public void When_withWrongUser_Then_returnUserNotExists() {
        //not user exists
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.POST)
                .url("/login?username=ck44")
                .responseFile("data/user/negative/notExistsResponse.json")
                .statusResult(status().isUnauthorized())
                .build());
    }


    public CustomComparator getTokenComparator() {
        // as long as id is numeric = fine, regardless under array or node
        return new CustomComparator(JSONCompareMode.STRICT
                , new Customization("access_token", new RegexNullableValueMatcher("[\\w-]*[.][\\w-]*[.][\\w-]*", false)
        ));
    }


}
