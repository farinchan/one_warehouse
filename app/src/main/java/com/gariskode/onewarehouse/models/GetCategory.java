package com.gariskode.onewarehouse.models;

import java.util.List;

public class GetCategory {
    private String message;

    private List<Result> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Result> getResults() {
        return result;
    }

    public void setResults(List<Result> result) {
        this.result = result;
    }

    public class Result{
        private Integer id;
        private String name;
        private String slug;
        private Integer shop_id;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public Integer getShop_id() {
            return shop_id;
        }

        public void setShop_id(Integer shop_id) {
            this.shop_id = shop_id;
        }
    }
}
