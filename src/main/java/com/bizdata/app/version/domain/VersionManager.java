package com.bizdata.app.version.domain;

import com.bizdata.app.version.constant.ClassifyEnum;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 版本管理
 *
 * @author w
 */
@Data
@Entity
@Table(name = "app_version")
public class VersionManager {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * 版本号
     */
    @Column(length = 100)
    private String versionId;

    /**
     * 更新内容
     */
    @Column
    private String updateContent;

    /**
     * 更新链接地址
     */
    @Column
    private String linkUrl;

    /**
     * 更新标志
     * 0：普通更新1：强制更新
     */
    @Column
    private Integer updateType;

    /**
     * 上下线标识(默认值为0)
     * 0：下线 1：上线
     */
    @Column(columnDefinition = "INT default 0")
    private Integer status;

    /**
     * 更新对应的操作系统
     */
    @Column
    @Enumerated(EnumType.STRING)
    private ClassifyEnum updateClassify;

    /**
     * 更新时间
     */
    @Column
    private Date updateTime;

    /**
     * 创造时间
     */
    @Column
    private Date createTime;
}
