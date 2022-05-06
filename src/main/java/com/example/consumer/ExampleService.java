package com.example.consumer;

import com.example.consumer.dto.ResponseStatus;
import com.example.consumer.dto.TestRequest;
import com.example.consumer.dto.TestResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * $Id$
 * <p>
 * Created 24/01/2022 by maciasavila
 * </p>
 * <p>
 * [Add comment here]
 * </p>
 * <p>
 * Copyright: Copyright (c) 2020
 * Organisation: Igel Technology GmbH
 * </p>
 */
@Service
public class ExampleService
{
    @Value("${producer.url}")
    public String producerUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String test1()
    {
        TestRequest requestBody = new TestRequest(1L, "testData");

        RequestEntity<TestRequest> request = RequestEntity
            .post(producerUrl + "/test")
            .accept(MediaType.APPLICATION_JSON)
            .body(requestBody);

        TestResponse producerResponse = restTemplate.exchange(request,TestResponse.class).getBody();
        // Do something else with the response
        if (producerResponse.getStatus() != ResponseStatus.OK)
        {
            throw new RuntimeException("the response doesnt have the correct status");
        }


        return "this works well";
    }

    public String test2()
    {
        TestRequest requestBody = new TestRequest(2L, "testData");

        RequestEntity<TestRequest> request = RequestEntity
            .post(producerUrl + "/test")
            .accept(MediaType.APPLICATION_JSON)
            .body(requestBody);

        try{
            restTemplate.exchange(request,TestResponse.class).getBody();
        }catch (HttpClientErrorException.BadRequest ex){
            System.out.println(">>>>>>>>> The expected Exception in handled: " + ex);
        }

        return "this works well";
    }
}