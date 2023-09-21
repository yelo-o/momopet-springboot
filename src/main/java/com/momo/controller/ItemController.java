package com.momo.controller;

import com.momo.domain.Item;
import com.momo.domain.member.Member;
import com.momo.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new ItemForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(ItemForm form) {
        Item item = new Item();
        item.setNickname(form.getNickname());
        item.setIntroduction(form.getIntroduction());
        item.setPrice(form.getPrice());
        item.setAvailableDate(form.getAvailableDate());

        itemService.saveItem(item);
        return "redirect:/";
    }

}
