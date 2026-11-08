package com.treasureverse.backend.member.domain;

import com.treasureverse.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(
    name = "social_accounts",
    uniqueConstraints =
        @UniqueConstraint(
            name = "uk_social_provider_user",
            columnNames = {"provider", "provider_user_id"}))
public class SocialAccount extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id")
  private Member member;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SocialProvider provider;

  @Column(name = "provider_user_id", nullable = false)
  private String providerUserId;

  protected SocialAccount() {}
}
