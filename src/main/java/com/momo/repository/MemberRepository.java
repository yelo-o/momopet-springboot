package com.momo.repository;

import com.momo.domain.member.Pet;
import com.momo.domain.member.Point;
import com.momo.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository{

    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public void save(Pet pet) {
        em.persist(pet);
    }

    public void save(Point point) { em.persist(point);
    }

    public User findOne(Long id) { //단건 조회
        return em.find(User.class, id);
    }

    public User findByEmail(String email) { //단건 조회
        return em.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

//    public List<Member> findAll() { //모든 멤버 조회
//        return em.createQuery("select m from Member m", Member.class) //JPQL : 엔티티 객체를 대상으로 쿼리 진행
//                .getResultList();
//    }

//    public List<Member> findById(String loginId) { //아이디로 리스트 조회
//        return em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
//                .setParameter("loginId", loginId)
//                .getResultList();
//    }

    public Pet findPet(Long id) throws NoResultException { //로그인한 아이디와 같은 펫 조회
        return em.createQuery("select p from Pet p where p.owner.id = :id", Pet.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    //잔액 조회
    public Long getBalance(Long id) {
        Query query = em.createQuery("select sum(p.amount) from Point p where p.user.id = :id");
        query.setParameter("id", id);
        if (query.getSingleResult() != null) {
            return (Long) query.getSingleResult();
        } else {
            return 0L;
        }
    }


}
