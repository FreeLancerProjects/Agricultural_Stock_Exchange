package com.creativeshare.agriculturalstockexchange.models;

import android.net.Uri;

import java.io.Serializable;
import java.util.List;

public class Services_Model implements Serializable {
    private static String id;
    private static String servi;
    private static String company_id;
    private static Adversiting_Model adversiting_model;

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        Services_Model.id = id;
    }

    public static String getServi() {
        return servi;
    }

    public static void setServi(String servi) {
        Services_Model.servi = servi;
    }

    public static String getCompany_id() {
        return company_id;
    }

    public static void setCompany_id(String company_id) {
        Services_Model.company_id = company_id;
    }

    public static Adversiting_Model getAdversiting_model() {
        return adversiting_model;
    }

    public static void setAdversiting_model(Adversiting_Model adversiting_model) {
        Services_Model.adversiting_model = adversiting_model;
    }
}
