package com.example.crudapplication.data.dto;

public class UserProfileRequestDto {
    private String name;
    private String phone;
    private String address;
    private String profileImage;    // Base64로 인코딩된 이미지 데이터 추가

    public UserProfileRequestDto(String name, String phone, String address, String profileImage) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.profileImage = profileImage;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
