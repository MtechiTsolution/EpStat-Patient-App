package com.example.addpatient.ModelClass;

public class Model {

    String MrNumber,currentStatus,dischargeDate,dateOfBirth,age,time,dateOfAdmission,PatientName,PhoneNumber,Address,Weight,Diagnosis,Miscellanous,Gender;

    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Model()
    {

    }


    public Model( String MrNumber,String currentStatus, String Gender, String dischargeDate, String dateOfBirth,String Age, String time, String dateOfAdmission, String patientName, String phoneNumber, String address, String weight, String diagnosis, String miscellanous) {

        this.MrNumber=MrNumber;
        this.currentStatus = currentStatus;
        this.dischargeDate = dischargeDate;
        this.dateOfBirth = dateOfBirth;
        this.age=Age;
        this.time = time;
        this.dateOfAdmission = dateOfAdmission;
       this.PatientName = patientName;
        this.PhoneNumber = phoneNumber;
        this.Address = address;
        this.Weight = weight;
        this.Diagnosis = diagnosis;
        this.Miscellanous = miscellanous;
        this.Gender = Gender;

    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMrNumber() {
        return MrNumber;
    }

    public void setMrNumber(String mrNumber) {
        MrNumber = mrNumber;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDateOfAdmission() {
        return dateOfAdmission;
    }

    public void setDateOfAdmission(String dateOfAdmission) {
        this.dateOfAdmission = dateOfAdmission;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getDiagnosis() {
        return Diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        Diagnosis = diagnosis;
    }

    public String getMiscellanous() {
        return Miscellanous;
    }

    public void setMiscellanous(String miscellanous) {
        Miscellanous = miscellanous;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String sex) {
        this.Gender = Gender;
    }
}
