package com.example.rick.sample;

import java.util.List;

/**
 * Created by rick on 5/29/16.
 */
public class Customer extends Model {

    private String name;
    private int vat_number;
    private String address;
    private String telephone;
    private String fax;
    private String email;
    private String city;
    private String updated;

    public int getVat_number() {
        return vat_number;
    }

    public void setVat_number(int vat_number) {
        this.vat_number = vat_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }



    public Customer()
    {}

    public Customer(String name, int vat_number, String address, String telephone, String fax, String email, String city, String updated) {
        this.name = name;
        this.vat_number = vat_number;
        this.address = address;
        this.telephone = telephone;
        this.fax = fax;
        this.email = email;
        this.city = city;
        this.updated = updated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public static List<Customer> getAll()
    {

        return Customer.listAll(Customer.class);
    }
}
