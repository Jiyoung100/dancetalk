package com.mysite.sbb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카테고리명
    @Column(nullable = false, unique = true, length = 30)
    private String title;

    // 카테고리 소개 문구
    @Column(length = 100)
    private String description;
}