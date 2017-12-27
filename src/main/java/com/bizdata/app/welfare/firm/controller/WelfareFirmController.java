package com.bizdata.app.welfare.firm.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bizdata.app.welfare.firm.controller.vo.SearchWelfareFirmVO;
import com.bizdata.app.welfare.firm.controller.vo.WelfareFirmVO;
import com.bizdata.app.welfare.firm.domain.WelfareFirm;
import com.bizdata.app.welfare.firm.service.WelfareFirmService;
import com.bizdata.app.welfare.order.service.WelfareOrderService;
import com.bizdata.app.welfare.type.service.WelfareTypeService;
import com.bizdata.commons.utils.DateEditor;
import com.bizdata.commons.utils.JqGridPageVO;
import com.bizdata.commons.utils.JqGridPageVO2JpaPageParamVO;
import com.bizdata.commons.utils.JqGridSortVO;
import com.bizdata.commons.utils.JqGridSortVO2JpaSortParamVO;
import com.bizdata.commons.utils.StringEditor;
import com.bizdata.commons.utils.StringEscapeHtml;
import com.bizdata.commons.utils.StringUnescapeHtml;
import com.bizdata.config.FileUploadConfig;

import me.sdevil507.resp.ResultUtil;
import me.sdevil507.resp.ResultVO;
import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaPageResultVO;
import me.sdevil507.vo.JpaSortParamVO;

@Controller
@RequestMapping(value = "/admin/welfare_firm")
public class WelfareFirmController {
	private final JqGridPageVO2JpaPageParamVO jqGridPageVO2JpaPageParamVO;

	private final JqGridSortVO2JpaSortParamVO jqGridSortVO2JpaSortParamVO;

	private final WelfareFirmService welfareFirmService;

	private final WelfareTypeService welfareTypeService;

	private final WelfareOrderService welfareOrderService;
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		// 对于需要转换为Date类型的属性，使用DateEditor进行处理
		binder.registerCustomEditor(Date.class, new DateEditor());
		//String类型html转义
        binder.registerCustomEditor(String.class, new StringEditor());
	}
	@Autowired
	private FileUploadConfig fileUploadConfig;
	@Autowired
	public WelfareFirmController(JqGridPageVO2JpaPageParamVO jqGridPageVO2JpaPageParamVO,
			JqGridSortVO2JpaSortParamVO jqGridSortVO2JpaSortParamVO, WelfareFirmService welfareFirmService,
			WelfareTypeService welfareTypeService, WelfareOrderService welfareOrderService) {
		this.jqGridPageVO2JpaPageParamVO = jqGridPageVO2JpaPageParamVO;
		this.jqGridSortVO2JpaSortParamVO = jqGridSortVO2JpaSortParamVO;
		this.welfareFirmService = welfareFirmService;
		this.welfareTypeService = welfareTypeService;
		this.welfareOrderService = welfareOrderService;
	}

	/**
	 * 展示对应模板页面
	 *
	 * @return 模板页面
	 */
	@RequiresPermissions("welfareFirm:view")
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView view() {
		Map<Integer, String> welfareTypes = welfareTypeService.findNameAndId();
		return new ModelAndView("app_page/welfare/firm/list", "welfareTypes", welfareTypes);
	}

	/**
	 * 分页查询记录
	 *
	 * @param jqGridPageVO
	 *            分页参数
	 * @param jqGridSortVO
	 *            排序参数
	 * @param sleepSearchVO
	 *            查询参数
	 * @return 分页记录
	 */
	@RequiresPermissions("welfareFirm:view")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public JpaPageResultVO<WelfareFirm, WelfareFirm> list(JqGridPageVO jqGridPageVO, JqGridSortVO jqGridSortVO,
			SearchWelfareFirmVO searchWelfareFirmVO) {
		JpaPageParamVO jpaPageParamVO = jqGridPageVO2JpaPageParamVO.convert(jqGridPageVO);
		JpaSortParamVO jpaSortParamVO = jqGridSortVO2JpaSortParamVO.convert(jqGridSortVO);
		Page<WelfareFirm> welfareFirms = welfareFirmService.findAllByPage(jpaPageParamVO, jpaSortParamVO,
				searchWelfareFirmVO);
		for (WelfareFirm welfareFirm : welfareFirms) {
			String typeName = welfareTypeService.findOne(String.valueOf(welfareFirm.getWelfareType())).getTypeName();
			welfareFirm.setWelfareTypeName(typeName);
			Integer people = welfareOrderService.getPeopleByWelfare(welfareFirm.getId());
			if (null == people) {
				people = 0;
			}
			welfareFirm.setPeople(people);
		}
		return new JpaPageResultVO<WelfareFirm, WelfareFirm>(welfareFirms, WelfareFirm.class);
	}

	@RequestMapping(value = "/add_view", method = RequestMethod.GET)
	public ModelAndView addView() {
		Map<Integer, String> welfareTypes = welfareTypeService.findNameAndId();
		return new ModelAndView("app_page/welfare/firm/add", "welfareTypes", welfareTypes);
	}

	@RequestMapping(value = "/edit_view", method = RequestMethod.GET)
	public ModelAndView editView(String id) {
		WelfareFirm welfareFirm = welfareFirmService.findOne(id);
		WelfareFirmVO welfareFirmVO = new WelfareFirmVO();
		//编辑时转义
		welfareFirm = StringUnescapeHtml.unescapeHtml(welfareFirm,"content");
		welfareFirmVO.setWelfareFirm(welfareFirm);
		Map<Integer, String> welfareTypes = welfareTypeService.findNameAndId();
		welfareFirmVO.setWelfareTypes(welfareTypes);
		return new ModelAndView("app_page/welfare/firm/edit", "welfareFirmVO", welfareFirmVO);
	}

	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO saveOrUpdate(WelfareFirm welfareFirm) {
		ResultVO resultVO;
		//kindeditor图片路径无法设置为绝对路径，全部替换
		String s = welfareFirm.getContent();
        s = s.replaceAll(fileUploadConfig.getServerPrefix(), "/sunhealth").replaceAll("/sunhealth", fileUploadConfig.getServerPrefix());
        welfareFirm.setContent(s);
		if (welfareFirmService.save(welfareFirm)) {
			resultVO = ResultUtil.create(0, "welfareFirm保存成功!");
		} else {
			resultVO = ResultUtil.create(-1, "welfareFirm保存失败!");
		}
		return resultVO;
	}

	@RequestMapping(value = "/del", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO del(String id) {
		ResultVO resultVO;
		if (welfareFirmService.delete(id)) {
			resultVO = ResultUtil.create(0, "welfareFirm删除成功!");
		} else {
			resultVO = ResultUtil.create(-1, "welfareFirm删除失败!");
		}
		return resultVO;
	}

	@RequestMapping(value = "/top", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO top(String id) {
		ResultVO resultVO;
		WelfareFirm welfareFirm = welfareFirmService.findOne(id);
		String isTop = welfareFirm.getIsTop();
		if ("0".equals(isTop)) {
			isTop = "1";
			welfareFirm.setIsTop(isTop);
		} else if ("1".equals(isTop)) {
			isTop = "0";
			welfareFirm.setIsTop(isTop);
		}

		if (welfareFirmService.save(welfareFirm)) {
			resultVO = ResultUtil.create(0, "welfareFirm置顶成功!");
		} else {
			resultVO = ResultUtil.create(-1, "welfareFirm置顶失败!");
		}
		return resultVO;
	}
}
