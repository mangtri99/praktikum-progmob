package com.example.tesshared.Produk;

import java.util.List;

public class GetProduk {

    String status;
    List<ResultProduk> result = null;
    List<ResultDetail> test = null;
    String message;

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public List<ResultProduk> getResult() {
        return result;
    }

}
