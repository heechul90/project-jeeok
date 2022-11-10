package com.jeeok.jeeokmember.core.member.controller;

import com.jeeok.jeeokmember.common.json.JsonResult;
import com.jeeok.jeeokmember.core.member.controller.request.SaveMemberRequest;
import com.jeeok.jeeokmember.core.member.controller.request.UpdateMemberRequest;
import com.jeeok.jeeokmember.core.member.controller.response.SaveMemberResponse;
import com.jeeok.jeeokmember.core.member.controller.response.UpdateMemberResponse;
import com.jeeok.jeeokmember.core.member.domain.Member;
import com.jeeok.jeeokmember.core.member.dto.MemberDto;
import com.jeeok.jeeokmember.core.member.dto.MemberSearchCondition;
import com.jeeok.jeeokmember.core.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/members")
public class AdminMemberController {

    private final MemberService memberService;

    /**
     * 멤버 목로 조회
     */
    @GetMapping
    public JsonResult findMembers(MemberSearchCondition condition, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<Member> content = memberService.findMembers(condition, pageable);
        List<Object> members = content.stream()
                .map(MemberDto::new)
                .collect(Collectors.toList());

        return JsonResult.OK(members);
    }

    /**
     * 멤버 단건 조회
     */
    @GetMapping("/{memberId}")
    public JsonResult findMember(@PathVariable("memberId") Long memberId) {
        Member findMember = memberService.findMember(memberId);
        MemberDto member = new MemberDto(findMember);
        return JsonResult.OK(member);
    }

    /**
     * 멤버 저장
     */
    @PostMapping
    public JsonResult saveMember(@RequestBody @Validated SaveMemberRequest request) {

        //validate
        request.validate();

        Member savedMember = memberService.saveMember(request.toParam());

        return JsonResult.OK(new SaveMemberResponse(savedMember.getId()));
    }

    /**
     * 멤버 수정
     */
    @PutMapping("/{memberId}")
    public JsonResult updateMember(@PathVariable("memberId") Long memberId,
                                   @RequestBody @Validated UpdateMemberRequest request) {

        //validate
        request.validate();

        memberService.updateMember(memberId, request.toParam());
        Member updatedMember = memberService.findMember(memberId);

        return JsonResult.OK(new UpdateMemberResponse(updatedMember.getId()));
    }

    /**
     * 멤버 삭제
     */
    @DeleteMapping("/{memberId}")
    public JsonResult deleteMember(@PathVariable("memberId") Long memberId) {

        memberService.deleteMember(memberId);

        return JsonResult.OK();
    }
}
