package com.bizdata.app.welfare.identification.controller.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by guoshan on 2017/12/7.
 */
public class SaveRequestVO implements Serializable {
    private static final long serialVersionUID = 887772769101021857L;
    private Integer type;
    private String id;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
