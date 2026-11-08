package com.treasureverse.backend.member.presentation;

import com.treasureverse.backend.common.response.ApiResponse;
import com.treasureverse.backend.common.security.CurrentMember;
import com.treasureverse.backend.member.application.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {
  private final MemberService memberService;

  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  @GetMapping("/me")
  @Operation(summary = "내 회원 정보 조회")
  public ApiResponse<MemberProfileResponse> getMyProfile(
      @AuthenticationPrincipal CurrentMember currentMember) {
    return ApiResponse.success(
        MemberProfileResponse.from(memberService.getMyProfile(currentMember.id())));
  }

  @PatchMapping("/me")
  @Operation(summary = "내 회원 정보 수정")
  public ApiResponse<MemberProfileResponse> updateMyProfile(
      @AuthenticationPrincipal CurrentMember currentMember,
      @Valid @RequestBody UpdateMemberProfileRequest request) {
    return ApiResponse.success(
        MemberProfileResponse.from(
            memberService.updateMyProfile(
                currentMember.id(),
                request.name(),
                request.phoneNumber(),
                request.postalCode(),
                request.address(),
                request.addressDetail())));
  }
}
