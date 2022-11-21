package com.jeeok.jeeoklog.client.member;

import com.jeeok.jeeoklog.common.json.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("MEMBER-SERVER")
public interface MemberClient {

    @GetMapping("/admin/members/{memberId}")
    JsonResult<FindMemberResponse> findMember(@PathVariable("memberId") Long memberId);
}
