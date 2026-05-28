package com.pratap.traffic.service;
import com.pratap.traffic.exception.ResourceNotFoundException;
import com.pratap.traffic.exception.TrafficException;
import com.pratap.traffic.model.Intersection;
import com.pratap.traffic.model.TrafficHistory;

import com.pratap.traffic.util.ApplicationConstant;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrafficLightService {
    private final Map<String, Intersection> intersections =
            new ConcurrentHashMap<>();

    private final List<TrafficHistory> history =
            Collections.synchronizedList(new ArrayList<>());

    private volatile boolean paused;

    @PostConstruct
    public void init() {

        Map<String, String> initialState = new HashMap<>();

        for(String direction : ApplicationConstant.directionList) {
            initialState.put(direction, ApplicationConstant.RED);
        }


        intersections.put(
                "MAIN_JUNCTION",
                new Intersection(
                        "MAIN_JUNCTION",
                        initialState));
    }

    public synchronized void changeSignal(
            String intersectionId,
            String direction,
            String newColor) {

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

        String previous =
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
            String direction,
            String color) {

        if (!ApplicationConstant.lightColorList.contains(color)) {
            throw new TrafficException("Invalid color "+ color);
        }
        if (!ApplicationConstant.directionList.contains(direction)) {
            throw new TrafficException("Invalid Direction "+ direction);
        }

        if (!color.equals(ApplicationConstant.GREEN)) {
            return;
        }
        Map<String, String> states = intersection.getSignalStates();
        
        Map<String, String> greenSignals =
                intersection.getSignalStates()
                        .entrySet()
                        .stream()
                        .filter(entry -> entry.getValue().equals(ApplicationConstant.GREEN))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue
                        ));
        if (greenSignals.isEmpty()) {
           return;
        }
        Set<String> greenSignalExist = greenSignals.keySet();

        if (direction.equals(ApplicationConstant.EAST) || direction.equals(ApplicationConstant.WEST)) {
            if (greenSignalExist.contains(ApplicationConstant.NORTH)
                    || greenSignalExist.contains(ApplicationConstant.SOUTH)) {
                throw new TrafficException(
                        "WEST/WEST cannnot be GREEN when NORTH/SOUTH is green");

            }
        } else if (direction.equals(ApplicationConstant.NORTH) || direction.equals(ApplicationConstant.SOUTH)) {
            if (greenSignalExist.contains(ApplicationConstant.EAST)
                    || greenSignalExist.contains(ApplicationConstant.WEST)) {
                throw new TrafficException(
                        "NORTH/SOUTH cannnot be GREEN when EAST/WEST is green");

            }
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
