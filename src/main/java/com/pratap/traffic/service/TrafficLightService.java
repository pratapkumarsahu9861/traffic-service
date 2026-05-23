package com.pratap.traffic.service;
import com.pratap.traffic.exception.ResourceNotFoundException;
import com.pratap.traffic.exception.TrafficException;
import com.pratap.traffic.model.Direction;
import com.pratap.traffic.model.Intersection;
import com.pratap.traffic.model.LightColor;
import com.pratap.traffic.model.TrafficHistory;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TrafficLightService {
    private final Map<String, Intersection> intersections =
            new ConcurrentHashMap<>();

    private final List<TrafficHistory> history =
            Collections.synchronizedList(new ArrayList<>());

    private volatile boolean paused;

    @PostConstruct
    public void init() {

        Map<Direction, LightColor> initialState =
                new EnumMap<>(Direction.class);

        for (Direction direction : Direction.values()) {
            initialState.put(direction, LightColor.RED);
        }

        intersections.put(
                "MAIN_JUNCTION",
                new Intersection(
                        "MAIN_JUNCTION",
                        initialState));
    }

    public synchronized void changeSignal(
            String intersectionId,
            Direction direction,
            LightColor newColor) {

        if (paused) {
            throw new TrafficException(
                    "Traffic controller is paused");
        }

        Intersection intersection =
                intersections.get(intersectionId);

        if (intersection == null) {
            throw new ResourceNotFoundException(
                    "Intersection not found");
        }

        validateGreenConflict(
                intersection,
                direction,
                newColor);

        LightColor previous =
                intersection.getSignalStates()
                        .get(direction);

        intersection.getSignalStates()
                .put(direction, newColor);

        history.add(
                new TrafficHistory(
                        intersectionId,
                        direction,
                        previous,
                        newColor,
                        LocalDateTime.now()));
    }

    private void validateGreenConflict(
            Intersection intersection,
            Direction direction,
            LightColor color) {

        if (color != LightColor.GREEN) {
            return;
        }

        boolean anotherGreen =
                intersection.getSignalStates()
                        .entrySet()
                        .stream()
                        .anyMatch(entry ->
                                entry.getKey() != direction
                                        && entry.getValue()
                                        == LightColor.GREEN);

        if (anotherGreen) {
            throw new TrafficException(
                    "Another direction is already GREEN");
        }
    }

    public Collection<Intersection> getAllIntersections() {
        return intersections.values();
    }

    public List<TrafficHistory> getHistory() {
        return history;
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public boolean isPaused() {
        return paused;
    }
}
