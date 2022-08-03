package pl.portfolio.estimateplussb.entity;

import pl.portfolio.estimateplussb.converter.LocalDateTimeAttributeConverter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table (name = "pricelistitem")
public class PriceListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "vendorName")
    private String vendorName;
    @NotBlank
    @Column(name = "referenceNumber")
    private String referenceNumber;
    @NotBlank
    private String description;
    @NotBlank
    private String brand;
    private String comment;
    @DecimalMin("0")
//    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "unitNetPrice")
    private BigDecimal unitNetPrice;
    //    private BigDecimal unitNetPrice = BigDecimal.valueOf(0);
    @NotBlank
    private String unit;
    @Min(0)
    @Column(name = "baseVatRate")
    private Integer baseVatRate;
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "addedOn")
    private LocalDateTime addedOn = null;


    public PriceListItem() {
    }
    public PriceListItem(String vendorName, String referenceNumber, String description, String brand, String comment, BigDecimal unitNetPrice, String unit, Integer baseVatRate) {
        this.vendorName = vendorName;
        this.referenceNumber = referenceNumber;
        this.description = description;
        this.brand = brand;
        this.comment = comment;
        this.unitNetPrice = unitNetPrice;
        this.unit = unit;
        this.baseVatRate = baseVatRate;
    }

    @PrePersist
    public void prePersist() {
        addedOn = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BigDecimal getUnitNetPrice() {
        return unitNetPrice;
    }

    public void setUnitNetPrice(BigDecimal unitNetPrice) {
        this.unitNetPrice = unitNetPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getBaseVatRate() {
        return baseVatRate;
    }

    public void setBaseVatRate(Integer baseVatRate) {
        this.baseVatRate = baseVatRate;
    }

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
    }
//
//    @Override
//    public String toString() {
//        return "PriceListItem{" +
//                "id=" + id +
//                ", vendorName='" + vendorName + '\'' +
//                ", referenceNumber='" + referenceNumber + '\'' +
//                ", description='" + description + '\'' +
//                ", brand='" + brand + '\'' +
//                ", comment='" + comment + '\'' +
//                ", unitNetPrice=" + unitNetPrice +
//                ", unit='" + unit + '\'' +
//                ", baseVatRate=" + baseVatRate +
//                ", addedOn=" + addedOn +
//                '}';
//    }
}
