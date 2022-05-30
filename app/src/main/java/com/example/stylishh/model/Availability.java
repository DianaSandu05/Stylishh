package com.example.stylishh.model;

public class Availability {
    private static final String TAG = "Availability";

    private long aId;
    private String aDateTime;
    private String status;
    private Services aServices;

    public Availability(){}
    public Availability(String appDateTime, String appStatus)
    {this.aDateTime = appDateTime;
    this.status = appStatus;}

    public long getaId()
    {return aId;}
    public void setaId(long aId){
        this.aId = aId;
    }
    public String getaDateTime(){return aDateTime;}
    public void setaDateTime(String aDateTime){this.aDateTime = aDateTime;}

    public String getStatus(){return status;}
    public void setStatus(String status){this.status = status;}
    public Services getServices(){
        return aServices;
    }
    public void setaServices(Services aServices)
    {
        this.aServices = aServices;
    }
}
