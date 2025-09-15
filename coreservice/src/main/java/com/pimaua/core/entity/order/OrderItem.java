package com.pimaua.core.entity.order;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "menu_item_id",nullable = false)
    private Integer menuItemId;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "quantity",nullable = false)
    private Integer quantity;
    @Column(name = "unit_price",nullable = false)
    private BigDecimal unitPrice;
    @Column(name = "total_price",nullable = false)
    private BigDecimal totalPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", nullable = false)
    private Order order;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
