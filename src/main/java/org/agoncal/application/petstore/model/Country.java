package org.agoncal.application.petstore.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Cacheable
@XmlRootElement
public class Country implements Serializable {

    // ======================================
    // = Attributes =
    // ======================================

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Version
    @Column(name = "version")
    private int version;

    @Column(length = 2, name = "iso_code", nullable = false)
    @NotNull
    @Size(min = 2, max = 2)
    private String isoCode;

    @Column(length = 80, nullable = false)
    @NotNull
    @Size(min = 2, max = 80)
    private String name;

    @Column(length = 80, name = "printable_name", nullable = false)
    @NotNull
    @Size(min = 2, max = 80)
    private String printableName;

    @Column(length = 3)
    @NotNull
    @Size(min = 3, max = 3)
    private String iso3;

    @Column(length = 3)
    @NotNull
    @Size(min = 3, max = 3)
    private String numcode;

    // ======================================
    // = Constructors =
    // ======================================

    public Country() {
    }

    public Country(String isoCode, String name, String printableName, String iso3, String numcode) {
        this.isoCode = isoCode;
        this.name = name;
        this.printableName = printableName;
        this.iso3 = iso3;
        this.numcode = numcode;
    }

    // ======================================
    // = Getters & setters =
    // ======================================

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrintableName() {
        return printableName;
    }

    public void setPrintableName(String printableName) {
        this.printableName = printableName;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getNumcode() {
        return numcode;
    }

    public void setNumcode(String numcode) {
        this.numcode = numcode;
    }

    // ======================================
    // = Methods hash, equals, toString =
    // ======================================

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }
        Country country = (Country) o;
        return Objects.equals(isoCode, country.isoCode);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(isoCode);
    }

    @Override
    public String toString() {
        return name;
    }
}
