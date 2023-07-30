package com.tutorial.appdemo.models;

import jakarta.persistence.*;

import java.util.Calendar;


//POJO = Plain Object Java Object
@Entity
@Table(name ="tblProduct") //Assign name for table Product
public class Product {
    // this is "primary key"
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO) // self-generated ID //auto-increament
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1 //increment by 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )

    private Long id;
    //Validate = constraint
    @Column(nullable = false, unique = true, length = 300) // Don't allow the same productName & Don't NULL
    private String productName;
    @Column(name = "yearDemo") // the same with data types in MySQL cause error
    private int year;
    private Double price;
    private String url;

    // default constructor
    public Product() {
    }

    //Calculated field = transient //It isn't available in DB => Ex: scores Math, scores Literature  => Average scores
    @Transient
    private int age; // age is calculated from year
    public int getAge() {
        return Calendar.getInstance().get(Calendar.YEAR) - year; //Current year - year = age
    }

    public Product(String productName, int year, Double price, String url) {
        super();
        this.productName = productName;
        this.year = year;
        this.price = price;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Products [id=" + id + ", productName=" + productName + ", year=" + year + ", price=" + price + ", url="
                + url + "]";
    }

}
