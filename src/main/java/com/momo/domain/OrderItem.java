package com.momo.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //Order 객체 직접 생성 방지
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //XToOne 은 기본 FetchType이 EAGER이기 때문에 지연로딩인 LAZY로 바꿔야 한다.
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY) //XToOne 은 기본 FetchType이 EAGER이기 때문에 지연로딩인 LAZY로 바꿔야 한다.
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //하루 일당
    private int day; //주문 일수


    /**
     * 주문상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getDay();
    }



}
