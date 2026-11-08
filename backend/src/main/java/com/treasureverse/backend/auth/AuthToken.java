package com.treasureverse.backend.auth;

public record AuthToken(String accessToken, String refreshToken, long accessTokenExpiresIn) {}
