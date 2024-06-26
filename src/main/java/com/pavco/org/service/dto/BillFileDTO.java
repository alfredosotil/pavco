package com.pavco.org.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.pavco.org.domain.BillFile} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillFileDTO implements Serializable {

    private Long id;

    private UUID uuid;

    @NotNull
    @Size(max = 128)
    private String name;

    @NotNull
    private Long size;

    @NotNull
    @Pattern(regexp = "^[\\w]+\\/[\\w]+$")
    private String mimeType;

    @Lob
    private byte[] content;

    private String contentContentType;

    private Boolean isProcessed;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return contentContentType;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public Boolean getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(Boolean isProcessed) {
        this.isProcessed = isProcessed;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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
        if (!(o instanceof BillFileDTO)) {
            return false;
        }

        BillFileDTO billFileDTO = (BillFileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, billFileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillFileDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", name='" + getName() + "'" +
            ", size=" + getSize() +
            ", mimeType='" + getMimeType() + "'" +
            ", content='" + getContent() + "'" +
            ", isProcessed='" + getIsProcessed() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", client=" + getClient() +
            "}";
    }
}
