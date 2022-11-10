package com.jeeok.jeeokmember.core.member.controller;

import com.jeeok.jeeokmember.common.json.JsonResult;
import com.jeeok.jeeokmember.core.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/front/members")
public class FrontMemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public JsonResult myInfo(@PathVariable("memberId") Long memberId) {

    }
}
