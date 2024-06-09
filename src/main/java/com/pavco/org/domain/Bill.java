package com.pavco.org.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A Bill.
 */
@Entity
@Table(name = "bill")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Bill implements Serializable {

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

    @Size(max = 255)
    @Column(name = "notes", length = 255)
    private String notes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bill")
    @JsonIgnoreProperties(value = { "bill" }, allowSetters = true)
    private Set<BillDetail> billDetails = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "billFiles", "equivalent" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bill id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Bill uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getCode() {
        return this.code;
    }

    public Bill code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNotes() {
        return this.notes;
    }

    public Bill notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<BillDetail> getBillDetails() {
        return this.billDetails;
    }

    public void setBillDetails(Set<BillDetail> billDetails) {
        if (this.billDetails != null) {
            this.billDetails.forEach(i -> i.setBill(null));
        }
        if (billDetails != null) {
            billDetails.forEach(i -> i.setBill(this));
        }
        this.billDetails = billDetails;
    }

    public Bill billDetails(Set<BillDetail> billDetails) {
        this.setBillDetails(billDetails);
        return this;
    }

    public Bill addBillDetail(BillDetail billDetail) {
        this.billDetails.add(billDetail);
        billDetail.setBill(this);
        return this;
    }

    public Bill removeBillDetail(BillDetail billDetail) {
        this.billDetails.remove(billDetail);
        billDetail.setBill(null);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Bill client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bill)) {
            return false;
        }
        return getId() != null && getId().equals(((Bill) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bill{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", code='" + getCode() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
