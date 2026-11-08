package com.treasureverse.backend.member.infrastructure;

import com.treasureverse.backend.member.domain.Member;
import com.treasureverse.backend.member.domain.MemberRepositoryPort;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryAdapter implements MemberRepositoryPort {
  private final MemberJpaRepository repository;

  public MemberRepositoryAdapter(MemberJpaRepository repository) {
    this.repository = repository;
  }

  public boolean existsByUsername(String username) {
    return repository.existsByUsername(username);
  }

  public Optional<Member> findByUsername(String username) {
    return repository.findByUsername(username);
  }

  public Optional<Member> findById(Long id) {
    return repository.findById(id);
  }

  public Member save(Member member) {
    return repository.save(member);
  }
}
