package com.pimaua.deliveryService.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LiveDriverLocationId implements Serializable {
    private Integer driverId;
    private LocalDateTime timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;  // Same object, they are equal
        if (!(o instanceof LiveDriverLocationId)) return false;  // Check if it's the same class
        LiveDriverLocationId that = (LiveDriverLocationId) o;  // Cast to the same type
        return Objects.equals(driverId, that.driverId) &&  // Compare fields
                Objects.equals(timestamp, that.timestamp);  // Compare fields
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverId, timestamp);  // Generate hash based on fields
    }
}
