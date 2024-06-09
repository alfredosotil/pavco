package com.pavco.org.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * A ProductType.
 */
@Entity
@Table(name = "product_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid", unique = true)
    private UUID uuid;

    @NotNull
    @Size(max = 30)
    @Column(name = "code", length = 30, nullable = false, unique = true)
    private String code;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "discount", nullable = false)
    private Double discount;

    @JsonIgnoreProperties(value = { "productType", "equivalent" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "productType")
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public ProductType uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getCode() {
        return this.code;
    }

    public ProductType code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getPrice() {
        return this.price;
    }

    public ProductType price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return this.discount;
    }

    public ProductType discount(Double discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        if (this.product != null) {
            this.product.setProductType(null);
        }
        if (product != null) {
            product.setProductType(this);
        }
        this.product = product;
    }

    public ProductType product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductType)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductType{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", code='" + getCode() + "'" +
            ", price=" + getPrice() +
            ", discount=" + getDiscount() +
            "}";
    }
}
