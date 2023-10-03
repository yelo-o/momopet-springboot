package com.momo.controller;

import com.momo.config.auth.LoginUser;
import com.momo.config.auth.dto.SessionUser;
import com.momo.domain.Item;
import com.momo.domain.Status;
import com.momo.domain.member.PrivateInformation;
import com.momo.domain.user.User;
import com.momo.service.ItemService;
import com.momo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final MemberService memberService;

    /* Input 값이 없을 때 empty string "" 을 null 로 바꿔주는 initbinder (동적쿼리 위해 추가)*/
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/items/new")
    public String createForm(Model model, @LoginUser SessionUser user) {
        User findUser = memberService.findOne(user.getEmail());

        if (findUser.getUserType() == null) {
            return "redirect:/members/beSitter";
        }

        model.addAttribute("form", new ItemForm());
        model.addAttribute("user", user);
        model.addAttribute("privateInformation", findUser.getPrivateInformation());
        log.info("로그인 이름 : " + user.getName());

        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(ItemForm form, @LoginUser SessionUser user) {

        Item item = new Item();

        User findUser = memberService.findOne(user.getEmail());

        item.setSitter(findUser);
        item.setName(findUser.getName());
        item.setEmail(findUser.getEmail());
        item.setPicture(findUser.getPicture());
        item.setSi(findUser.getPrivateInformation().getAddress().getSi());
        item.setGu(findUser.getPrivateInformation().getAddress().getGu());

        item.setPrice(form.getPrice());
        item.setIntroduction(form.getIntroduction());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDateTime = LocalDateTime.parse(form.getStartDate(), formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(form.getEndDate(), formatter);

        item.setStartDate(startDateTime);
        item.setEndDate(endDateTime);

        item.setDog(form.getDog());
        item.setCat(form.getCat());
        item.setStatus(Status.활성화);

        itemService.saveItem(item);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(ItemForm form, Model model) {

        model.addAttribute("form", new ItemForm());

        List<Item> items = itemService.searchItems(form);
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/members/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemService.findOne(itemId);

        ItemForm form = new ItemForm();
        form.setPrice(item.getPrice());
        form.setIntroduction(item.getIntroduction());
        log.info("start 날짜타입 날짜:" + item.getStartDate());
        form.setStartDate(item.getStartDate().toString().replace("T", " "));
        form.setEndDate(item.getEndDate().toString().replace("T", " "));
        log.info("start 스트링타입 날짜:" + item.getStartDate().toString().replace("T", " "));

        form.setDog(item.getDog());
        form.setCat(item.getCat());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/members/items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") ItemForm form) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDateTime = LocalDateTime.parse(form.getStartDate(), formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(form.getEndDate(), formatter);

        itemService.updateItem(itemId, form.getPrice(), form.getIntroduction(), startDateTime, endDateTime, form.getDog(), form.getCat());
        return "redirect:/members/myItem";
    }

    @GetMapping("/members/items/{itemId}/delete")
    public String deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return "redirect:/members/myItem";
    }


/*    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }*/


}
