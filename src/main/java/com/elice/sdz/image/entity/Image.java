package com.elice.sdz.image.entity;

import com.elice.sdz.global.entity.BaseEntity;
import com.elice.sdz.inquiry.entity.Inquiry;
import com.elice.sdz.product.entity.Product;
import com.elice.sdz.review.entity.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image")
public class Image extends BaseEntity {

    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "inquiry_id")
    private Inquiry inquiry;

    @Column(name = "image_origin", nullable = false)
    private String imageOrigin;

    @Column(name = "image_uuid", nullable = false)
    private String imageUuid;

    @Column(name = "upload_path")
    private String uploadPath;

    @Column(name = "thumbnail_path")
    private String thumbnailPath;
}
