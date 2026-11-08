package com.treasureverse.backend.auth.presentation;

public record AuthToken(String accessToken, String refreshToken, long accessTokenExpiresIn) {}
