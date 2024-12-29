package com.example.crudapplication;

public class UserProfile {
    private int id; // 데이터베이스에서 자동 생성
    private String name;
    private String phone;
    private String address;

    public UserProfile() {
    }

    public UserProfile(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    // Getter와 Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    // 추후 삭제
    // JSON 데이터를 확인하기 위한 toString() 메서드 추가
    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + "\"," +
                "\"name\":\"" + name + "\"," +
                "\"phone\":\"" + phone + "\"," +
                "\"address\":\"" + address + "\"" +
                "}";
    }
}
