package com.pratap.traffic.controller;

import com.pratap.traffic.service.TrafficLightService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrafficControllerTest {

    @Mock
    private TrafficLightService service;

    @InjectMocks
    private TrafficController controller;

    @Test
    void shouldPauseController() {

        ResponseEntity<String> response = controller.pause();

        assertEquals(200, response.getStatusCode());
        assertEquals("Traffic controller paused", response.getBody());

        verify(service).pause();
    }
}
