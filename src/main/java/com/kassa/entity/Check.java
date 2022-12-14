package com.kassa.entity;

import java.time.LocalDate;

public class Check {
    private Long id;
    private Double sumAmount;
    private LocalDate date;
    private String shopName;
    private String comment;

    public Check(Builder builder) {
        this.id = builder.id;
        this.sumAmount = builder.sumAmount;
        this.date = builder.date;
        this.shopName = builder.shopName;
        this.comment = builder.comment;
    }

    public Check() {
    }

    public Check(Long id, Double sumAmount, LocalDate date, String shopName, String comment) {
        this.id = id;
        this.sumAmount = sumAmount;
        this.date = date;
        this.shopName = shopName;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(Double sumAmount) {
        this.sumAmount = sumAmount;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public static class Builder {
        Long id;
        Double sumAmount;
        LocalDate date;
        String shopName;
        String comment;

        public Builder setId(Long idd) {
            this.id = idd;
            return this;
        }

        public Builder setSumAmount(Double sumAmount) {
            this.sumAmount = sumAmount;
            return this;
        }

        public Builder setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder setShopName(String shopName) {
            this.shopName = shopName;
            return this;
        }

        public Builder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Check build() {
            return new Check(this);
        }
    }
}
