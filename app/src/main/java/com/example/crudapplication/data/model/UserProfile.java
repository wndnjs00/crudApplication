package com.example.crudapplication.data.model;

import java.util.UUID;

public class UserProfile {
    private UUID uuid; // 데이터베이스에서 자동 생성
    private String name;
    private String phone;
    private String address;
    private String profileImage; // Base64 이미지 데이터 추가

    public UserProfile() {
        // UUID 자동 생성
        this.uuid = UUID.randomUUID();
    }

    public UserProfile(String name, String phone, String address, String profileImage) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.profileImage = profileImage;
    }

    // Getter와 Setter
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    // 추후 삭제
    // JSON 데이터를 확인하기 위한 toString() 메서드 추가
    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + uuid + "\"," +
                "\"name\":\"" + name + "\"," +
                "\"phone\":\"" + phone + "\"," +
                "\"address\":\"" + address + "\"" +
                "\"profileImage\":\"" + profileImage + "\"" +
                "}";
    }
}
