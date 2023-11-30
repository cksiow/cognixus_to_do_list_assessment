package com.cognixus.todo.base;

import com.cognixus.todo.Application;
import com.cognixus.todo.comparator.RegexNullableValueMatcher;
import com.cognixus.todo.dto.UnitTestRequest;
import com.cognixus.todo.dto.UnitTestResponse;
import com.cognixus.todo.dto.UserToken;
import com.cognixus.todo.enums.HttpMethod;
import com.cognixus.todo.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Mini version of unit test base
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {Application.class})
@Data
@Slf4j
public abstract class TestBase {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Value("${security.jwt.token.signkey}")
    private String signKey;

    List<UserToken> userTokens = new ArrayList<>();

    @BeforeAll
    public void beforeAll() {
        //full access
        String accessToken = generateToken("ck", 1L, List.of("Admin"));
        addUserTokens(accessToken);
    }

    private void addUserTokens(String accessToken) {
        userTokens.add(UserToken.builder()
                .accessToken(accessToken)
                .build());
    }

    protected String generateToken(String userName, Long id, List<String> roles) {
        return TokenUtil.generateToken(
                userName,
                id,
                "unittest",
                roles,
                signKey, 120
        );
    }

    public String getToken() {
        return userTokens.get(0).getAccessToken();
    }

    @SneakyThrows
    public UnitTestResponse getRequest(UnitTestRequest request) {
        //get request context, normally is json
        var requestContext = request.getRequestFile() == null
                ? null : Resources.toString(Resources.getResource(request.getRequestFile()), StandardCharsets.UTF_8);
        //massage the request Context by call the passing function
        if (request.getRequestContextInvoker() != null) {
            requestContext = request.getRequestContextInvoker().apply(requestContext);
        }
        //call post and set the request context etc
        MockHttpServletRequestBuilder builder = getMockHttpServletRequestBuilder(request, requestContext);
        //get the post result
        var result = mockMvc.perform(builder).andExpect(request.getStatusResult());
        //write the result as output for verify
        log.info("Verify actual result: {}", result.andReturn().getResponse().getContentAsString());


        //check is valid or not
        return checkJSONAssert(request, result);
    }

    public MockHttpServletRequestBuilder getMockHttpServletRequestBuilder(
            UnitTestRequest request, String requestContext) {
        //get the correct builder
        var builder = request.getMethod() == HttpMethod.GET ?
                MockMvcRequestBuilders.get(request.getUrl())
                : request.getMethod() == HttpMethod.DELETE ?
                MockMvcRequestBuilders.delete(request.getUrl())
                :
                MockMvcRequestBuilders.post(request.getUrl());

        //set application json
        builder = builder.contentType(MediaType.APPLICATION_JSON);
        //set body content if exists
        if (requestContext != null) {
            builder = builder.content(requestContext);
        }
        //set bearer token if exists
        if (request.getToken() != null) {
            builder.header("Authorization", "Bearer " + request.getToken());
        }
        return builder;
    }

    @SneakyThrows
    private UnitTestResponse checkJSONAssert(UnitTestRequest request, ResultActions result) {
        return checkJSONAssert(request, result.andReturn().getResponse().getContentAsString());
    }

    @SneakyThrows
    public UnitTestResponse checkJSONAssert(UnitTestRequest request, String actualResponse) {
        try {
            //allow user set charset for the response file read
            var expectedResponse = request.getResponseFile() == null ? ""
                    : Resources.toString(Resources.getResource(request.getResponseFile())
                    , StandardCharsets.UTF_8);
            if (!actualResponse.isEmpty()) {
                if (request.getCustomComparator() == null) {
                    JSONAssert.assertEquals(expectedResponse, actualResponse, true);
                } else if (request.getResponseFile() != null) {
                    JSONAssert.assertEquals(expectedResponse, actualResponse, request.getCustomComparator());
                }
            }
            return UnitTestResponse.builder()
                    .actualResponse(actualResponse)
                    .build();
        } catch (Exception e) {
            log.info("Failed verify, actual result: {}", actualResponse);
            throw e;
        }

    }

    public CustomComparator getStandardComparator() {
        // as long as id is numeric = fine, regardless under array or node
        return new CustomComparator(JSONCompareMode.STRICT
                , new Customization("**.id", new RegexNullableValueMatcher<>("\\d+", false))
                , new Customization("**.createdBy", new RegexNullableValueMatcher<>("\\d+", false))
        );
    }

    public void verifyInvalidToken(String url, HttpMethod method) {
        this.getRequest(UnitTestRequest.builder()
                .method(method)
                .url(url)
                .responseFile("data/general/negative/invalidTokenResponse.json")
                .statusResult(status().isUnauthorized())
                .build());
    }

}
