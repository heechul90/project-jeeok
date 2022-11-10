package com.jeeok.jeeokmember.core.member.controller;

import com.jeeok.jeeokmember.common.json.JsonResult;
import com.jeeok.jeeokmember.core.member.controller.request.MemberEditRequest;
import com.jeeok.jeeokmember.core.member.controller.response.MemberEditResponse;
import com.jeeok.jeeokmember.core.member.controller.response.MemberInfoResponse;
import com.jeeok.jeeokmember.core.member.domain.Member;
import com.jeeok.jeeokmember.core.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/front/member")
public class FrontMemberController {

    private final MemberService memberService;

    /**
     * 내 정보 조회
     */
    @GetMapping("/info")
    public JsonResult info(@RequestHeader(value = "member-id") @Validated String memberId) {

        Member findMember = memberService.findMember(Long.parseLong(memberId));
        MemberInfoResponse memberInfo = new MemberInfoResponse(findMember);
        return JsonResult.OK(memberInfo);
    }

    /**
     * 내 정보 수정
     */
    @PutMapping("/{memberId}/edit")
    public JsonResult edit(@PathVariable("memberId") Long memberId,
                           @RequestBody @Validated MemberEditRequest request) {

        //validate
        request.validate();

        memberService.updateMember(memberId, request.toParam());
        Member editMember = memberService.findMember(memberId);

        return JsonResult.OK(new MemberEditResponse(editMember.getId()));
    }
}
