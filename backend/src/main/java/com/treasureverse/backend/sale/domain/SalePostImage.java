package com.treasureverse.backend.sale.domain;

import com.treasureverse.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "sale_post_images")
public class SalePostImage extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sale_post_id")
  private SalePost salePost;

  @Column(name = "object_key", nullable = false)
  private String objectKey;

  @Column(name = "sort_order", nullable = false)
  private int sortOrder;

  protected SalePostImage() {}

  public SalePostImage(SalePost p, String k, int o) {
    salePost = p;
    objectKey = k;
    sortOrder = o;
  }

  public String getObjectKey() {
    return objectKey;
  }
}
