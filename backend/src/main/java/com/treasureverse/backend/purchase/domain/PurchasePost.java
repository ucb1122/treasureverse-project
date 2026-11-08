package com.treasureverse.backend.purchase.domain;

import com.treasureverse.backend.common.entity.BaseTimeEntity;
import com.treasureverse.backend.member.domain.Member;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "purchase_posts")
public class PurchasePost extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "buyer_id")
  Member buyer;

  @Column(nullable = false)
  String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  String content;

  @Column(nullable = false)
  BigDecimal desiredPrice;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  PurchaseStatus status = PurchaseStatus.OPEN;

  protected PurchasePost() {}

  public PurchasePost(Member b, String t, String c, BigDecimal p) {
    buyer = b;
    title = t;
    content = c;
    desiredPrice = p;
  }

  public Long id() {
    return id;
  }

  public Long buyerId() {
    return buyer.getId();
  }

  public String title() {
    return title;
  }

  public BigDecimal price() {
    return desiredPrice;
  }

  public PurchaseStatus status() {
    return status;
  }

  public void update(String t, String c, BigDecimal p, PurchaseStatus s) {
    title = t;
    content = c;
    desiredPrice = p;
    status = s;
  }
}
