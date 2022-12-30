package com.kassa.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Check {
    private Long id;
    private Double sumAmount;
    private LocalDate date;
    private String shopName;
    private String comment;
}
