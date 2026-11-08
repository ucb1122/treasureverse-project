package com.treasureverse.backend.auth.domain;

public record TokenResult(String accessToken, String refreshToken, long accessTokenExpiresIn) {}
