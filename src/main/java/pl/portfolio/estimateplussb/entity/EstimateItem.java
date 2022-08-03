package pl.portfolio.estimateplussb.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Entity
@Table(name = "estimateitem")
public class EstimateItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(0)
    @Column(name = "individualVatRate")
    private int individualVatRate;
    @Column(name = "totalNetPrice")
    private BigDecimal totalNetPrice;
    @Min(1)
    private int quantity;
    @Column(name = "positionInEstimate")
    private int positionInEstimate;
    @OneToOne
    @JoinColumn(name = "priceListItem_id")
    private PriceListItem priceListItem;

    public EstimateItem() {
    }

    public void calculateAmounts(int quantity) {
        this.totalNetPrice = this.priceListItem.getUnitNetPrice().multiply(BigDecimal.valueOf(quantity));
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIndividualVatRate() {
        return individualVatRate;
    }

    public void setIndividualVatRate(int individualVatRate) {
        this.individualVatRate = individualVatRate;
    }

//    public BigInteger getVatAmount() {
//        return vatAmount;
//    }
//
//    public void setVatAmount(BigInteger vatAmount) {
//        this.vatAmount = vatAmount;
//    }


    public BigDecimal getTotalNetPrice() {
        return totalNetPrice;
    }

    public void setTotalNetPrice(BigDecimal totalNetPrice) {
        this.totalNetPrice = totalNetPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public PriceListItem getPriceListItem() {
        return priceListItem;
    }

    public void setPriceListItem(PriceListItem priceListItem) {
        this.priceListItem = priceListItem;
    }

    public int getPositionInEstimate() {
        return positionInEstimate;
    }

    public void setPositionInEstimate(int positionInEstimate) {
        this.positionInEstimate = positionInEstimate;
    }

    @Override
    public String toString() {
        return "EstimateItem{" +
                "id=" + id +
                ", individualVatRate=" + individualVatRate +
                ", totalNetPrice=" + totalNetPrice +
                ", quantity=" + quantity +
                ", priceListItem=" + priceListItem +
                '}';
    }
}
