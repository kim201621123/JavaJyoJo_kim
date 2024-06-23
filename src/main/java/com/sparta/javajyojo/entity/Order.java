package com.sparta.javajyojo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.javajyojo.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class) // JPA Auditing 활성화
public class Order extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore // 순환 참조 방지를 위해 추가
    private List<OrderDetail> orderDetails;

    @Column(nullable = false)
    private String deliveryRequest;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    private String address;

    @Column(nullable = false)
    private int totalPrice;

    public Order(User user, String deliveryRequest, String address, OrderStatus orderStatus, int totalPrice) {
        this.user = user;
        this.deliveryRequest = deliveryRequest;
        this.address = address;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
    }
}