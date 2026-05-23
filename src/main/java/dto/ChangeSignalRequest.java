package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import model.Direction;
import model.LightColor;

@Data
public class ChangeSignalRequest {
    @NotBlank
    private String intersectionId;

    @NotNull
    private Direction direction;

    @NotNull
    private LightColor color;
}
