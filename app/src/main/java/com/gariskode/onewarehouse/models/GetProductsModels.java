package com.gariskode.onewarehouse.models;

import java.util.List;

public class GetProductsModels {

    private  String message;
    private List<Result> result;

    public List<Result> getResult() {
        return result;
    }

    public void setResults(List<Result> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Result{
        private Integer id;
        private  String name;
        private  String description;
        private  String barcode;
        private Integer stock;
        private Integer selling_price;
        private Integer capital_price;
        private Integer category_id;
        private Integer shop_id;
        private String category_name;


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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public Integer getStock() {
            return stock;
        }

        public void setStock(Integer stock) {
            this.stock = stock;
        }

        public Integer getSelling_price() {
            return selling_price;
        }

        public void setSelling_price(Integer selling_price) {
            this.selling_price = selling_price;
        }

        public Integer getCapital_price() {
            return capital_price;
        }

        public void setCapital_price(Integer capital_price) {
            this.capital_price = capital_price;
        }

        public Integer getCategory_id() {
            return category_id;
        }

        public void setCategory_id(Integer category_id) {
            this.category_id = category_id;
        }

        public Integer getShop_id() {
            return shop_id;
        }

        public void setShop_id(Integer shop_id) {
            this.shop_id = shop_id;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }
    }
}
