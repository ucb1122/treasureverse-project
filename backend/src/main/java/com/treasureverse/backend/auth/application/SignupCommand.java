package com.treasureverse.backend.auth.application;

public record SignupCommand(
    String username,
    String password,
    String name,
    String phoneNumber,
    String postalCode,
    String address,
    String addressDetail) {}
