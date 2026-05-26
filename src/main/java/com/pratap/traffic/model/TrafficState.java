package com.pratap.traffic.model;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrafficState {
    private String direction;
    private String color;
    private LocalDateTime updatedTime;

}
