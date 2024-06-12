package com.pavco.org.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.pavco.org.domain.ProductType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductTypeDTO implements Serializable {

    private Long id;

    private UUID uuid;

    @NotNull
    @Size(min = 10, max = 12)
    @Pattern(regexp = "^[0-9]+")
    private String code;

    @NotNull
    @Size(min = 4, max = 64)
    private String name;

    @NotNull
    private Double price;

    @NotNull
    private Double discount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductTypeDTO)) {
            return false;
        }

        ProductTypeDTO productTypeDTO = (ProductTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductTypeDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", discount=" + getDiscount() +
            "}";
    }
}
