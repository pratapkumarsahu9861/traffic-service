package com.pratap.traffic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeSignalRequest {
    @NotBlank
    private String intersectionId;

    @NotNull
    private String direction;

    @NotNull
    private String color;
}
