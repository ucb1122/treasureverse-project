package com.treasureverse.backend.member.infrastructure;

import com.treasureverse.backend.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
  boolean existsByUsername(String username);

  Optional<Member> findByUsername(String username);
}
