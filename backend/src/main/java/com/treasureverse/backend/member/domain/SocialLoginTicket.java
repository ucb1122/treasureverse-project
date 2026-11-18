package com.treasureverse.backend.member.domain;

public record SocialLoginTicket(
    SocialProvider provider, String providerUserId, String suggestedName) {}
