package com.treasureverse.backend.member.presentation;

import com.treasureverse.backend.member.domain.Member;
import com.treasureverse.backend.member.domain.Role;

public record MemberProfileResponse(
    Long id,
    String username,
    String name,
    String phoneNumber,
    String postalCode,
    String address,
    String addressDetail,
    Role role) {

  public static MemberProfileResponse from(Member member) {
    return new MemberProfileResponse(
        member.getId(),
        member.getUsername(),
        member.getName(),
        member.getPhoneNumber(),
        member.getPostalCode(),
        member.getAddress(),
        member.getAddressDetail(),
        member.getRole());
  }
}
