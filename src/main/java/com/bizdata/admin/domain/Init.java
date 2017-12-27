package com.bizdata.admin.domain;

import com.bizdata.commons.domain.BaseStringEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 数据初始化表
 *
 * @author wry
 * @version 1.0
 */
@Entity
@Table(name = "admin_init")
@Data
@EqualsAndHashCode(callSuper = true)
public class Init extends BaseStringEntity {
    /**
     * 是否已经初始化
     * 0(flase) 表示没有初始化 1(true) 表示已经初始化 -- 只限于mysql
     */
    @Column(nullable = false)
    private boolean state = false;

    @Column(nullable = false)
    private Date date = new Date();

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
