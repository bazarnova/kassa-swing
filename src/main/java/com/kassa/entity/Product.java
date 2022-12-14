package com.kassa.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private Long id;
    private String productName;
    private Integer amount;
    private Integer account;
    private BigDecimal weight;
    private Long check_id;
    private String comment;

    public Product(Builder builder) {
        this.id = builder.id;
        this.productName = builder.productName;
        this.amount = builder.amount;
        this.account = builder.account;
        this.weight = builder.weight;
        this.check_id = builder.check_id;
        this.comment = builder.comment;
    }

    public Product() {
    }

    public Product(Long id, String productName, Integer amount, Integer account, BigDecimal weight, Long check_id, String comment) {
        this.id = id;
        this.productName = productName;
        this.amount = amount;
        this.account = account;
        this.weight = weight;
        this.check_id = check_id;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Long getCheck_id() {
        return check_id;
    }

    public void setCheck_id(Long check_id) {
        this.check_id = check_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static class Builder {
        Long id;
        String productName;
        Integer amount;
        Integer account;
        BigDecimal weight;
        Long check_id;
        String comment;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder setAmount(Integer amount) {
            this.amount = amount;
            return this;
        }

        public Builder setAccount(Integer account) {
            this.account = account;
            return this;
        }

        public Builder setWeight(BigDecimal weight) {
            this.weight = weight;
            return this;
        }

        public Builder setCheck_id(Long check_id) {
            this.check_id = check_id;
            return this;
        }

        public Builder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productName, product.productName) &&
                Objects.equals(amount, product.amount) &&
                Objects.equals(account, product.account) &&
                Objects.equals(weight, product.weight) &&
                Objects.equals(check_id, product.check_id) &&
                Objects.equals(comment, product.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, amount, account, weight, check_id, comment);
    }
}
