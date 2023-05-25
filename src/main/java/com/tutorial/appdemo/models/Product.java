package com.tutorial.appdemo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

//POJO = Plain Object Java Object
@Entity

public class Product {
    // this is "primary key"
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // self-generated ID //auto-increament

    private Long id;
    private String productName;
    @Column(name = "yearDemo") // the same with data types in MySQL cause error
    private int year;
    private Double price;
    private String url;

    // default constructor
    public Product() {
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
