package com.pratap.traffic.model;
import java.time.LocalDateTime;

public class TrafficState {
    private Direction direction;
    private LightColor color;
    private LocalDateTime updatedTime;

    public TrafficState() {
    }

    public TrafficState(Direction direction,
                        LightColor color,
                        LocalDateTime updatedTime) {
        this.direction = direction;
        this.color = color;
        this.updatedTime = updatedTime;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public LightColor getColor() {
        return color;
    }

    public void setColor(LightColor color) {
        this.color = color;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
