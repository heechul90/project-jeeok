package com.jeeok.jeeokmember.core.service;

import com.jeeok.jeeokmember.common.exception.EntityNotFound;
import com.jeeok.jeeokmember.core.domain.Member;
import com.jeeok.jeeokmember.core.dto.MemberSearchCondition;
import com.jeeok.jeeokmember.core.dto.SaveMemberParam;
import com.jeeok.jeeokmember.core.dto.UpdateMemberParam;
import com.jeeok.jeeokmember.core.repository.MemberQueryRepository;
import com.jeeok.jeeokmember.core.repository.MemberRepository;
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

    public static final String MEMBER = "Member";

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
                .orElseThrow(() -> new EntityNotFound(MEMBER, memberId.toString()));
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
                .build();
        return memberRepository.save(member);
    }

    /**
     * 멤버 수정
     */
    @Transactional
    public void updateMember(Long memberId, UpdateMemberParam param) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFound(MEMBER, memberId.toString()));
        findMember.updateMember(param);
    }

    /**
     * 멤버 삭제
     */
    @Transactional
    public void deleteMember(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFound(MEMBER, memberId.toString()));
        memberRepository.delete(findMember);
    }
}
