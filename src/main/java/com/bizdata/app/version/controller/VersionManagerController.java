package com.bizdata.app.version.controller;

import com.bizdata.app.version.controller.vo.SearchVersionManagerVO;
import com.bizdata.app.version.domain.VersionManager;
import com.bizdata.app.version.service.VersionManagerService;
import com.bizdata.commons.utils.*;
import me.sdevil507.resp.ResultUtil;
import me.sdevil507.resp.ResultVO;
import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaPageResultVO;
import me.sdevil507.vo.JpaSortParamVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author w
 */
@RestController
@RequestMapping("/admin/version")
public class VersionManagerController {
    private final JqGridSortVO2JpaSortParamVO jqGridSortVO2JpaSortParamVO;

    private final JqGridPageVO2JpaPageParamVO jqGridPageVO2JpaPageParamVO;

    private final VersionManagerService versionManagerService;

    @Autowired
    public VersionManagerController(JqGridPageVO2JpaPageParamVO jqGridPageVO2JpaPageParamVO,
                                    JqGridSortVO2JpaSortParamVO jqGridSortVO2JpaSortParamVO, VersionManagerService versionManagerService) {
        this.jqGridSortVO2JpaSortParamVO = jqGridSortVO2JpaSortParamVO;
        this.jqGridPageVO2JpaPageParamVO = jqGridPageVO2JpaPageParamVO;
        this.versionManagerService = versionManagerService;
    }

    @InitBinder()
    protected void initBinderNew(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        System.out.println(request.getAuthType());
        System.out.println(request.getParameter("versionId"));
        // 对于需要转换为Date类型的属性，使用DateEditor进行处理
        binder.registerCustomEditor(Date.class, new DateEditor());
        // 对字符串进行HTML转义
        binder.registerCustomEditor(String.class, new StringEditor());
    }

    @RequiresPermissions("version_manager:view")
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView view() {
        return new ModelAndView("app_page/version/list");
    }

    @RequiresPermissions("version_manager:view")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JpaPageResultVO<VersionManager, VersionManager> list(JqGridPageVO jqGridPageVO, JqGridSortVO jqGridSortVO, SearchVersionManagerVO searchVersionManagerVO) {
        JpaPageParamVO jpaPageParamVO = jqGridPageVO2JpaPageParamVO.convert(jqGridPageVO);
        JpaSortParamVO jpaSortParamVO = jqGridSortVO2JpaSortParamVO.convert(jqGridSortVO);
        Page<VersionManager> versionManagers = versionManagerService.findAllByPage(jpaPageParamVO, jpaSortParamVO, searchVersionManagerVO);
        return new JpaPageResultVO<>(versionManagers, VersionManager.class);
    }

    @RequestMapping(value = "/add_view", method = RequestMethod.GET)
    public ModelAndView addView() {
        return new ModelAndView("app_page/version/add");
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO saveOrUpdate(VersionManager versionManager) {
        ResultVO resultVO;
        if (versionManagerService.save(versionManager)) {
            resultVO = ResultUtil.create(0, "versionManager保存成功!");
        } else {
            resultVO = ResultUtil.create(-1, "versionManager保存失败!");
        }
        return resultVO;
    }

    @RequestMapping(value = "/edit_view", method = RequestMethod.GET)
    public ModelAndView editView(String id) {
        VersionManager versionManager = versionManagerService.findOne(id);
        versionManager = StringUnescapeHtml.unescapeHtml(versionManager);
        ModelAndView mv = new ModelAndView();
        mv.addObject("versionManager", versionManager);
        mv.setViewName("app_page/version/edit");
        return mv;
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO del(String id) {
        ResultVO resultVO;

        if (versionManagerService.delById(id)) {
            resultVO = ResultUtil.create(0, "versionManager删除成功!");
        } else {
            resultVO = ResultUtil.create(-1, "versionManager删除失败!");
        }
        return resultVO;
    }

    /**
     * 上下线功能
     *
     * @param id
     * @return ResultVO 一个结果对象
     */
    @RequestMapping(value = "/down", method = RequestMethod.POST)
    @ResponseBody
    public ResultVO down(String id) {
        ResultVO resultVO;
        VersionManager versionManager = versionManagerService.findOne(id);
        Integer status = versionManager.getStatus();
        if (null != status) {
            if (1 == status) {
                status = 0;
            } else {
                status = 1;
            }
            versionManager.setStatus(status);
        }
        if (versionManagerService.save(versionManager)) {
            resultVO = ResultUtil.create(0, "versionManager状态修改成功!");
        } else {
            resultVO = ResultUtil.create(-1, "versionManager状态修改失败!");
        }
        return resultVO;
    }
}
