package com.pavco.org.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pavco.org.domain.enumeration.TaxPayerType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid", unique = true)
    private UUID uuid;

    @NotNull
    @Size(max = 30)
    @Column(name = "ruc", length = 30, nullable = false, unique = true)
    private String ruc;

    @NotNull
    @Size(max = 100)
    @Column(name = "business_name", length = 100, nullable = false)
    private String businessName;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tax_payer_type", nullable = false)
    private TaxPayerType taxPayerType;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    @JsonIgnoreProperties(value = { "client" }, allowSetters = true)
    private Set<BillFile> billFiles = new HashSet<>();

    @JsonIgnoreProperties(value = { "product", "client" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "client")
    private Equivalent equivalent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Client uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getRuc() {
        return this.ruc;
    }

    public Client ruc(String ruc) {
        this.setRuc(ruc);
        return this;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getBusinessName() {
        return this.businessName;
    }

    public Client businessName(String businessName) {
        this.setBusinessName(businessName);
        return this;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getDescription() {
        return this.description;
    }

    public Client description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaxPayerType getTaxPayerType() {
        return this.taxPayerType;
    }

    public Client taxPayerType(TaxPayerType taxPayerType) {
        this.setTaxPayerType(taxPayerType);
        return this;
    }

    public void setTaxPayerType(TaxPayerType taxPayerType) {
        this.taxPayerType = taxPayerType;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public Client logo(byte[] logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public Client logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Client user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<BillFile> getBillFiles() {
        return this.billFiles;
    }

    public void setBillFiles(Set<BillFile> billFiles) {
        if (this.billFiles != null) {
            this.billFiles.forEach(i -> i.setClient(null));
        }
        if (billFiles != null) {
            billFiles.forEach(i -> i.setClient(this));
        }
        this.billFiles = billFiles;
    }

    public Client billFiles(Set<BillFile> billFiles) {
        this.setBillFiles(billFiles);
        return this;
    }

    public Client addBillFile(BillFile billFile) {
        this.billFiles.add(billFile);
        billFile.setClient(this);
        return this;
    }

    public Client removeBillFile(BillFile billFile) {
        this.billFiles.remove(billFile);
        billFile.setClient(null);
        return this;
    }

    public Equivalent getEquivalent() {
        return this.equivalent;
    }

    public void setEquivalent(Equivalent equivalent) {
        if (this.equivalent != null) {
            this.equivalent.setClient(null);
        }
        if (equivalent != null) {
            equivalent.setClient(this);
        }
        this.equivalent = equivalent;
    }

    public Client equivalent(Equivalent equivalent) {
        this.setEquivalent(equivalent);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return getId() != null && getId().equals(((Client) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", ruc='" + getRuc() + "'" +
            ", businessName='" + getBusinessName() + "'" +
            ", description='" + getDescription() + "'" +
            ", taxPayerType='" + getTaxPayerType() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            "}";
    }
}
