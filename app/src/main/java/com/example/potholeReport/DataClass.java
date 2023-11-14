package com.example.potholeReport;


public class DataClass {
    private String dataAddress;
    private String dataAddress2;
    private String dataDesc;
    private String dataContact;
    private String dataImage;
    private String key;
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getDataAddress() {
        return dataAddress;
    }
    public String getDataAddress2() {
        return dataAddress2;
    }
    public String getDataDesc() {
        return dataDesc;
    }
    public String getDataContact() {
        return dataContact;
    }
    public String getDataImage() {
        return dataImage;
    }
    public DataClass(String dataAddress,String dataAddress2, String dataDesc, String dataContact, String dataImage) {
        this.dataAddress = dataAddress;
        this.dataAddress2 = dataAddress2;

        this.dataDesc = dataDesc;
        this.dataContact = dataContact;
        this.dataImage = dataImage;
    }
    public DataClass(){
    }
}