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
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/orders/new/{itemId}")
    public String createOrder(@ModelAttribute("form") ItemForm form, @PathVariable Long itemId, @LoginUser SessionUser user) {
        //엔티티 조회
        User findUser = memberService.findOne(user.getEmail());
        Item item = itemService.findOne(itemId);

        //주문 생성
        Order order = new Order();
        order.setUser(findUser);
        order.setOrderDate(LocalDateTime.now());

        if (form.getStartDate().isEmpty()) {
            order.setStartDate(item.getStartDate());
            order.setEndDate(item.getEndDate());
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime startDateTime = LocalDateTime.parse(form.getStartDate(), formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(form.getEndDate(), formatter);
            order.setStartDate(startDateTime);
            order.setEndDate(endDateTime);
        }
        order.setStatus(OrderStatus.PENDING);
        log.info("스타트데이트: " + form.getStartDate());
        log.info("엔드데이트: " + form.getEndDate());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item);
        order.addOrderItem(orderItem);

        //주문 저장
        orderService.saveOrder(order, orderItem);
        log.info("오더아이템 아이디: " + orderItem.getId());

        return "redirect:/items";
    }

    @GetMapping("/members/orders/{orderId}/accepted")
    public String acceptOrder(@PathVariable Long orderId, @LoginUser SessionUser user,
                              Model model) {
        Order order = orderService.findOne(orderId);

        //결제 진행
        User buyer = order.getUser();
        User seller = memberService.findOne(user.getEmail());

        //전체 금액(공급가+부가세)
        int totalPrice = order.getTotalPrice(); //11,000원

        //공급가
        int supplyPrice = (int) (totalPrice / 1.1); //10,000원

        //부가세
        int tax = totalPrice - supplyPrice; //1,000원

        //구매자 포인트 잔액 확인
        memberService.checkBalance(buyer);
        log.info("구매자 포인트 잔액 : " + buyer.getBalance());

        // 구매자의 금액 확인
        if (buyer.getBalance() < totalPrice) {
            model.addAttribute("message", "구매자의 잔액이 부족합니다.");
            model.addAttribute("searchUrl", "/members/myOrder");

            return "items/message";
        }

        //구매자에게 전체금액만큼 차감
        memberService.chargePoint(-Long.valueOf(totalPrice), buyer);
        //판매자에게 공급가만큼 추가
        memberService.chargePoint(Long.valueOf(supplyPrice), seller);

        // 상태 ACCEPTED로 변경
        order.setStatus(OrderStatus.ACCEPTED);

        return "redirect:/members/myOrder";
    }

    @GetMapping("/members/orders/{orderId}/declined")
    public String declineOrder(@PathVariable Long orderId) {
        Order order = orderService.findOne(orderId);
        order.setStatus(OrderStatus.DECLINED);
        return "redirect:/members/myOrder";
    }

    @GetMapping("/members/orders/{orderId}/completed")
    public String completedOrder(@PathVariable Long orderId) {
        Order order = orderService.findOne(orderId);
        order.setStatus(OrderStatus.COMPLETED);
        return "redirect:/members/myOrder";
    }

    @GetMapping("/members/orders/{orderId}/canceled")
    public String cancelOrder(@PathVariable Long orderId) {
        Order order = orderService.findOne(orderId);
        order.setStatus(OrderStatus.CANCELED);
        return "redirect:/members/myOrder";
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

/*    @GetMapping("/orders/new/{itemId}")
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
    }*/

}
