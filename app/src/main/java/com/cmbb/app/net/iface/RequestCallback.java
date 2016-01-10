package com.cmbb.app.net.iface;


import com.cmbb.app.entity.base.BaseEntity;

/**
 * @Directions:网络请求回调接口
 * @author: liman
 */
public interface RequestCallback {
    /**
     * @Directions:请求成功
     * @author: liman
     * @date: 2015-8-12
     * @tag:@param result 结果实体
     * @tag:@param jsonData 原始json数据
     */
    void onRequestSucess(BaseEntity result, String jsonData);

    /**
     * @Directions:请求错误
     * @author: liman
     * @date: 2015-8-12
     * @tag:@param msg 出错原因
     */
    void onRequestFailed(String msg);
}
