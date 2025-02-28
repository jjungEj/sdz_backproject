package com.elice.sdz.order.entity;

import com.elice.sdz.user.entity.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id") // 외래 키 컬럼을 지정
    private Users userId; // Users 엔티티와의 관계

    @Column(name = "order_count", nullable = false)
    private int orderCount; //수량

    @Column(name = "order_amount", nullable = false)
    private Double orderAmount; //가격

    @Column(name = "reg_date", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant regDate; //주문일

    @Column(name = "refund_status", nullable = false,
            columnDefinition = "BIT(1) DEFAULT 0")
    private boolean refundStatus; //환불 가능여부

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private Status orderStatus; //주문 상태

    public enum Status { //주문 상태 나타내는 enum
        PENDING,
        PAYMENTPROCESSED,
        DELIVERYPROCESSED,
        REFUNDPROCESSED;
    }
}
