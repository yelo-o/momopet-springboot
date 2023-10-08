package com.momo.controller;

import com.momo.config.auth.LoginUser;
import com.momo.config.auth.dto.SessionUser;
import com.momo.domain.*;
import com.momo.domain.user.User;
import com.momo.service.ItemService;
import com.momo.service.MemberService;
import com.momo.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/orders/new/{itemId}")
    public String createOrderForm(@PathVariable Long itemId, @LoginUser SessionUser user) {
        //엔티티 조회
        User findUser = memberService.findOne(user.getEmail());
        Item item = itemService.findOne(itemId);

        //주문 생성
        Order order = new Order();
        order.setUser(findUser);
        order.setOrderDate(LocalDateTime.now());
        order.setStartDate(item.getStartDate());
        order.setEndDate(item.getEndDate());
        order.setStatus(OrderStatus.PENDING);

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item);
        order.addOrderItem(orderItem);

        //주문 저장
        orderService.saveOrder(order, orderItem);
        log.info("오더아이템 아이디: " + orderItem.getId());

        return "redirect:/items";
    }

/*    @GetMapping("/orders/new/{itemId}")
    public String createOrderForm(@PathVariable Long itemId, @LoginUser SessionUser user) {
        User findUser = memberService.findOne(user.getEmail());
        Item item = itemService.findOne(itemId);

        Order order = new Order();

        order.setUser(findUser);
        order.setOrderDate(LocalDateTime.now());
        order.setStartDate(item.getStartDate());
        order.setEndDate(item.getEndDate());
        order.setStatus(OrderStatus.PENDING);

        orderService.saveOrder(order);
        return "redirect:/items";
    }*/

}
