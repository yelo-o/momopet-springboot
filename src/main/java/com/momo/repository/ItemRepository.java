package com.momo.repository;

import com.momo.controller.ItemForm;
import com.momo.domain.Item;
import com.momo.domain.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

        //JPQL 사용
        /*List<Item> items = em.createQuery("select i from Item i where i.status = ?1", Item.class)
                .setParameter(1, Status.활성화)
                .getResultList();*/

        //Criteria 사용해서 활성화된 아이템들 날짜 순서대로 리스트 불러오기
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Item> cq = cb.createQuery(Item.class);

        Root<Item> i = cq.from(Item.class);
        Predicate statusEqual = cb.equal(i.get("status"), Status.활성화);

        javax.persistence.criteria.Order dateAsc = cb.asc(i.get("startDate"));

        cq.select(i)
                .where(statusEqual)
                .orderBy(dateAsc);

        List<Item> items = em.createQuery(cq).getResultList();

        return items;
    }

    public List<Item> searchItems(ItemForm form) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Item> cq = cb.createQuery(Item.class);

        Root<Item> i = cq.from(Item.class);

        List<Predicate> criteria = new ArrayList<Predicate>();

        criteria.add(cb.equal(i.get("status"), Status.활성화));

        if (form.getSi() != null) criteria.add(cb.equal(i.get("si"),
                cb.parameter(String.class, "si")));
        if (form.getGu() != null) criteria.add(cb.equal(i.get("gu"),
                cb.parameter(String.class, "gu")));
        if (form.getPrice() != null) criteria.add(cb.lessThan(i.get("price"),
                cb.parameter(Integer.class, "price")));
        /*if (form.getDog() != null || Boolean.TRUE.equals(form.getDog())) criteria.add(cb.equal(i.get("dog"),*/
        if (Boolean.TRUE.equals(form.getDog())) criteria.add(cb.equal(i.get("dog"),
                cb.parameter(Boolean.class, "dog")));
        if (Boolean.TRUE.equals(form.getCat())) criteria.add(cb.equal(i.get("cat"),
                cb.parameter(Boolean.class, "cat")));

        javax.persistence.criteria.Order dateAsc = cb.asc(i.get("startDate"));

        cq.where(cb.and(criteria.toArray(new Predicate[0])))
                .orderBy(dateAsc);

        TypedQuery<Item> query = em.createQuery(cq);

        if (form.getSi() != null) query.setParameter("si", form.getSi());
        if (form.getGu() != null) query.setParameter("gu", form.getGu());
        if (form.getPrice() != null) query.setParameter("price", form.getPrice());
        if (Boolean.TRUE.equals(form.getDog())) query.setParameter("dog", form.getDog());
        if (Boolean.TRUE.equals(form.getCat())) query.setParameter("cat", form.getCat());

        List<Item> items = query.getResultList();

        return items;

    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }
}
