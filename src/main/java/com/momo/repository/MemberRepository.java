package com.momo.repository;

import com.momo.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) { //단건 조회
        return em.find(Member.class, id);
    }

    public List<Member> findAll() { //모든 멤버 조회
        return em.createQuery("select m from Member m", Member.class) //JPQL : 엔티티 객체를 대상으로 쿼리 진행
                .getResultList();
    }

    public List<Member> findById(String loginId) { //아이디로 리스트 조회
        return em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList();
    }

}
