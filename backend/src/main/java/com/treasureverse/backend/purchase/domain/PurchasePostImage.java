package com.treasureverse.backend.purchase.domain;

import com.treasureverse.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "purchase_post_images")
public class PurchasePostImage extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "purchase_post_id")
  PurchasePost post;

  @Column(name = "object_key", nullable = false)
  String objectKey;

  @Column(name = "sort_order", nullable = false)
  int sortOrder;

  protected PurchasePostImage() {}

  public PurchasePostImage(PurchasePost p, String k, int s) {
    post = p;
    objectKey = k;
    sortOrder = s;
  }
}
