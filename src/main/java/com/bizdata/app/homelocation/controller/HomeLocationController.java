package com.bizdata.app.homelocation.controller;

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

import com.bizdata.app.banner.domain.Banner;
import com.bizdata.app.content.article.domain.Article;
import com.bizdata.app.content.column.domain.Columnart;
import com.bizdata.app.homelocation.controller.vo.SearchHomeLocationVO;
import com.bizdata.app.homelocation.domain.HomeLocation;
import com.bizdata.app.homelocation.service.HomeLocationService;
import com.bizdata.commons.utils.DateEditor;
import com.bizdata.commons.utils.JqGridPageVO;
import com.bizdata.commons.utils.JqGridPageVO2JpaPageParamVO;
import com.bizdata.commons.utils.JqGridSortVO;
import com.bizdata.commons.utils.JqGridSortVO2JpaSortParamVO;
import com.bizdata.commons.utils.StringEditor;
import com.bizdata.commons.utils.StringUnescapeHtml;
import com.bizdata.config.FileUploadConfig;

import me.sdevil507.resp.ResultUtil;
import me.sdevil507.resp.ResultVO;
import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaPageResultVO;
import me.sdevil507.vo.JpaSortParamVO;

@Controller
@RequestMapping("/admin/home_location")
public class HomeLocationController {
	private final HomeLocationService homeLocationService;

	private final JqGridPageVO2JpaPageParamVO jqGridPageVO2JpaPageParamVO;

	private final JqGridSortVO2JpaSortParamVO jqGridSortVO2JpaSortParamVO;
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		// 对于需要转换为Date类型的属性，使用DateEditor进行处理
		binder.registerCustomEditor(Date.class, new DateEditor());
		//String类型html转义
        binder.registerCustomEditor(String.class, new StringEditor());
	}

	private final FileUploadConfig fileUploadConfig;

	@Autowired
	public HomeLocationController(JqGridPageVO2JpaPageParamVO jqGridPageVO2JpaPageParamVO,
			JqGridSortVO2JpaSortParamVO jqGridSortVO2JpaSortParamVO, HomeLocationService homeLocationService,FileUploadConfig fileUploadConfig) {
		this.jqGridPageVO2JpaPageParamVO = jqGridPageVO2JpaPageParamVO;
		this.jqGridSortVO2JpaSortParamVO = jqGridSortVO2JpaSortParamVO;
		this.homeLocationService = homeLocationService;
		this.fileUploadConfig = fileUploadConfig;
	}

	/**
	 * 展示对应模板页面
	 *
	 * @return 模板页面
	 */
	@RequiresPermissions("home_location:view")
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView view() {
		return new ModelAndView("app_page/homelocation/list");
	}

	@RequiresPermissions("home_location:view")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public JpaPageResultVO list(JqGridPageVO jqGridPageVO, JqGridSortVO jqGridSortVO,
			SearchHomeLocationVO searchHomeLocationVO) {
		JpaPageParamVO jpaPageParamVO = jqGridPageVO2JpaPageParamVO.convert(jqGridPageVO);
		JpaSortParamVO jpaSortParamVO = jqGridSortVO2JpaSortParamVO.convert(jqGridSortVO);
		Page<HomeLocation> homeLocation = homeLocationService.findAllByPage(jpaPageParamVO, jpaSortParamVO,
				searchHomeLocationVO);
		for (HomeLocation home : homeLocation) {
			home.setLocationPicture(fileUploadConfig.getServerPrefix() + home.getLocationPicture());
        }
		return new JpaPageResultVO<>(homeLocation, HomeLocation.class);
	}
	@RequestMapping(value = "/edit_view", method = RequestMethod.GET)
    public ModelAndView editView(String id) {
        System.out.println(id);
        HomeLocation home = homeLocationService.findOne(id);
        ModelAndView mv = new ModelAndView();
      //编辑时转义
        home = StringUnescapeHtml.unescapeHtml(home);
        mv.addObject("url", fileUploadConfig.getServerPrefix());
        mv.addObject("home", home);
        mv.setViewName("app_page/homelocation/edit");
        return mv;
    }
	@RequestMapping(value = "/add_view", method = RequestMethod.GET)
    public ModelAndView addView() {
		Integer locationid = homeLocationService.getMaxid();
		locationid = locationid == null?0:locationid;
        return new ModelAndView("app_page/homelocation/add","home",locationid+1);
    }
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO saveOrUpdate(HomeLocation home) {
        ResultVO resultVO;
        if(home.getId() == null && homeLocationService.findOneByLocationid(home.getLocationId()) != null){
        	return ResultUtil.create(-1, "保存失败!");
        }
        if (homeLocationService.save(home)) {
            resultVO = ResultUtil.create(0, "保存成功!");
        } else {
            resultVO = ResultUtil.create(-1, "保存失败!");
        }
        return resultVO;
        
    }
	/**
     * 检查sort
     *
     * @param 栏目 实体
     * @return 执行反馈
     */
    @RequestMapping(value = "/checkSort", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkSort(int locationNo,String id) {
    	HomeLocation home = homeLocationService.findBylocationNo(locationNo);
    	
    	System.out.println("s:"+id);
    	if(home != null && !home.getId().equals(id)){
    		System.out.println("---"+home.getId());
    		return false;
    	}
    	return true;
    }
}
