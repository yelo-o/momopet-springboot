package com.momo.service;

import com.momo.domain.member.Address;
import com.momo.domain.member.Member;
import com.momo.domain.member.PrivateInformation;
import com.momo.domain.user.User;
import com.momo.repository.MemberRepository;
import com.momo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

//    public Long join(Member member) {
//        validateDuplicateMember(member); //중복 회원 검증
//        memberRepository.save(member);
//        return member.getId();
//    }

    //중복 아이디 검증 메소드
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findById(member.getEmail());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

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
        memberRepository.save(findUser);
    }

}
