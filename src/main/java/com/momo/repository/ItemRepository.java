package com.momo.repository;

import com.momo.controller.ItemForm;
import com.momo.domain.Item;
import com.momo.domain.Status;
import com.momo.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
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

    public List<Item> findMyItems(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Item> cq = cb.createQuery(Item.class);

        Root<Item> i = cq.from(Item.class);

        Predicate emailEqual = cb.equal(i.get("sitter").get("email"), email);
        Predicate statusEqual = cb.equal(i.get("status"), Status.활성화);

        javax.persistence.criteria.Order dateAsc = cb.asc(i.get("startDate"));

        cq.select(i)
                .where(emailEqual, statusEqual)
                .orderBy(dateAsc);

        List<Item> items = em.createQuery(cq).getResultList();

        return items;
    }

    public List<Item> searchItems(ItemForm form, String email) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        //Criteria 쿼리를 생성하기 위한 Criteria 빌더
        CriteriaBuilder cb = em.getCriteriaBuilder();
        //Criteria 생성, 반환 타입 지정
        CriteriaQuery<Item> cq = cb.createQuery(Item.class);

        //FROM 절 생성. 반환된 값 i는 Criteria에서 사용하는 별칭이다. i를 조회의 시작점이라는 의미로 쿼리 루트(Root)라 한다.
        Root<Item> i = cq.from(Item.class);
/*        Join<Item, User> u = i.join("sitter");*/

        //검색 조건 리스트 생성
        List<Predicate> criteria = new ArrayList<Predicate>();

        //검색 조건 리스트에 하나씩 추가
        criteria.add(cb.equal(i.get("status"), Status.활성화)); //삭제되지 않은 아이템만 조회
        criteria.add(cb.notEqual(i.get("sitter").get("email"), email)); //본인이 올린 아이템은 조회 리스트에서 제외

        if (form.getSi() != null)
            criteria.add(cb.like(i.get("sitter").get("privateInformation").get("address").get("si"),
                cb.parameter(String.class, "si")));
        if (form.getGu() != null)
            criteria.add(cb.like(i.get("sitter").get("privateInformation").get("address").get("gu"),
                    cb.parameter(String.class, "gu")));
        if (form.getPrice() != null) criteria.add(cb.lessThanOrEqualTo(i.get("price"),
                cb.parameter(Integer.class, "price")));
        /*if (form.getDog() != null || Boolean.TRUE.equals(form.getDog())) criteria.add(cb.equal(i.get("dog"),*/
        if (Boolean.TRUE.equals(form.getDog())) criteria.add(cb.equal(i.get("dog"),
                cb.parameter(Boolean.class, "dog")));
        if (Boolean.TRUE.equals(form.getCat())) criteria.add(cb.equal(i.get("cat"),
                cb.parameter(Boolean.class, "cat")));
        if (form.getStartDate() != null) criteria.add(cb.lessThanOrEqualTo(i.get("startDate"),
                cb.parameter(LocalDateTime.class, "startDate")));
        if (form.getEndDate() != null) criteria.add(cb.greaterThanOrEqualTo(i.get("endDate"),
                cb.parameter(LocalDateTime.class, "endDate")));

        //정렬 조건 정의
        javax.persistence.criteria.Order dateAsc = cb.asc(i.get("startDate"));

        //쿼리 생성
        cq.where(cb.and(criteria.toArray(new Predicate[0])))
                .orderBy(dateAsc);

        TypedQuery<Item> query = em.createQuery(cq);
        if (form.getSi() != null) query.setParameter("si", form.getSi() + "%");
        if (form.getGu() != null) query.setParameter("gu", form.getGu() + "%");
        if (form.getPrice() != null) query.setParameter("price", form.getPrice());
        if (Boolean.TRUE.equals(form.getDog())) query.setParameter("dog", form.getDog());
        if (Boolean.TRUE.equals(form.getCat())) query.setParameter("cat", form.getCat());
        if (form.getStartDate() != null) query.setParameter("startDate",
                LocalDateTime.parse(form.getStartDate(), formatter));
        if (form.getEndDate() != null) query.setParameter("endDate",
                LocalDateTime.parse(form.getEndDate(), formatter));

        List<Item> items = query.getResultList();

        return items;

    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }
}
