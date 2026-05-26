package com.pratap.traffic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
public class TrafficHistory {
    private String intersectionId;

    private String direction;

    private String previousColor;

    private String currentColor;

    private LocalDateTime timestamp;
}
