package com.bizdata.app.push.controller;

import java.util.Date;
import java.util.HashMap;
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

import com.bizdata.app.content.help.domain.Help;
import com.bizdata.app.information.domain.Information;
import com.bizdata.app.push.constant.PushEnum;
import com.bizdata.app.push.controller.vo.SearchPushVO;
import com.bizdata.app.push.domain.Push;
import com.bizdata.app.push.jpush.Jgpush;
import com.bizdata.app.push.service.PushService;
import com.bizdata.commons.utils.DateEditor;
import com.bizdata.commons.utils.JqGridPageVO;
import com.bizdata.commons.utils.JqGridPageVO2JpaPageParamVO;
import com.bizdata.commons.utils.JqGridSortVO;
import com.bizdata.commons.utils.JqGridSortVO2JpaSortParamVO;
import com.bizdata.commons.utils.StringEditor;
import com.bizdata.commons.utils.StringUnescapeHtml;
import com.bizdata.config.JpushConfig;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import me.sdevil507.resp.ResultUtil;
import me.sdevil507.resp.ResultVO;
import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaPageResultVO;
import me.sdevil507.vo.JpaSortParamVO;

@Controller
@RequestMapping("/admin/push")
public class PushController {
	private final PushService pushmanageService;

	private final JqGridPageVO2JpaPageParamVO jqGridPageVO2JpaPageParamVO;

	private final JqGridSortVO2JpaSortParamVO jqGridSortVO2JpaSortParamVO;
	@Autowired
    private JpushConfig jpushConfig;
	@Autowired
	public PushController(JqGridPageVO2JpaPageParamVO jqGridPageVO2JpaPageParamVO,
			JqGridSortVO2JpaSortParamVO jqGridSortVO2JpaSortParamVO, PushService pushmanageService) {
		this.jqGridPageVO2JpaPageParamVO = jqGridPageVO2JpaPageParamVO;
		this.jqGridSortVO2JpaSortParamVO = jqGridSortVO2JpaSortParamVO;
		this.pushmanageService = pushmanageService;
	}
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		// 对于需要转换为Date类型的属性，使用DateEditor进行处理
		binder.registerCustomEditor(Date.class, new DateEditor());
		//String类型html转义
        binder.registerCustomEditor(String.class, new StringEditor());
	}
	/**
	 * 展示对应模板页面
	 *
	 * @return 模板页面
	 */
	@RequiresPermissions("push:view")
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView view() {
		return new ModelAndView("app_page/push/list");
	}

	@RequiresPermissions("push:view")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public JpaPageResultVO list(JqGridPageVO jqGridPageVO, JqGridSortVO jqGridSortVO,
			SearchPushVO searchPushVO) {
		JpaPageParamVO jpaPageParamVO = jqGridPageVO2JpaPageParamVO.convert(jqGridPageVO);
		JpaSortParamVO jpaSortParamVO = jqGridSortVO2JpaSortParamVO.convert(jqGridSortVO);
		Page<Push> pushmanage = pushmanageService.findAllByPage(jpaPageParamVO, jpaSortParamVO,
				searchPushVO);
		return new JpaPageResultVO<>(pushmanage, Push.class);
	}
	
	/**
     * 新增记录页面
     *
     * @return 页面
     */
	@RequestMapping(value="/add_view",method=RequestMethod.GET)
	public ModelAndView addView() {
		return new ModelAndView("app_page/push/add");
	}
	/**
     * 根据ID跳转至编辑页面
     *
     * @param id id
     * @return 页面
     */
    @RequestMapping(value = "/edit_view", method = RequestMethod.GET)
    public ModelAndView editView(String id) {
        System.out.println(id);
        Push push = pushmanageService.findOne(id);
        //编辑时转义
        push = StringUnescapeHtml.unescapeHtml(push);
        return new ModelAndView("app_page/push/edit", "push", push);
    }
	/**
	 * 新增推送消息方法
	 * @param information 入参VO
	 * @return 执行反馈
	 */
	@RequestMapping(value="/saveOrUpdate",method=RequestMethod.POST)
	@ResponseBody
	public ResultVO addInformation(Push push) {
		ResultVO resultVO;
		String[] ads = push.getAddAdress().split(",");
		if(push.getPushEnum().equals(PushEnum.H5SKIP)) {
			push.setAddAdress(ads[0]);
		}else if(push.getPushEnum().equals(PushEnum.NATIVESKOIP)) {
			push.setAddAdress(ads[1]);
		}else if(push.getPushEnum().equals(PushEnum.NOON)) {
			push.setAddAdress("");
		}
		if (pushmanageService.save(push)) {
            resultVO = ResultUtil.create(0, "保存消息成功!");
        } else {
            resultVO = ResultUtil.create(-1, "保存消息失败!");
        }
		return resultVO;
	}
	
	@RequestMapping(value = "/push", method = RequestMethod.GET)
	@ResponseBody
    public ResultVO push(String id) {
		ResultVO resultVO;
		Push push = pushmanageService.findOne(id);
		if(push.getResult().equals("1")) {
			resultVO = ResultUtil.create(-1, "推送无效!");
		}
		JPushClient jpushClient= new JPushClient(jpushConfig.getMasterSecret(),jpushConfig.getAppKey(), 3);
		Map<String,String> map = new HashMap<String,String>();
		map.put("addAdress", push.getAddAdress());
		PushPayload payload = Jgpush.buildPushObject_tag(push.getContent(),push.getTitle(),map);//配置发送内容
		PushResult result = Jgpush.testSendPush(jpushClient,payload);//发送给用户
		if(null!= result) {
			long s = result.msg_id;
			push.setMsg_id(s);
			push.setResult("1");
			pushmanageService.save(push);
            resultVO = ResultUtil.create(0, "推送成功!");
        } else {
            resultVO = ResultUtil.create(-1, "推送失败!");
        }
		return resultVO;
    }
}
