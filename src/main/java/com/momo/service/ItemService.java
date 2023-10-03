package com.momo.service;

import com.momo.controller.ItemForm;
import com.momo.domain.Item;
import com.momo.domain.Status;
import com.momo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, int price, String introduction, LocalDateTime startDate, LocalDateTime endDate, Boolean dog, Boolean cat) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(price);
        findItem.setIntroduction(introduction);
        findItem.setStartDate(startDate);
        findItem.setEndDate(endDate);
        findItem.setDog(dog);
        findItem.setCat(cat);
    }

    public void deleteItem(Long itemId) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setStatus(Status.비활성화);

    }
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public List<Item> findMyItems(String email) {
        return itemRepository.findMyItems(email);
    }

    public List<Item> searchItems(ItemForm form) {
        return itemRepository.searchItems(form);
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
