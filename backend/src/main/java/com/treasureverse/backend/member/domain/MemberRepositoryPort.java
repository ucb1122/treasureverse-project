package com.treasureverse.backend.member.domain;

import java.util.Optional;

public interface MemberRepositoryPort {
  boolean existsByUsername(String username);

  Optional<Member> findByUsername(String username);

  Optional<Member> findById(Long id);

  Member save(Member member);
}
