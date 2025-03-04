package com.elice.sdz.product.entity;

import com.elice.sdz.category.entity.Category;
import com.elice.sdz.image.entity.Image;
import com.elice.sdz.inquiry.entity.Inquiry;
import com.elice.sdz.review.entity.Review;
import com.elice.sdz.user.entity.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category categoryId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users userId;

    @Column(name = "product_name", length = 50, nullable = false)
    private String productName;

    @Column(name = "product_count", nullable = false)
    private int productCount;

    @Column(name = "product_amount", nullable = false)
    private Double productAmount;

    @Column(name = "product_content", length = 3000, nullable = false)
    private String productContent;

    @Column(name = "reg_date", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant regDate;

    @OneToMany(mappedBy = "productId", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private final List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "productId", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private final List<Review> reviews  = new ArrayList<>();

    @OneToMany(mappedBy = "productId", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private final List<Inquiry> inquiries = new ArrayList<>();
}
