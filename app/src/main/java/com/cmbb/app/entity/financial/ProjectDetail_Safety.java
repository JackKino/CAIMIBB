package com.cmbb.app.entity.financial;

import java.io.Serializable;

/**
 * Created by Storm on 2015/11/28.
 * DES:
 */
public class ProjectDetail_Safety implements Serializable {
    private String info;
    private String url;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
