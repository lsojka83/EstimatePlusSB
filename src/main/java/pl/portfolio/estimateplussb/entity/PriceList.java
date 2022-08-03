package pl.portfolio.estimateplussb.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "pricelist")
public class PriceList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @Column(name = "numberOfItems")
    private Long numberOfItems;
    @OneToMany
    @JoinTable(name = "pricelist_pricelistitem",
            joinColumns = @JoinColumn(name = "PriceList_id"),
            inverseJoinColumns = @JoinColumn(name = "priceListItems_id"))
    private List<PriceListItem> priceListItems;
    @Column(name = "userOwned")
    boolean userOwned;
    @Column(name = "errorMessage")
    private String errorMessage= "";

    public PriceList() {
    }

    public void countItems()
    {
        this.numberOfItems = Long.valueOf(this.priceListItems.size());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(Long numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public List<PriceListItem> getPriceListItems() {
        return priceListItems;
    }

    public void setPriceListItems(List<PriceListItem> priceListItems) {
        this.priceListItems = priceListItems;
    }

    public boolean isUserOwned() {
        return userOwned;
    }

    public void setUserOwned(boolean userOwned) {
        this.userOwned = userOwned;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "PriceList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numberOfItems=" + numberOfItems +
                ", priceListItems=" + priceListItems +
                '}';
    }
}
