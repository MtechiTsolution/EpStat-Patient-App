package com.example.addpatient.ModelClass;

public class addPatient {

    String MrNumber,DoctorName,currentStatus,gestaionalAge,dischargeDate,dateOfBirth,age,time,dateOfAdmission,PatientName,PhoneNumber,Address,Weight,Diagnosis,GynaeUnit,DurationOfStay,Miscellanous,sex,WeightAccordingToGestationalAge,status;

    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public addPatient()
    {

    }

    public addPatient( String MrNumver,String DoctorName,String time, String dateOfAdmission,String patientName, String phoneNumber,
                       String sex,String address,String gestaionalAge,String WeightAccordingToGestationalAge,
                       String weight, String dateOfBirth,String Age, String diagnosis,String gynaeUnit, String status,String durationOfStay,String dischargeDate,String currentStatus, String miscellanous) {
        this.age=Age;
        this.DoctorName=DoctorName;
        this.MrNumber=MrNumver;
        this.time = time;
        this.dateOfAdmission = dateOfAdmission;
        this.PatientName = patientName;
        this.PhoneNumber = phoneNumber;
        this.sex=sex;
        this.Address = address;
        this.gestaionalAge = gestaionalAge;
        this.WeightAccordingToGestationalAge=WeightAccordingToGestationalAge;
        this.Weight=weight;
        this.dateOfBirth = dateOfBirth;
        this.Diagnosis = diagnosis;
        this.GynaeUnit = gynaeUnit;
        this.status=status;
        this.DurationOfStay = durationOfStay;
        this.dischargeDate = dischargeDate;
        this.currentStatus = currentStatus;
        this.Miscellanous = miscellanous;


    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWeightAccordingToGestationalAge() {
        return WeightAccordingToGestationalAge;
    }

    public void setWeightAccordingToGestationalAge(String weightAccordingToGestationalAge) {
        WeightAccordingToGestationalAge = weightAccordingToGestationalAge;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getGestaionalAge() {
        return gestaionalAge;
    }

    public void setGestaionalAge(String gestaionalAge) {
        this.gestaionalAge = gestaionalAge;
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

    public String getGynaeUnit() {
        return GynaeUnit;
    }

    public void setGynaeUnit(String gynaeUnit) {
        GynaeUnit = gynaeUnit;
    }

    public String getDurationOfStay() {
        return DurationOfStay;
    }

    public void setDurationOfStay(String durationOfStay) {
        DurationOfStay = durationOfStay;
    }

    public String getMiscellanous() {
        return Miscellanous;
    }

    public void setMiscellanous(String miscellanous) {
        Miscellanous = miscellanous;
    }
}
