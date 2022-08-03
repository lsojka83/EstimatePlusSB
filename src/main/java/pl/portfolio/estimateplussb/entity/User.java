package pl.portfolio.estimateplussb.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(groups = Default.class)
    @Column(name = "userName")
    private String userName;
    @NotBlank
    @NotNull
    @Email
    private String email;
    //    @Password //own validator
    private String password;
    @Column(name = "passwordUnhashed")
    private String passwordUnhashed;
    private boolean admin;
    //    @OneToMany (fetch = FetchType.EAGER)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_estimate",
            joinColumns = @JoinColumn(name = "User_id"),
            inverseJoinColumns = @JoinColumn(name = "estimates_id"))
    List<Estimate> estimates = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "userPriceList_id")
    private PriceList userPriceList;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public PriceList getUserPriceList() {
        return userPriceList;
    }

    public void setUserPriceList(PriceList userPriceList) {
        this.userPriceList = userPriceList;
    }

    public List<Estimate> getEstimates() {
        return estimates;
    }

    public void setEstimates(List<Estimate> estimates) {
        this.estimates = estimates;
    }

    public String getPasswordUnhashed() {
        return passwordUnhashed;
    }

    public void setPasswordUnhashed(String passwordUnhashed) {
        this.passwordUnhashed = passwordUnhashed;
    }

//    @Override
//    public String toString() {
//        return "User{" +
//                "id=" + id +
//                ", userName='" + userName + '\'' +
//                ", email='" + email + '\'' +
//                ", password='" + password + '\'' +
//                ", admin=" + admin +
//                ", userPriceList=" + userPriceList +
//                '}';
//    }
}
