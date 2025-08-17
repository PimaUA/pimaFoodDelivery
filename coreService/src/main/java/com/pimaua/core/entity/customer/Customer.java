package com.pimaua.core.entity.customer;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    @Column(nullable = false)
    private String name;
    @Column(name="phone_number", nullable = false)
    private String phoneNumber;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
