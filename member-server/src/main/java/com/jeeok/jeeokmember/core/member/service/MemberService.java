package com.jeeok.jeeokmember.core.member.service;

import com.jeeok.jeeokmember.core.member.domain.Member;
import com.jeeok.jeeokmember.core.member.dto.MemberSearchCondition;
import com.jeeok.jeeokmember.core.member.dto.SaveMemberParam;
import com.jeeok.jeeokmember.core.member.dto.UpdateMemberParam;
import com.jeeok.jeeokmember.core.member.exception.MemberNotFound;
import com.jeeok.jeeokmember.core.member.repository.MemberQueryRepository;
import com.jeeok.jeeokmember.core.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberQueryRepository memberQueryRepository;
    private final MemberRepository memberRepository;

    /**
     * 멤버 목록 조회
     */
    public Page<Member> findMembers(MemberSearchCondition condition, Pageable pageable) {
        return memberQueryRepository.findMembers(condition, pageable);
    }

    /**
     * 멤버 단건 조회
     */
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFound(memberId));
    }

    /**
     * 멤버 저장
     */
    @Transactional
    public Member saveMember(SaveMemberParam param) {
        Member member = Member.createMember()
                .email(param.getEmail())
                .password(param.getPassword())
                .name(param.getName())
                .roleType(param.getRoleType())
                .authType(param.getAuthType())
                .phoneNumber(param.getPhoneNumber())
                .address(param.getAddress())
                .build();
        return memberRepository.save(member);
    }

    /**
     * 멤버 수정
     */
    @Transactional
    public void updateMember(Long memberId, UpdateMemberParam param) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFound(memberId));
        findMember.updateMember(param);
    }

    /**
     * 멤버 삭제
     */
    @Transactional
    public void deleteMember(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFound(memberId));
        memberRepository.delete(findMember);
    }
}
