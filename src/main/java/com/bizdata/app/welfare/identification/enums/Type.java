package com.bizdata.app.welfare.identification.enums;

/**
 * Created by guoshan on 2017/12/7.
 *
 * 证件类型
 */
public enum Type {
    ID_CARD("10", "居民身份证"), HOUSEHOLD_REGISTER("11", "居民户口簿"), DRIVING_LICENCE("12", "驾驶证"),
    CERTIFICATE_OF_OFFICERS("13", "军官证"), ABNORMA_IDENTITY_CARD("16", "异常身份证"), TAIWAN_PASS_CHECK("17", "台湾居民来往大陆通行证"),
    HONGKONG_MACAO("19", "港澳居民来往内地通行证"), ORGANIZATION_CODE_CERTIFICATE("21", "组织机构代码证"), PASSPORT("51", "护照"),
    OTHER("99", "其他证件");

    private String code;
    private String value;

    Type(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String getText(String code) {
        for(Type type : Type.values()){
            if(type.getCode().equals(code)){
                return type.getValue();
            }
        }
        return null;
    }
}
