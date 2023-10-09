package com.momo.domain;

import com.momo.domain.user.User;
import net.bytebuddy.asm.Advice;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate; //주문 date
    private LocalDateTime startDate; //시팅 시작 date
    private LocalDateTime endDate; //시팅 끝나는 date

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문 상태 [PENDING, ACCEPTED, DECLINED, COMPLETED]

    @OneToOne(mappedBy = "order")
    private Review review;

    /**
     * 주문에 오더아이템 추가
     */
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        //위의 코드를 줄인 문법
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }


/*    public static Order createOrder(User user, int day, OrderItem... orderItems) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.PENDING);
        return order;
    }*/



}
