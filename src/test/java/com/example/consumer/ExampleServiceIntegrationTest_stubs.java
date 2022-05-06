package com.example.consumer;

import com.example.consumer.dto.ResponseStatus;
import com.example.consumer.dto.TestRequest;
import com.example.consumer.dto.TestResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/*@AutoConfigureStubRunner(
    stubsMode = StubRunnerProperties.StubsMode.LOCAL,
//    stubsMode = StubRunnerProperties.StubsMode.REMOTE,
//    repositoryRoot = "git://git@github.com:spring-cloud-samples/spring-cloud-contract-nodejs-contracts-git.git",
    ids = "com.example.producer:+:stubs:9090"
)*/
@ActiveProfiles({"contract-testing"})
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExampleServiceIntegrationTest_stubs
{

    @RegisterExtension
    public StubRunnerExtension stubRunnerExtension = new StubRunnerExtension()
        .downloadStub("com.example", "producer" /*, "0.0.1-SNAPSHOT"*/)
//        .repoRoot("http://nexusServer.local")
        .withPort(9090)
        .stubsMode(StubRunnerProperties.StubsMode.LOCAL);

    @Autowired
    ExampleService exampleService;

    RestTemplate restTemplate = new RestTemplate();

    @Test
    @Order(1)
    void test_the_stub_directly()
    {
        String stubUrl = "http://localhost:9090";

        TestRequest requestOK = new TestRequest(1L, "testData");
        ResponseEntity<TestResponse> responseEntity = restTemplate.postForEntity(stubUrl + "/test", requestOK, TestResponse.class);

        TestResponse producerResponse = responseEntity.getBody();
        assertThat(producerResponse.getStatus()).isEqualTo(ResponseStatus.OK);

        /** Second case: */
        TestRequest requestNotOK = new TestRequest(2L, "testData");
        assertThatThrownBy(() -> {
            restTemplate.postForEntity(stubUrl + "/test", requestNotOK, TestResponse.class);
        }).isInstanceOf(HttpClientErrorException.BadRequest.class);
    }

    @Test
    @Order(2)
    @DisplayName("The consumer should work well with the stubs (case 1)")
    void test1()
    {
        String s = this.exampleService.test1();

        // other assertions, etc.
        assertThat(s).isEqualTo("this works well");
    }

    @Test
    @Order(3)
    @DisplayName("The consumer should work well with the stubs (case 2)")
    void test2()
    {
        String s = this.exampleService.test2();

        // other assertions, etc.
        assertThat(s).isEqualTo("this works well");
    }
}