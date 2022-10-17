package com.example.addpatient.ModelClass;

public class UserHelperClass {
    String name, username, phone,email,gender,designation,password,date,time,url;

    public UserHelperClass() {

    }
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public UserHelperClass(String name, String username, String phone, String email, String gender, String designation, String password,String date,String time,String url) {
        this.name = name;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.designation = designation;
        this.password = password;
        this.date=date;
        this.time=time;
        this.url=url;

    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserame() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPassword() {
        return password;
    }
    public void  setPassword(String password){ this.password=password;}


}
