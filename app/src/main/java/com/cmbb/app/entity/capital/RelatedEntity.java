package com.cmbb.app.entity.capital;

import com.cmbb.app.action.common.Logger;
import com.google.gson.Gson;

/**
 * Created by admin on 2016/1/6.
 */
public class RelatedEntity {
    //    "amount": "100",
//            "cardHolderId": "22022119870503333X",
//            "cardHolderName": "施阳",
//            "externalRefNumber": "20160106141057",
//            "pan": "6230200091895248",
//            "phoneNO": "18664550423",
//            "storablePan": "6230205248",
//            "token": "359494301"
    private String amount;
    private String cardHolderId;
    private String cardHolderName;
    private String externalRefNumber;
    private String pan;
    private String phoneNO;
    private String storablePan;
    private String token;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCardHolderId() {
        return cardHolderId;
    }

    public void setCardHolderId(String cardHolderId) {
        this.cardHolderId = cardHolderId;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getExternalRefNumber() {
        return externalRefNumber;
    }

    public void setExternalRefNumber(String externalRefNumber) {
        this.externalRefNumber = externalRefNumber;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getPhoneNO() {
        return phoneNO;
    }

    public void setPhoneNO(String phoneNO) {
        this.phoneNO = phoneNO;
    }

    public String getStorablePan() {
        return storablePan;
    }

    public void setStorablePan(String storablePan) {
        this.storablePan = storablePan;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        String resData = new Gson().toJson(this);
        Logger.i("related", resData);
        return resData;
    }
}
