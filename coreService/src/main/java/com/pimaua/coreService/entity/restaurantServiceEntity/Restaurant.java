package com.pimaua.coreService.entity.restaurantServiceEntity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private String address;
    @Column(name="is_active")
    private Boolean isActive;
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu>menus=new ArrayList<>();
    @OneToMany(mappedBy = "restaurant",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpeningHours>openingHoursList=new ArrayList<>();

    @Builder
    public Restaurant(String name, String description, String address, boolean isActive) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.isActive = isActive;
    }
}
