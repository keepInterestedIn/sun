package com.bizdata.app.shortcutkey.controller;

import java.util.Date;

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

import com.bizdata.app.shortcutkey.controller.vo.SearchShortcutKeyVO;
import com.bizdata.app.shortcutkey.domain.ShortcutKey;
import com.bizdata.app.shortcutkey.service.ShortcutKeyService;
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
@RequestMapping("/admin/shortcut_key")
public class ShortcutKeyController {
	private final ShortcutKeyService shortcutKeyService;

	private final JqGridPageVO2JpaPageParamVO jqGridPageVO2JpaPageParamVO;

	private final JqGridSortVO2JpaSortParamVO jqGridSortVO2JpaSortParamVO;
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		// 对于需要转换为Date类型的属性，使用DateEditor进行处理
		binder.registerCustomEditor(Date.class, new DateEditor());
		//String类型html转义
        binder.registerCustomEditor(String.class, new StringEditor());
	}
	@Autowired
	public ShortcutKeyController(JqGridPageVO2JpaPageParamVO jqGridPageVO2JpaPageParamVO,
			JqGridSortVO2JpaSortParamVO jqGridSortVO2JpaSortParamVO, ShortcutKeyService shortcutKeyService) {
		this.jqGridPageVO2JpaPageParamVO = jqGridPageVO2JpaPageParamVO;
		this.jqGridSortVO2JpaSortParamVO = jqGridSortVO2JpaSortParamVO;
		this.shortcutKeyService = shortcutKeyService;
	}

	/**
	 * 展示对应模板页面
	 *
	 * @return 模板页面
	 */
	@RequiresPermissions("shortcut_key:view")
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView view() {
		return new ModelAndView("app_page/shortcutkey/list");
	}

	@RequiresPermissions("shortcut_key:view")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public JpaPageResultVO<ShortcutKey, ShortcutKey> list(JqGridPageVO jqGridPageVO, JqGridSortVO jqGridSortVO,
			SearchShortcutKeyVO searchShortcutKeyVO) {
		System.out.println("--------------"+1);
		JpaPageParamVO jpaPageParamVO = jqGridPageVO2JpaPageParamVO.convert(jqGridPageVO);
		JpaSortParamVO jpaSortParamVO = jqGridSortVO2JpaSortParamVO.convert(jqGridSortVO);
		Page<ShortcutKey> shortcutKey = shortcutKeyService.findAllByPage(jpaPageParamVO, jpaSortParamVO,
				searchShortcutKeyVO);
		
		for (ShortcutKey stk: shortcutKey){
			if (stk.getTypes().equals("1"))
				stk.setTypes("在线咨询提问快捷词");
			else if (stk.getTypes().equals("2"))
				stk.setTypes("阳光加搜索快捷搜索词");
			else
				stk.setTypes("");
		}
		return new JpaPageResultVO<ShortcutKey, ShortcutKey>(shortcutKey, ShortcutKey.class);
	}
	
	/**
     * 跳转至新增页面
     *
     * @return 模板页面
     */
    @RequestMapping(value = "/add_view", method = RequestMethod.GET)
    public ModelAndView addView() {
        return new ModelAndView("app_page/shortcutkey/add");
    }
    
    @RequestMapping(value = "/edit_view", method = RequestMethod.GET)
    public ModelAndView editView(String id) {
        System.out.println(id);
        ShortcutKey shortcutKey = shortcutKeyService.findOne(id);
      //编辑时转义
        shortcutKey = StringUnescapeHtml.unescapeHtml(shortcutKey);
        return new ModelAndView("app_page/shortcutkey/edit", "shortcutKey", shortcutKey);
    }
    
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO saveOrUpdate(ShortcutKey shortcutKey) {
        ResultVO resultVO;
    	shortcutKey.setUpdateDate(new Date());
        if (shortcutKeyService.save(shortcutKey)) {
            resultVO = ResultUtil.create(0, "shortcutKey保存成功!");
        } else {
            resultVO = ResultUtil.create(-1, "shortcutKey保存失败!");
        }
        return resultVO;
    }
}
