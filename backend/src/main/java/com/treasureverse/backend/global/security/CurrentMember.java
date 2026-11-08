package com.treasureverse.backend.global.security;

import com.treasureverse.backend.member.domain.Role;

public record CurrentMember(Long id, String username, Role role) {}
