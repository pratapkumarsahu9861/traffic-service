package service;

import exception.TrafficException;
import model.Direction;
import model.LightColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TrafficLightServiceTest {
    private TrafficLightService service;

    @BeforeEach
    void setup() {

        service = new TrafficLightService();
        service.init();
    }

    @Test
    void shouldRejectSecondGreenSignal() {

        service.changeSignal(
                "MAIN_JUNCTION",
                Direction.NORTH,
                LightColor.GREEN);

        assertThrows(
                TrafficException.class,
                () -> service.changeSignal(
                        "MAIN_JUNCTION",
                        Direction.SOUTH,
                        LightColor.GREEN));
    }
}
