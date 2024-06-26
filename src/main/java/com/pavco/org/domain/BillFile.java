package com.pavco.org.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Persistable;

/**
 * A BillFile.
 */
@Entity
@Table(name = "bill_file")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BillFile extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid", unique = true)
    private UUID uuid;

    @NotNull
    @Size(max = 128)
    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @NotNull
    @Column(name = "size", nullable = false)
    private Long size;

    @NotNull
    @Pattern(regexp = "^[\\w]+\\/[\\w]+$")
    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    @Lob
    @Column(name = "content", nullable = false)
    private byte[] content;

    @NotNull
    @Column(name = "content_content_type", nullable = false)
    private String contentContentType;

    @Column(name = "is_processed")
    private Boolean isProcessed;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @Transient
    private boolean isPersisted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "billFiles", "equivalent" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BillFile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public BillFile uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public BillFile name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return this.size;
    }

    public BillFile size(Long size) {
        this.setSize(size);
        return this;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public BillFile mimeType(String mimeType) {
        this.setMimeType(mimeType);
        return this;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getContent() {
        return this.content;
    }

    public BillFile content(byte[] content) {
        this.setContent(content);
        return this;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return this.contentContentType;
    }

    public BillFile contentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
        return this;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public Boolean getIsProcessed() {
        return this.isProcessed;
    }

    public BillFile isProcessed(Boolean isProcessed) {
        this.setIsProcessed(isProcessed);
        return this;
    }

    public void setIsProcessed(Boolean isProcessed) {
        this.isProcessed = isProcessed;
    }

    // Inherited createdBy methods
    public BillFile createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public BillFile createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public BillFile lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public BillFile lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public BillFile setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public BillFile client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillFile)) {
            return false;
        }
        return getId() != null && getId().equals(((BillFile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillFile{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", name='" + getName() + "'" +
            ", size=" + getSize() +
            ", mimeType='" + getMimeType() + "'" +
            ", content='" + getContent() + "'" +
            ", contentContentType='" + getContentContentType() + "'" +
            ", isProcessed='" + getIsProcessed() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
