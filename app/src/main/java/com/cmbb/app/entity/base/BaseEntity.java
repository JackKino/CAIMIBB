package com.cmbb.app.entity.base;

import com.cmbb.app.net.http.ErrorCode;

public class BaseEntity {
    private int r = ErrorCode.ERROR_1;
    private String msg;

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSucess() {
        if (ErrorCode.ERROR_1 == r) {
            return true;
        } else {
            return false;
        }
    }
}
