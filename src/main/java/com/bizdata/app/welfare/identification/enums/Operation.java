package com.bizdata.app.welfare.identification.enums;

/**
 * Created by guoshan on 2017/12/7.
 */
public enum Operation {
    SUCCESS(0, "通过"), FAILED(1, "失败");

    private Integer code;
    private String  value;

    Operation(Integer code, String value) {
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
}
