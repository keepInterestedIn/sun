package com.bizdata.app.welfare.identification.controller;

import com.bizdata.app.welfare.identification.controller.vo.SaveRequestVO;
import com.bizdata.app.welfare.identification.domain.EmployeeIdentification;
import com.bizdata.app.welfare.identification.enums.Operation;
import com.bizdata.app.welfare.identification.enums.Status;
import com.bizdata.app.welfare.identification.enums.Type;
import com.bizdata.app.welfare.identification.service.WelfareIdentificationService;
import com.bizdata.commons.utils.DateEditor;
import com.bizdata.commons.utils.JqGridPageVO;
import com.bizdata.commons.utils.JqGridPageVO2JpaPageParamVO;
import com.bizdata.commons.utils.JqGridSortVO;
import com.bizdata.commons.utils.JqGridSortVO2JpaSortParamVO;
import com.bizdata.commons.utils.StringEditor;

import me.sdevil507.resp.ResultUtil;
import me.sdevil507.resp.ResultVO;
import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaPageResultVO;
import me.sdevil507.vo.JpaSortParamVO;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by guoshan on 2017/12/7.
 */
@Controller
@RequestMapping(value = "/admin/welfare_identification")
public class WelfareIdentificationController {
    private final JqGridPageVO2JpaPageParamVO jqGridPageVO2JpaPageParamVO;

    private final JqGridSortVO2JpaSortParamVO jqGridSortVO2JpaSortParamVO;

    private final WelfareIdentificationService welfareIdentificationService;
    @InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		// 对于需要转换为Date类型的属性，使用DateEditor进行处理
		binder.registerCustomEditor(Date.class, new DateEditor());
		//String类型html转义
        binder.registerCustomEditor(String.class, new StringEditor());
	}
    @Autowired
    public WelfareIdentificationController(JqGridPageVO2JpaPageParamVO jqGridPageVO2JpaPageParamVO,
                                           JqGridSortVO2JpaSortParamVO jqGridSortVO2JpaSortParamVO,
                                           WelfareIdentificationService welfareIdentificationService) {
        this.jqGridPageVO2JpaPageParamVO = jqGridPageVO2JpaPageParamVO;
        this.jqGridSortVO2JpaSortParamVO = jqGridSortVO2JpaSortParamVO;
        this.welfareIdentificationService = welfareIdentificationService;
    }

    /**
     * 展示对应模板页面
     *
     * @return 模板页面
     */
    @RequiresPermissions("welfareIdentification:view")
    @RequestMapping(method = RequestMethod.GET)
    public String view() {
        return "app_page/welfare/identification/list";
    }

    /**
     * 查询员工认证列表
     *
     * @param jqGridPageVO 分页
     * @param jqGridSortVO 分页排序
     * @return 查询结果
     */
    @RequiresPermissions("welfareIdentification:view")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JpaPageResultVO<EmployeeIdentification, EmployeeIdentification> list(JqGridPageVO jqGridPageVO, JqGridSortVO jqGridSortVO) {
        JpaPageParamVO jpaPageParamVO = jqGridPageVO2JpaPageParamVO.convert(jqGridPageVO);
        JpaSortParamVO jpaSortParamVO = jqGridSortVO2JpaSortParamVO.convert(jqGridSortVO);
        Page<EmployeeIdentification> employeeIdentifications = welfareIdentificationService.findAllByPage(jpaPageParamVO, jpaSortParamVO);

        for (EmployeeIdentification employeeIdentification : employeeIdentifications) {
            employeeIdentification.setType(Type.getText(employeeIdentification.getType()));
            employeeIdentification.setShowStatus(Status.getText(employeeIdentification.getStatus()));
        }
        return new JpaPageResultVO<>(employeeIdentifications, EmployeeIdentification.class);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO save(@RequestBody SaveRequestVO saveRequestVO) {
        ResultVO resultVO;
        Integer type = saveRequestVO.getType();
        String id = saveRequestVO.getId();

        if (Operation.SUCCESS.getCode().equals(type)) {
            if (welfareIdentificationService.certifiedSuccess(Status.CERTIFIED.getCode(), id)) {
                resultVO = ResultUtil.create(0, "认证成功!");
            } else {
                resultVO = ResultUtil.create(-1, "认证失败!");
            }
        } else {
            if (welfareIdentificationService.certifiedSuccess(Status.CERTIFIED_FAILED.getCode(), id)) {
                resultVO = ResultUtil.create(0, "驳回成功!");
            } else {
                resultVO = ResultUtil.create(-1, "驳回失败!");
            }
        }
        return resultVO;
    }
}
