package com.creativeshare.agriculturalstockexchange.models;

import java.io.Serializable;

public class Insuarce_Model implements Serializable {
 private String request_id;
    private String user_id;
    private String comp_id;
    private String serv_id;
    private String id_advertisement;
    private String amount;
    private String apppoiment;
    private String amount_desc;
    private String from_address;
    private String from_lat;
    private String from_long;
    private String to_address;
    private String to_lat;
    private String to_long;
    private String date;
    private String from_user_name;
    private String comp_name;
    private String offer_value;

    public String getRequest_id() {
        return request_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getComp_id() {
        return comp_id;
    }

    public String getServ_id() {
        return serv_id;
    }

    public String getId_advertisement() {
        return id_advertisement;
    }

    public String getAmount() {
        return amount;
    }

    public String getApppoiment() {
        return apppoiment;
    }

    public String getAmount_desc() {
        return amount_desc;
    }

    public String getFrom_address() {
        return from_address;
    }

    public String getFrom_lat() {
        return from_lat;
    }

    public String getFrom_long() {
        return from_long;
    }

    public String getTo_address() {
        return to_address;
    }

    public String getTo_lat() {
        return to_lat;
    }

    public String getTo_long() {
        return to_long;
    }

    public String getDate() {
        return date;
    }

    public String getFrom_user_name() {
        return from_user_name;
    }

    public String getComp_name() {
        return comp_name;
    }

    public String getOffer_value() {
        return offer_value;
    }
}
