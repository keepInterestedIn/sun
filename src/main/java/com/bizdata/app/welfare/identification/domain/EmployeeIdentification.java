package com.bizdata.app.welfare.identification.domain;

import me.sdevil507.base.JpaUUIDBaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * 用户认证
 *
 */
@Table(name = "employee_identification")
@Entity
public class EmployeeIdentification extends JpaUUIDBaseEntity {
    /**
     * 用户id
     */
    @Column
    private String userId;

    /**
     * 姓名
     */
    @Column
    private String name;

    /**
     * 证件类型
     * <p>
     * <option value="10">居民身份证</option>
     <option value="11">居民户口簿</option>
     <option value="12">驾驶证</option>
     <option value="13">军官证</option>
     <option value="16">异常身份证</option>
     <option value="17">台湾居民来往大陆通行证</option>
     <option value="19">港澳居民来往内地通行证</option>
     <option value="21">组织机构代码证</option>
     <option value="51">护照</option>
     <option value="99">其他证件</option>
     */
    @Column
    private String type;

    /**
     * 证件号
     */
    @Column
    private String numId;


    /**
     * 认证状态（0:未认证，1:认证中，2:已认证，3:认证失败）
     */
    @Column
    private Integer status;

    @Column
    private String showStatus;

    @Column
    private Timestamp updateTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumId() {
        return numId;
    }

    public void setNumId(String numId) {
        this.numId = numId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(String showStatus) {
        this.showStatus = showStatus;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
