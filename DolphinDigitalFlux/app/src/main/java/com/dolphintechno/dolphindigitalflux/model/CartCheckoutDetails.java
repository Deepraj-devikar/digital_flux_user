package com.dolphintechno.dolphindigitalflux.model;

public class CartCheckoutDetails {

    public String UserId;
    public String firstName;
    public String lastName;
    public String fullName;
    public String mobile;
    public String personEmail;
    public String address;
    public String state;
    public String district;
    public String city;
    public String pincode;

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getUserId() {
        return UserId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public String getAddress() {
        return address;
    }

    public String getState() {
        return state;
    }

    public String getDistrict() {
        return district;
    }

    public String getCity() {
        return city;
    }

    public String getPincode() {
        return pincode;
    }
}
