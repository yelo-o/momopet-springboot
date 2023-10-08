package com.momo.repository;

import com.momo.domain.Item;
import com.momo.domain.Order;
import com.momo.domain.OrderItem;
import com.momo.domain.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order, OrderItem orderItem) {
        if (order.getId() == null) { //저장된 것이 없으면 새것을 호출
            em.persist(order); //persist로 등록
            em.persist(orderItem);
        } else {
            em.merge(order);
        }
    }

    public List<Order> find(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);

        Root<Order> o = cq.from(Order.class);

        Predicate emailEqual = cb.equal(o.get("user").get("email"), email);

        javax.persistence.criteria.Order dateDesc = cb.desc(o.get("startDate"));

        cq.select(o)
                .where(emailEqual)
                .orderBy(dateDesc);

        List<Order> orders = em.createQuery(cq).getResultList();

        return orders;
    }

    public List<Order> findSittingOrders(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);

        Root<Order> o = cq.from(Order.class);

        // 조인을 통해 OrderItems와 연관된 엔티티에 접근
        Join<Order, OrderItem> orderItemJoin = o.join("orderItems", JoinType.INNER);

        Predicate emailEqual = cb.equal(orderItemJoin.get("item").get("sitter").get("email"), email);

        javax.persistence.criteria.Order dateDesc = cb.desc(o.get("orderDate"));

        cq.select(o)
                .where(emailEqual)
                .orderBy(dateDesc);

        List<Order> orders = em.createQuery(cq).getResultList();

        return orders;
    }
}
