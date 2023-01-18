package com.gariskode.onewarehouse.models;

import java.sql.Timestamp;

public class ProfileModels {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

     Result result;

    public Result getResult() {
        return result;
    }

    public class Result {
        private Integer id;
        private String email;
        private String name_shop;
        private String address;
        private Timestamp created_at;

        public Timestamp getCreated_at() {
            return created_at;
        }

        public void setCreated_at(Timestamp created_at) {
            this.created_at = created_at;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName_shop() {
            return name_shop;
        }

        public void setName_shop(String name_shop) {
            this.name_shop = name_shop;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

    }
}
