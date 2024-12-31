package com.elice.sdz.category.entity;

import com.elice.sdz.category.dto.CategoryResponseDTO;
import com.elice.sdz.global.entity.BaseEntity;
import com.elice.sdz.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category extends BaseEntity {

    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(name = "category_name", length = 10, nullable = false)
    private String categoryName;

    @Column(name = "parent_id", nullable = true)
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL,
            orphanRemoval = false, fetch = FetchType.LAZY)
    private List<Category> subCategories = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL,
            orphanRemoval = false, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    public CategoryResponseDTO toResponseDTO() {
        List<CategoryResponseDTO> subCategoriesDTO = subCategories.stream()
                .map(Category::toResponseDTO)
                .collect(Collectors.toList());

        return CategoryResponseDTO.builder()
                .categoryId(categoryId)
                .categoryName(categoryName)
                .parentId(parentId)
                .subCategories(subCategoriesDTO)
                .build();
    }
}
