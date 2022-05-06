package com.example.consumer;

import com.example.consumer.dto.ResponseStatus;
import com.example.consumer.dto.TestRequest;
import com.example.consumer.dto.TestResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * This could be an example of the old approach using integration test with the real context loaded
 * so the real producer is used in this tests
 */

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExampleServiceIntegrationTest_real
{
    @Autowired
    ExampleService exampleService;

    RestTemplate restTemplate = new RestTemplate();

    @Test
    @Order(1)
    void test_the_real_producer_directly()
    {
        String producerUrl = "http://localhost:9180";

        TestRequest requestOK = new TestRequest(1L, "testData");
        ResponseEntity<TestResponse> responseEntity = restTemplate.postForEntity(producerUrl + "/test", requestOK, TestResponse.class);

        TestResponse producerResponse = responseEntity.getBody();
        assertThat(producerResponse.getStatus()).isEqualTo(ResponseStatus.OK);

        /** Second case: */
        TestRequest requestNotOK = new TestRequest(2L, "testData");
        assertThatThrownBy(() -> {
            restTemplate.postForEntity(producerUrl + "/test", requestNotOK, TestResponse.class);
        }).isInstanceOf(HttpClientErrorException.BadRequest.class);
    }

    @Test
    @DisplayName("The consumer should work well withOUT stubs (case 1)")
    @Order(2)
    void test1()
    {
        String s = this.exampleService.test1();

        // other assertions, etc.
        assertThat(s).isEqualTo("this works well");
    }

    @Test
    @DisplayName("The consumer should work well withOUT stubs (case 2)")
    @Order(3)
    void test2()
    {
        String s = this.exampleService.test1();

        // other assertions, etc.
        assertThat(s).isEqualTo("this works well");
    }
}