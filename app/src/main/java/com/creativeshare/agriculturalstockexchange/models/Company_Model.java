package com.creativeshare.agriculturalstockexchange.models;

import java.io.Serializable;
import java.util.List;

public class Company_Model  implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public class Data
implements Serializable    {
     private String user_id;
        private String user_type;
        private String user_name;
        private String user_email;
        private String user_pass;
        private String user_phone_code;
        private String user_phone;
        private String user_full_name;
        private String user_image;
        private String user_api_token;
        private String user_address;
        private String commercial_register;
        private String post_code;
        private String home_num;
        private String user_age;
        private String user_gender;
        private String user_google_lat;
        private String user_google_long;
        private String user_country;
        private String user_nationality;
        private String user_city;
        private String date_registration;
        private String soft_type;
        private String is_login;
        private String user_show;
        private String insurance_services;
        private String shipping_serv;
        private String packaging_serv;
        private String storage_serv;

        public String getUser_id() {
            return user_id;
        }

        public String getUser_type() {
            return user_type;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getUser_email() {
            return user_email;
        }

        public String getUser_pass() {
            return user_pass;
        }

        public String getUser_phone_code() {
            return user_phone_code;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public String getUser_full_name() {
            return user_full_name;
        }

        public String getUser_image() {
            return user_image;
        }

        public String getUser_api_token() {
            return user_api_token;
        }

        public String getUser_address() {
            return user_address;
        }

        public String getCommercial_register() {
            return commercial_register;
        }

        public String getPost_code() {
            return post_code;
        }

        public String getHome_num() {
            return home_num;
        }

        public String getUser_age() {
            return user_age;
        }

        public String getUser_gender() {
            return user_gender;
        }

        public String getUser_google_lat() {
            return user_google_lat;
        }

        public String getUser_google_long() {
            return user_google_long;
        }

        public String getUser_country() {
            return user_country;
        }

        public String getUser_nationality() {
            return user_nationality;
        }

        public String getUser_city() {
            return user_city;
        }

        public String getDate_registration() {
            return date_registration;
        }

        public String getSoft_type() {
            return soft_type;
        }

        public String getIs_login() {
            return is_login;
        }

        public String getUser_show() {
            return user_show;
        }

        public String getInsurance_services() {
            return insurance_services;
        }

        public String getShipping_serv() {
            return shipping_serv;
        }

        public String getPackaging_serv() {
            return packaging_serv;
        }

        public String getStorage_serv() {
            return storage_serv;
        }
    }
}
