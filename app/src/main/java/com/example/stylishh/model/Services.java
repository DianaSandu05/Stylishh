package com.example.stylishh.model;

import java.io.Serializable;

public class Services implements Serializable {
    public static final String TAG = "Services";
   /* private static final long serialVersionUID=""; // search what's this*/

    private long sId;
    private String sName;
    private String sPostcode;
    private Availability sAvailability;

    public Services()
    {

    }
    public Services(String serviceName, String postcode)
    {
        this.sName = serviceName;
        this.sPostcode = postcode;
    }

    public long getsId()
    {return sId;}

    public void setsId(long sId){this.sId = sId;}

    public String getsName()
    { return sName;}
    public void setsName(String sName){this.sName = sName;}
    public String getsPostcode(){
        return sPostcode;}
    public void setsPostcode(String postcode){
        this.sPostcode = postcode;
    }
    public Availability getsAvailability()
    {return sAvailability;}
    public void setsAvailability(Availability sAvailability)
    {this.sAvailability = sAvailability;
    }
}
