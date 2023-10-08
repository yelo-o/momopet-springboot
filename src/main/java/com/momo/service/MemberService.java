package com.momo.service;

import com.momo.domain.member.*;
import com.momo.domain.user.User;
import com.momo.repository.MemberRepository;
import com.momo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

//    public Long join(Member member) {
//        validateDuplicateMember(member); //중복 회원 검증
//        memberRepository.save(member);
//        return member.getId();
//    }

    //중복 아이디 검증 메소드
//    private void validateDuplicateMember(User user) {
//        List<Member> findMembers = memberRepository.findById(user.getEmail());
//        if (!findMembers.isEmpty()) {
//            throw new IllegalStateException("이미 존재하는 회원입니다.");
//        }
//    }

    //회원 전체 조회
//    public List<Member> findMembers() {
//        return memberRepository.findAll();
//    }

    public User findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    public User findOne(String email) {
        return memberRepository.findByEmail(email);
    }

    //회원 정보 업데이트
    public void updateUser(String email, PrivateInformation privateInformation) {
        User findUser = memberRepository.findByEmail(email);
        findUser.update(privateInformation);
    }

    public void updateUserInfo(String email, PrivateInformation privateInformation) {
        User findUser = memberRepository.findByEmail(email);
        findUser.updateInfo(privateInformation);
//        memberRepository.save(findUser);
    }

    public void updatePet(String name, Gender gender, PetType petType, String breed,
                          LocalDate birthDate, String remark, Pet pet, String photo) {

        pet.update(name, gender, petType, breed, birthDate, remark, photo);
    }

    //펫 정보 불러오기
    public Pet findPet(Long id) {
        try {
            Pet pet = memberRepository.findPet(id);
            return pet;
        } catch (NoResultException e) {
            return null;
        }
    }

    //펫 추가
    public void add(Pet pet) {
        memberRepository.save(pet);
//        findUser.upgrade(); //SITTER => OWNER 업그레이드
    }

    // 충전 금액 증가
    public void increasePoint(int point, User user) {
        user.pointUp(point);
    }

}
