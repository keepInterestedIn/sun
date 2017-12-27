package com.bizdata.app.version.controller.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author w
 */
@Data
public class SearchVersionManagerVO {
    /**
     * 版本号
     */
    private String searchVersionId;

    private Date searchStartTime;

    private Date searchEndTime;
}
