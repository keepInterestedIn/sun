package com.bizdata.commons.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 所有主键为uuid实体类的基类
 * PS：@MappedSuperclass作用是将多个实体类共同拥有的树形抽象出来
 *
 * @author wry
 */
@MappedSuperclass
public class BaseStringEntity {
    /**
     * 主键 uuid生成 32位16进制字符串
     * PS：1.protected表示作用范围自身类和继承该类的子类
     * 2.GenericGenerator中的name和GeneratedValue的generator一一对应
     */
    @Id
    @GenericGenerator(name = "sys-uuid", strategy = "uuid")
    @GeneratedValue(generator = "sys-uuid")
    @Column(name = "id", length = 32)
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
