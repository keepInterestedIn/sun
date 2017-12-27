package com.bizdata.app.welfare.type.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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

import com.bizdata.app.welfare.type.controller.vo.SearchWelfareVO;
import com.bizdata.app.welfare.type.domain.WelfareType;
import com.bizdata.app.welfare.type.service.WelfareTypeService;
import com.bizdata.commons.utils.DateEditor;
import com.bizdata.commons.utils.JqGridPageVO;
import com.bizdata.commons.utils.JqGridPageVO2JpaPageParamVO;
import com.bizdata.commons.utils.JqGridSortVO;
import com.bizdata.commons.utils.JqGridSortVO2JpaSortParamVO;
import com.bizdata.commons.utils.StringEditor;
import com.bizdata.commons.utils.StringUnescapeHtml;

import me.sdevil507.resp.ResultUtil;
import me.sdevil507.resp.ResultVO;
import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaPageResultVO;
import me.sdevil507.vo.JpaSortParamVO;

@Controller
@RequestMapping(value = "/admin/welfare_type")
public class WelfareTypeController {

	private final JqGridPageVO2JpaPageParamVO jqGridPageVO2JpaPageParamVO;

	private final JqGridSortVO2JpaSortParamVO jqGridSortVO2JpaSortParamVO;

	private final WelfareTypeService welfareTypeService;
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		// 对于需要转换为Date类型的属性，使用DateEditor进行处理
		binder.registerCustomEditor(Date.class, new DateEditor());
		//String类型html转义
        binder.registerCustomEditor(String.class, new StringEditor());
	}
	@Autowired
	public WelfareTypeController(JqGridPageVO2JpaPageParamVO jqGridPageVO2JpaPageParamVO,
			JqGridSortVO2JpaSortParamVO jqGridSortVO2JpaSortParamVO, WelfareTypeService welfareTypeService) {
		this.jqGridPageVO2JpaPageParamVO = jqGridPageVO2JpaPageParamVO;
		this.jqGridSortVO2JpaSortParamVO = jqGridSortVO2JpaSortParamVO;
		this.welfareTypeService = welfareTypeService;
	}

	/**
	 * 展示对应模板页面
	 *
	 * @return 模板页面
	 */
	@RequiresPermissions("welfareType:view")
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView view() {
		return new ModelAndView("app_page/welfare/type/list");
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
	@RequiresPermissions("welfareType:view")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public JpaPageResultVO<WelfareType, WelfareType> list(JqGridPageVO jqGridPageVO, JqGridSortVO jqGridSortVO,
			SearchWelfareVO searchWelfareVO) {
		JpaPageParamVO jpaPageParamVO = jqGridPageVO2JpaPageParamVO.convert(jqGridPageVO);
		JpaSortParamVO jpaSortParamVO = jqGridSortVO2JpaSortParamVO.convert(jqGridSortVO);
		Page<WelfareType> welfareTypes = welfareTypeService.findAllByPage(jpaPageParamVO, jpaSortParamVO,
				searchWelfareVO);
		return new JpaPageResultVO<WelfareType, WelfareType>(welfareTypes, WelfareType.class);
	}

	@RequestMapping(value = "/add_view", method = RequestMethod.GET)
	public ModelAndView addView() {
		return new ModelAndView("app_page/welfare/type/add");
	}

	@RequestMapping(value = "/edit_view", method = RequestMethod.GET)
	public ModelAndView editView(String id) {
		WelfareType welfareType = welfareTypeService.findOne(id);
		String functionType = welfareType.getCommit();
		String[] temp = functionType.split(",");
		List<Boolean> isChecked = new ArrayList<Boolean>();
		for (int i = 0; i < 7; i++) {
			boolean flag = false;
			for (String s : temp) {
				if (s.equals(String.valueOf(i + 1))) {
					flag = true;
					break;
				}
			}
			isChecked.add(flag);
		}
		//编辑时转义
		welfareType = StringUnescapeHtml.unescapeHtml(welfareType);
		welfareType.setIsChecked(isChecked);
		return new ModelAndView("app_page/welfare/type/edit", "welfareType", welfareType);
	}

	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO saveOrUpdate(WelfareType welfareType) {
		ResultVO resultVO;
		if (welfareTypeService.save(welfareType)) {
			resultVO = ResultUtil.create(0, "welfareType保存成功!");
		} else {
			resultVO = ResultUtil.create(-1, "welfareType保存失败!");
		}
		return resultVO;
	}

	@RequestMapping(value = "/del", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO del(String id) {
		ResultVO resultVO;
		if (welfareTypeService.delete(id)) {
			resultVO = ResultUtil.create(0, "welfareType删除成功!");
		} else {
			resultVO = ResultUtil.create(-1, "welfareType删除失败!");
		}
		return resultVO;
	}
}
