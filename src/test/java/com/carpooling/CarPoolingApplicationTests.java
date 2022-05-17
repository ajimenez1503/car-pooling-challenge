package com.carpooling;

import com.carpooling.controller.CarPoolingController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarPoolingApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CarPoolingController controller;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }


    @Test
    public void getStatusAPI() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/status", HttpMethod.GET, entity, String.class);

        assertNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
