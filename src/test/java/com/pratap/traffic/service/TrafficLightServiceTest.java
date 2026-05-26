package com.pratap.traffic.service;

import com.pratap.traffic.exception.TrafficException;
import com.pratap.traffic.model.TrafficHistory;
import com.pratap.traffic.util.ApplicationConstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
                ApplicationConstant.NORTH,
                ApplicationConstant.GREEN);

        assertThrows(
                TrafficException.class,
                () -> service.changeSignal(
                        "MAIN_JUNCTION",
                        ApplicationConstant.SOUTH,
                        ApplicationConstant.GREEN));
    }

    @Test
    void shouldAllowFirstGreenSignal() {

        service.changeSignal(
                "MAIN_JUNCTION",
                ApplicationConstant.NORTH,
                ApplicationConstant.GREEN);
    }

    @Test
    void shouldRejectConflictingGreenSignal() {

        service.changeSignal(
                "MAIN_JUNCTION",
                ApplicationConstant.NORTH,
                ApplicationConstant.GREEN);

        assertThrows(
                TrafficException.class,
                () -> service.changeSignal(
                        "MAIN_JUNCTION",
                        ApplicationConstant.EAST,
                        ApplicationConstant.GREEN));
    }

    @Test
    void shouldAllowNorthAndSouthGreenTogether() {

        service.changeSignal(
                "MAIN_JUNCTION",
                ApplicationConstant.NORTH,
                ApplicationConstant.GREEN);

        service.changeSignal(
                "MAIN_JUNCTION",
                ApplicationConstant.SOUTH,
                ApplicationConstant.GREEN);
    }
    @Test
    void shouldAllowSignalToBecomeRed() {

        service.changeSignal(
                "MAIN_JUNCTION",
                ApplicationConstant.NORTH,
                ApplicationConstant.GREEN);

        service.changeSignal(
                "MAIN_JUNCTION",
                ApplicationConstant.NORTH,
                ApplicationConstant.RED);
    }

    @Test
    void shouldRejectInvalidDirection() {

        assertThrows(
                TrafficException.class,
                () -> service.changeSignal(
                        "MAIN_JUNCTION",
                        "UP",
                        ApplicationConstant.GREEN));
    }
    @Test
    void shouldRejectInvalidColor() {

        assertThrows(
                TrafficException.class,
                () -> service.changeSignal(
                        "MAIN_JUNCTION",
                        ApplicationConstant.NORTH,
                        "BLUE"));
    }
    @Test
    void shouldRejectUnknownIntersection() {

        assertThrows(
                RuntimeException.class,
                () -> service.changeSignal(
                        "UNKNOWN",
                        ApplicationConstant.NORTH,
                        ApplicationConstant.GREEN));
    }

    @Test
    void shouldRejectSignalChangeWhenPaused() {

        service.pause();

        assertThrows(
                TrafficException.class,
                () -> service.changeSignal(
                        "MAIN_JUNCTION",
                        ApplicationConstant.NORTH,
                        ApplicationConstant.GREEN));
    }
    @Test
    void shouldAllowSignalChangeAfterResume() {

        service.pause();
        service.resume();

        service.changeSignal(
                "MAIN_JUNCTION",
                ApplicationConstant.NORTH,
                ApplicationConstant.GREEN);
    }

    @Test
    void shouldRecordHistory() {

        service.changeSignal(
                "MAIN_JUNCTION",
                ApplicationConstant.NORTH,
                ApplicationConstant.GREEN);

        assertEquals(
                1,
                service.getHistory().size());
    }

    @Test
    void shouldStorePreviousAndNewColorInHistory() {

        service.changeSignal(
                "MAIN_JUNCTION",
                ApplicationConstant.NORTH,
                ApplicationConstant.GREEN);

        TrafficHistory history =
                service.getHistory().get(0);

        assertEquals(
                ApplicationConstant.RED,
                history.getPreviousColor());

        assertEquals(
                ApplicationConstant.GREEN,
                history.getCurrentColor());

    }


}
