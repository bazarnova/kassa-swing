package com.kassa.entity;

import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private Long id;
    private String productName;
    private Integer amount;
    private Integer account;
    private BigDecimal weight;
    private Long checkId;
    private String comment;
}
