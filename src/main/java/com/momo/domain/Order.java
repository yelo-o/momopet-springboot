package com.momo.domain;

import net.bytebuddy.asm.Advice;

import com.momo.domain.member.Member;
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
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate; //주문 date
    private LocalDateTime startDate; //시팅 시작 date
    private LocalDateTime endDate; //시팅 끝나는 date

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문 상태 [PENDING, ACCEPTED, DECLINED, COMPLETED]

    @OneToOne(mappedBy = "order")
    private Review review;

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        //위의 코드를 줄인 문법
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }

}
