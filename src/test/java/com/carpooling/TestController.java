package com.carpooling;

import com.carpooling.controller.CarPoolingController;
import com.carpooling.model.Car;
import com.carpooling.model.CarDTO;
import com.carpooling.model.JourneyDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestController {

    static private List<CarDTO> cars;
    static private List<JourneyDTO> journeys;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private CarPoolingController controller;

    /**
     * Called before the test.
     * - Initialize the list of cars.
     */
    @BeforeAll
    static void init() {
        cars = new ArrayList<>(4);
        cars.add(new CarDTO(0L, 2));
        cars.add(new CarDTO(1L, 5));

        journeys = new ArrayList<>(2);
        journeys.add(new JourneyDTO(0L, 4));
        journeys.add(new JourneyDTO(1L, 3));
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    /**
     * Check that the /status GET API return HttpStatus.OK
     *
     * @throws Exception
     */
    @Test
    void getStatusApi() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/status", HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Check that the /cars PUT API return HttpStatus.OK and the list of cars passed.
     *
     * @throws Exception
     */
    @Test
    @Order(1)
    void putCarApi() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<Object>(cars, headers);

        ResponseEntity<List<CarDTO>> response = restTemplate.exchange(
                "http://localhost:" + port + "/cars",
                HttpMethod.PUT,
                entity,
                new ParameterizedTypeReference<List<CarDTO>>() {
                });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cars, response.getBody());
    }

    /**
     * Check that the /cars PUT API return HttpStatus.BAD_REQUEST if passing invalid request body.
     *
     * @throws Exception
     */
    @Test
    void putCarApiInvalidRequestBody() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/cars",
                HttpMethod.PUT,
                entity,
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Require putCarAPI to be called in advance.
     * Check that the /cars GET API return HttpStatus.OK and the list of cars in the system.
     *
     * @throws Exception
     */
    @Test
    @Order(2)
    void getCarApi() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        ResponseEntity<List<CarDTO>> response = restTemplate.exchange(
                "http://localhost:" + port + "/cars",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<CarDTO>>() {
                });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cars, response.getBody());
    }

    /**
     * Check that the /journey POST API return HttpStatus.ACCEPTED.
     * Post two journeys
     *
     * @throws Exception
     */
    @Test
    @Order(3)
    void postJourneyApi() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> response;
        HttpEntity<Object> entity;

        // journey 1
        entity = new HttpEntity<Object>(journeys.get(0), headers);
        response = restTemplate.exchange(
                "http://localhost:" + port + "/journey",
                HttpMethod.POST,
                entity,
                String.class);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        // journey 2
        entity = new HttpEntity<Object>(journeys.get(1), headers);
        response = restTemplate.exchange(
                "http://localhost:" + port + "/journey",
                HttpMethod.POST,
                entity,
                String.class);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    /**
     * Check that the /journey GET API return HttpStatus.Ok.
     *
     * @throws Exception
     */
    @Test
    @Order(4)
    void getJourneysApiStatusOK() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        ResponseEntity<List<JourneyDTO>> response = restTemplate.exchange(
                "http://localhost:" + port + "/journeys",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<JourneyDTO>>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(journeys, response.getBody());
    }


    /**
     * Check that the /journey POST API return HttpStatus.BAD_REQUEST if passing invalid request body.
     *
     * @throws Exception
     */
    @Test
    void postJourneyApiInvalidRequestBody() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<Object>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/journey",
                HttpMethod.POST,
                entity,
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Check that the /locate POST API return HttpStatus.Ok.
     *
     * @throws Exception
     */
    @Test
    @Order(5)
    void postLocateApiStatusOK() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("ID", "0");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<CarDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/locate",
                HttpMethod.POST,
                entity,
                CarDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cars.get(1).getId(), response.getBody().getId());
    }

    /**
     * Check that the /locate POST API return HttpStatus.NO_CONTENT.
     *
     * @throws Exception
     */
    @Test
    @Order(6)
    void postLocateApiStatusNoContent() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("ID", "1");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<CarDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/locate",
                HttpMethod.POST,
                entity,
                CarDTO.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Check that the /locate POST API return HttpStatus.NOT_FOUND.
     *
     * @throws Exception
     */
    @Test
    void postLocateApiStatusNotFound() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("ID", "100000");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<CarDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/locate",
                HttpMethod.POST,
                entity,
                CarDTO.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Check that the /locate POST API return HttpStatus.BAD_REQUEST.
     * The payload can't be unmarshalled, wrong key value.
     *
     * @throws Exception
     */
    @Test
    void postLocateApiStatusBadRequestWrongKeyValue() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("wrongKey", "100000");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<CarDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/locate",
                HttpMethod.POST,
                entity,
                CarDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Check that the /locate POST API return HttpStatus.BAD_REQUEST.
     * The key "ID" appears several times.
     *
     * @throws Exception
     */
    @Test
    void postLocateApiStatusBadRequestDuplicateKey() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("ID", "100000");
        journeyId.add("ID", "100000");
        journeyId.add("ID", "100000");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<CarDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/locate",
                HttpMethod.POST,
                entity,
                CarDTO.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Check that the /dropoff POST API return HttpStatus.Ok.
     *
     * @throws Exception
     */
    @Test
    @Order(7)
    void postDropOffApiStatusOK() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("ID", "0");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/dropoff",
                HttpMethod.POST,
                entity,
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Check that the /dropoff POST API return HttpStatus.NOT_FOUND.
     *
     * @throws Exception
     */
    @Test
    void postDropOffApiStatusNotFound() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("ID", "100000");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<Car> response = restTemplate.exchange(
                "http://localhost:" + port + "/dropoff",
                HttpMethod.POST,
                entity,
                Car.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Check that the /dropoff POST API return HttpStatus.BAD_REQUEST.
     * The payload can't be unmarshalled, wrong key value.
     *
     * @throws Exception
     */
    @Test
    void postDropOffApiStatusBadRequestWrongKeyValue() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("wrongKey", "100000");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<Car> response = restTemplate.exchange(
                "http://localhost:" + port + "/dropoff",
                HttpMethod.POST,
                entity,
                Car.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Check that the /dropoff POST API return HttpStatus.BAD_REQUEST.
     * The key "ID" appears several times.
     *
     * @throws Exception
     */
    @Test
    void postDropOffApiStatusBadRequestDuplicateKey() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("ID", "100000");
        journeyId.add("ID", "100000");
        journeyId.add("ID", "100000");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<Car> response = restTemplate.exchange(
                "http://localhost:" + port + "/dropoff",
                HttpMethod.POST,
                entity,
                Car.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Check that the /locate POST API return HttpStatus.Ok.
     * For the Journey ID 1, this journey was waiting to be assigned to a car.
     *
     * @throws Exception
     */
    @Test
    @Order(8)
    void postLocateApiStatusOKAfterWaiting() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        LinkedMultiValueMap<String, String> journeyId = new LinkedMultiValueMap<>();
        journeyId.add("ID", "1");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Object> entity = new HttpEntity<Object>(journeyId, headers);

        ResponseEntity<CarDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/locate",
                HttpMethod.POST,
                entity,
                CarDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cars.get(1).getId(), response.getBody().getId());
    }
}
