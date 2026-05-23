package com.pratap.traffic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public class TrafficHistory {
    private String intersectionId;

    private Direction direction;

    private LightColor previousColor;

    private LightColor currentColor;

    private LocalDateTime timestamp;
}
