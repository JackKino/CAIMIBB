package com.cmbb.app.entity.mine;

import com.cmbb.app.action.common.Tools;

import java.io.Serializable;

/**
 * Created by Storm on 2015/12/1.
 * DES:
 */
public class SafetyAccount implements Serializable{
    private String bank_no;
    private String name;
    private String pid;
    private String user_id;
    private String bank_name;
    private String bank_logo;

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_logo() {
        return bank_logo;
    }

    public void setBank_logo(String bank_logo) {
        this.bank_logo = bank_logo;
    }

    public String getBank_no() {
        return bank_no;
    }

    public void setBank_no(String bank_no) {
        this.bank_no = bank_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean hasBindBank() {
        if (Tools.isEmpty(bank_no) || Tools.isEmpty(name)) {
            return false;
        }
        return true;
    }
}
