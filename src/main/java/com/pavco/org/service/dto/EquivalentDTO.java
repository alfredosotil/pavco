package com.pavco.org.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.pavco.org.domain.Equivalent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EquivalentDTO implements Serializable {

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

    private ProductDTO product;

    private ClientDTO client;

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

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquivalentDTO)) {
            return false;
        }

        EquivalentDTO equivalentDTO = (EquivalentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, equivalentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquivalentDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", discount=" + getDiscount() +
            ", product=" + getProduct() +
            ", client=" + getClient() +
            "}";
    }
}
