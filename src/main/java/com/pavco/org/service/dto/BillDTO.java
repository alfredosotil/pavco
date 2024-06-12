package com.pavco.org.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.pavco.org.domain.Bill} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillDTO implements Serializable {

    private Long id;

    private UUID uuid;

    @NotNull
    @Size(min = 10, max = 12)
    @Pattern(regexp = "^[0-9]+")
    private String code;

    @Size(max = 255)
    private String notes;

    @NotNull
    private Double total;

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
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
        if (!(o instanceof BillDTO)) {
            return false;
        }

        BillDTO billDTO = (BillDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, billDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", code='" + getCode() + "'" +
            ", notes='" + getNotes() + "'" +
            ", total=" + getTotal() +
            ", client=" + getClient() +
            "}";
    }
}
