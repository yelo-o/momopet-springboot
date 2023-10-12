package com.momo.controller;

import com.momo.config.auth.LoginUser;
import com.momo.config.auth.dto.SessionUser;
import com.momo.domain.Item;
import com.momo.domain.Status;
import com.momo.domain.user.User;
import com.momo.domain.user.UserType;
import com.momo.service.ItemService;
import com.momo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        model.addAttribute("user", findUser);
        log.info("로그인 이름 : " + user.getName());

        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(@ModelAttribute("form") @Valid ItemForm form, BindingResult result,
                         @LoginUser SessionUser user, Model model) {

        User findUser = memberService.findOne(user.getEmail());
        //createItemForm에 오류가 있는지 확인 (Validation)
        if (result.hasErrors()) {
            model.addAttribute(("user"), findUser);
            if (!form.getDog() && !form.getCat()) {
                result.addError(new FieldError("form", "dog", "적어도 1개 선택은 필수입니다."));
            }
            return "items/createItemForm";
        }
        //createItemForm에 개나 고양이 중 하나라도 선택했는지 확인 (Validation)
        if (!form.getDog() && !form.getCat()) {
            model.addAttribute(("user"), findUser);
            result.addError(new FieldError("form", "dog", "적어도 1개 선택은 필수입니다."));
            return "items/createItemForm";
        }

        Item item = new Item();

        item.setSitter(findUser);

        item.setPrice((int)(form.getPrice() * 1.1)); //부가세 포함
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
    public String list(ItemForm form, Model model, @LoginUser SessionUser user) {

        User findUser = memberService.findOne(user.getEmail());

        if (findUser.getUserType() == null) {
            return "redirect:/members/beSitter";
        }

        if (findUser.getUserType() == UserType.SITTER) {
            return "redirect:/members/myPet";
        }

        model.addAttribute("form", new ItemForm());

        List<Item> items = itemService.searchItems(form, user.getEmail());
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
