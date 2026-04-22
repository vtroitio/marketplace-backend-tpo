package com.uade.tpo.grupo7.marketplace.products.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "attribute_id", nullable = false)
    @JsonIgnore
    private Attribute attribute;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false, unique = true)
    private String code;

    @Column
    private String hexColor;

    @OneToMany(mappedBy = "attributeValue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VariantAttributeValue> variantAttributeValues;
}
