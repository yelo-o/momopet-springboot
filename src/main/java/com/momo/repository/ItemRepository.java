package com.momo.repository;

import com.momo.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) { //저장된 것이 없으면 새것을 호출
            em.persist(item); //persist로 등록
        } else {
            em.merge(item);
        }
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }
}
