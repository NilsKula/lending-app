package io.codelex.lendingapp;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimeSupplier {
    private LocalDateTime time = LocalDateTime.now();

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
