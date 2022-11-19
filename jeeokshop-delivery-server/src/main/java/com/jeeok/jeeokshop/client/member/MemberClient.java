package com.jeeok.jeeokshop.client.member;

import com.jeeok.jeeokshop.common.json.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.ws.rs.Path;

@FeignClient("MEMBER-SERVER")
public interface MemberClient {

    @GetMapping("/admin/members/{memberId}")
    JsonResult<FindMemberResponse> findMember(@PathVariable("memberId") Long memberId);
}
