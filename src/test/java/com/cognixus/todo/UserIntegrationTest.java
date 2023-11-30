package com.cognixus.todo;

import com.cognixus.todo.base.TestBase;
import com.cognixus.todo.dto.UnitTestRequest;
import com.cognixus.todo.enums.HttpMethod;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserIntegrationTest extends TestBase {


    @Test
    @Order(10)
    public void When_noToken_Then_returnInvalidToken() {
        //get un-auth when no token provided
        verifyInvalidToken("/user", HttpMethod.POST);
        verifyInvalidToken("/user/name/ck", HttpMethod.GET);
        verifyInvalidToken("/user/me", HttpMethod.DELETE);
        verifyInvalidToken("/user/name/ck", HttpMethod.DELETE);
    }

    @Test
    @Order(20)
    public void When_getUserNameWithAccess_Then_returnData() {
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.GET)
                .url("/user/name/ck")
                .responseFile("data/user/positive/nameResponse.json")
                .token(this.getToken())
                .statusResult(status().isOk())
                .customComparator(getStandardComparator())
                .build());
    }


}
