package com.bizdata.app.banner.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.bizdata.app.banner.constant.BannerPositionEnum;
import com.bizdata.app.banner.constant.GotoChildEnum;
import com.bizdata.app.banner.constant.GotoEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.sdevil507.base.JpaUUIDBaseEntity;

/**
 * Banner实体
 * <p>
 * Created by sdevil507 on 2017/9/5.
 */
@Table(name = "app_banner")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Banner extends JpaUUIDBaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * banner名称
     */
    @Column
    private String name;

    /**
     * banner图片地址
     */
    @Column
    private String picUrl;

    /**
     * 排序号
     */
    @Column
    private int serialNum;

    /**
     * 开始时间
     */
    @Column
    private Date startTime;

    /**
     * 结束时间
     */
    @Column
    private Date endTime;

    /**
     * 跳转URL
     */
    @Column
    private String targetUrl;

    /**
     * banner展示位置
     *
     * @see BannerPositionEnum
     */
    @Enumerated(EnumType.STRING)
    @Column
    private BannerPositionEnum bannerPositionEnum;

    /**
     * 跳转类型
     */
    @Enumerated(EnumType.STRING)
    @Column
    private GotoEnum gotoEnum;
    
    /**
     * 跳转子类型
     */
    @Enumerated(EnumType.STRING)
    @Column
    private GotoChildEnum gotoChildEnum;
    /**
     * 文章,圈子,话题的序号,用于管理员编辑
     */
    @Column
    private String gotonum;
    /**
     * 文章,圈子,话题的主键id,用于前端跳转
     */
    @Column
    private String gotoid;
    /**
     * 状态;1正常 2删除
     */
    private String state = "2";
}
