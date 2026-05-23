package controller;

import dto.ChangeSignalRequest;
import jakarta.validation.Valid;
import model.TrafficCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.TrafficLightService;

@RestController
@RequestMapping("/traffic")
public class TrafficController {
    private final TrafficLightService service;

    public TrafficController(
            TrafficLightService service) {
        this.service = service;
    }

    @PostMapping("/change")
    public ResponseEntity<String> changeSignal(
            @Valid @RequestBody
            ChangeSignalRequest request) {

        service.changeSignal(
                request.getIntersectionId(),
                request.getDirection(),
                request.getColor());

        return ResponseEntity.ok(
                "Signal updated successfully");
    }

    @PostMapping("/pause")
    public ResponseEntity<String> pause() {

        service.pause();

        return ResponseEntity.ok(
                "Traffic controller paused");
    }

    @PostMapping("/resume")
    public ResponseEntity<String> resume() {

        service.resume();

        return ResponseEntity.ok(
                "Traffic controller resumed");
    }

    @GetMapping("/state")
    public ResponseEntity<?> state() {
        return ResponseEntity.ok(
                service.getAllIntersections());
    }

    @GetMapping("/history")
    public ResponseEntity<?> history() {
        return ResponseEntity.ok(
                service.getHistory());
    }
}
