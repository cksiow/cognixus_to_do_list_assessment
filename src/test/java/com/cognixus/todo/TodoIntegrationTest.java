package com.cognixus.todo;

import com.cognixus.todo.base.TestBase;
import com.cognixus.todo.dto.UnitTestRequest;
import com.cognixus.todo.enums.HttpMethod;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoIntegrationTest extends TestBase {


    @Test
    @Order(10)
    public void When_saveWithoutMandatoryData_Then_returnInvalid() {
        //without name
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.POST)
                .url("/todo")
                .requestFile("data/todo/negative/withoutName.json")
                .responseFile("data/todo/negative/withoutNameResponse.json")
                .token(this.getToken())
                .statusResult(status().is4xxClientError())
                .build());
        //without description
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.POST)
                .url("/todo")
                .requestFile("data/todo/negative/withoutDescription.json")
                .responseFile("data/todo/negative/withoutDescriptionResponse.json")
                .token(this.getToken())
                .statusResult(status().is4xxClientError())
                .build());
    }

    @Test
    @Order(20)
    public void When_list_Then_returnResults() {
        //all
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.GET)
                .url("/todo")
                .responseFile("data/todo/positive/getAllResponse.json")
                .token(this.getToken())
                .statusResult(status().isOk())
                .customComparator(this.getStandardComparator())
                .build());
        //completed
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.GET)
                .url("/todo?completed=true")
                .responseFile("data/todo/positive/getCompletedResponse.json")
                .token(this.getToken())
                .statusResult(status().isOk())
                .customComparator(this.getStandardComparator())
                .build());
        //pending
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.GET)
                .url("/todo?completed=false")
                .responseFile("data/todo/positive/getPendingResponse.json")
                .token(this.getToken())
                .statusResult(status().isOk())
                .customComparator(this.getStandardComparator())
                .build());
    }

    @Test
    @Order(30)
    public void When_save_Then_returnResults() {
        //success
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.POST)
                .url("/todo")
                .requestFile("data/todo/positive/test01.json")
                .responseFile("data/todo/positive/test01Response.json")
                .token(this.getToken())
                .statusResult(status().isOk())
                .customComparator(this.getStandardComparator())
                .build());

    }

    @Test
    @Order(40)
    public void When_deleteWithoutAccess_Then_returnInvalid() {
        //without name
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.DELETE)
                .url("/todo")
                .requestFile("data/todo/negative/deleteWrongIds.json")
                .responseFile("data/todo/negative/deleteWrongIdsResponse.json")
                .token(this.getToken())
                .statusResult(status().is4xxClientError())
                .build());

    }

    @Test
    @Order(50)
    public void When_delete_Then_verifyEmpty() {
        //delete 1 complete
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.DELETE)
                .url("/todo")
                .requestFile("data/todo/positive/delete01.json")
                .token(this.getToken())
                .statusResult(status().isOk())
                .customComparator(this.getStandardComparator())
                .build());
        //verify latest complete data
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.GET)
                .url("/todo?completed=true")
                .responseFile("data/todo/positive/getAfterDeletedResponse.json")
                .token(this.getToken())
                .statusResult(status().isOk())
                .customComparator(this.getStandardComparator())
                .build());

    }

    @Test
    @Order(60)
    public void When_completedWithoutAccess_Then_returnInvalid() {
        //without name
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.POST)
                .url("/todo/completed")
                .requestFile("data/todo/negative/completedWrongIds.json")
                .responseFile("data/todo/negative/completedWrongIdsResponse.json")
                .token(this.getToken())
                .statusResult(status().is4xxClientError())
                .build());

    }

    @SneakyThrows
    @Test
    @Order(70)
    public void When_completed_Then_returnResults() {
        //complete 1 pending
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.POST)
                .url("/todo/completed")
                .requestFile("data/todo/positive/completed01.json")
                .responseFile("data/todo/positive/completedResponse.json")
                .token(this.getToken())
                .statusResult(status().isOk())
                .customComparator(this.getStandardComparator())
                .build());

    }
}
