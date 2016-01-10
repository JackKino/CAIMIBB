package com.cmbb.app.entity.recharge;

/**
 * Created by Storm on 2015/12/1.
 * DES:
 */
public class RechargeHistoryItemEntity {
//    status:1,           //状态[0:充值失败,1:充值成功]
//    money:500,                      //充值金额
//    action_time: "2015/10/31 15:28",    //操作时间


    private String status;
    private String money;
    private String action_time;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAction_time() {
        return action_time;
    }

    public void setAction_time(String action_time) {
        this.action_time = action_time;
    }
}
