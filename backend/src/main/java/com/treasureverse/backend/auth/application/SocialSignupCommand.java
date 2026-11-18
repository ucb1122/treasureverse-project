package com.treasureverse.backend.auth.application;

public record SocialSignupCommand(
    String ticketId,
    String name,
    String phoneNumber,
    String postalCode,
    String address,
    String addressDetail) {}
