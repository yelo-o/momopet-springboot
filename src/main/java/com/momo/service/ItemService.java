package com.momo.service;

import com.momo.domain.Item;
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

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
