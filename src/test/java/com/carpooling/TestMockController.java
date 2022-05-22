package com.carpooling;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/*
 * Let's start our testing adventure with a test class that uses MockMvc. In this approach, the Spring Boot application server will not be started but the code will be called exactly in the same way as if it handling an HTTP request.
 * Ref: https://frontbackend.com/spring-boot/spring-boot-2-junit-5-mockito
 */

@SpringBootTest
@AutoConfigureMockMvc
class TestMockController {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void integrationTest() throws Exception {
        this.mockMvc.perform(get("/status")).andDo(print())
                .andExpect(status().isOk());
    }
}
