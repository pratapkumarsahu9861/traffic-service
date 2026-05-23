package com.pratap.traffic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class Intersection {
    private String intersectionId;

    private Map<Direction, LightColor> signalStates;
}
