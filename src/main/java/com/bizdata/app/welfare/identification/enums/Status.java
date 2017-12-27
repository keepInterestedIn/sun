package com.bizdata.app.welfare.identification.enums;

/**
 * Created by guoshan on 2017/12/7.
 *
 * 认证状态
 */
public enum Status {
    UNCERTIFIED(0, "未认证"), BEING_CERTIFIED(1, "认证中"), CERTIFIED(2, "已认证"), CERTIFIED_FAILED(3, "认证失败");
    private Integer code;
    private String value;

    Status(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String getText(Integer code) {
        for(Status status : Status.values()){
            if(status.getCode().equals(code)){
                return status.getValue();
            }
        }
        return null;
    }
}
