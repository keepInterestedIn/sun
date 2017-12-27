package com.bizdata.app.welfare.order.controller;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

import com.bizdata.app.maguser.domain.UserProfile;
import com.bizdata.app.maguser.service.MagUserService;
import com.bizdata.app.maguser.service.UserProfileService;
import com.bizdata.app.welfare.order.controller.vo.SearchWelfareOrderVO;
import com.bizdata.app.welfare.order.domain.WelfareOrder;
import com.bizdata.app.welfare.order.service.WelfareOrderService;
import com.bizdata.app.welfare.type.service.WelfareTypeService;
import com.bizdata.commons.utils.DateEditor;
import com.bizdata.commons.utils.JqGridPageVO;
import com.bizdata.commons.utils.JqGridPageVO2JpaPageParamVO;
import com.bizdata.commons.utils.JqGridSortVO;
import com.bizdata.commons.utils.JqGridSortVO2JpaSortParamVO;
import com.bizdata.commons.utils.StringEditor;

import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaPageResultVO;
import me.sdevil507.vo.JpaSortParamVO;

@Controller
@RequestMapping(value = "/admin/welfare_order")
public class WelfareOrderController {

    private final JqGridPageVO2JpaPageParamVO jqGridPageVO2JpaPageParamVO;

    private final JqGridSortVO2JpaSortParamVO jqGridSortVO2JpaSortParamVO;

    private final WelfareOrderService welfareOrderService;

    private final MagUserService magUserService; // 获取手机号

    private final UserProfileService userProfileService; // 获取昵称、姓名

    private final WelfareTypeService welfareTypeService; // 获取福利类型名称

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        // 对于需要转换为Date类型的属性，使用DateEditor进行处理
        binder.registerCustomEditor(Date.class, new DateEditor());
        //String类型html转义
        binder.registerCustomEditor(String.class, new StringEditor());
    }

    @Autowired
    public WelfareOrderController(JqGridPageVO2JpaPageParamVO jqGridPageVO2JpaPageParamVO,
                                  JqGridSortVO2JpaSortParamVO jqGridSortVO2JpaSortParamVO, WelfareOrderService welfareOrderService,
                                  MagUserService magUserService, UserProfileService userProfileService,
                                  WelfareTypeService welfareTypeService) {
        this.jqGridPageVO2JpaPageParamVO = jqGridPageVO2JpaPageParamVO;
        this.jqGridSortVO2JpaSortParamVO = jqGridSortVO2JpaSortParamVO;
        this.welfareOrderService = welfareOrderService;
        this.magUserService = magUserService;
        this.userProfileService = userProfileService;
        this.welfareTypeService = welfareTypeService;
    }

    /**
     * 展示对应模板页面
     *
     * @return 模板页面
     */
    @RequiresPermissions("welfareOrder:view")
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView view() {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<Integer, String> welfareTypes = welfareTypeService.findNameAndId();
        Map<String, String> idTypes = this.getIdType();
        map.put("welfareTypes", welfareTypes);
        map.put("idTypes", idTypes);
        return new ModelAndView("app_page/welfare/order/list", map);
    }

    @RequiresPermissions("welfareOrder:view")
    @RequestMapping(value = "/showdetail", method = RequestMethod.GET)
    public ModelAndView showdetail(String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<Integer, String> welfareTypes = welfareTypeService.findNameAndId();
        Map<String, String> idTypes = this.getIdType();
        map.put("welfareId", id);
        map.put("welfareTypes", welfareTypes);
        map.put("idTypes", idTypes);
        return new ModelAndView("app_page/welfare/order/list", map);
    }

    @RequiresPermissions("welfareOrder:view")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JpaPageResultVO<WelfareOrder, WelfareOrder> list(JqGridPageVO jqGridPageVO, JqGridSortVO jqGridSortVO,
                                                            SearchWelfareOrderVO searchWelfareOrderVO) {
        JpaPageParamVO jpaPageParamVO = jqGridPageVO2JpaPageParamVO.convert(jqGridPageVO);
        JpaSortParamVO jpaSortParamVO = jqGridSortVO2JpaSortParamVO.convert(jqGridSortVO);
        Page<WelfareOrder> welfareOrderes = welfareOrderService.findAllByPage(jpaPageParamVO, jpaSortParamVO,
                searchWelfareOrderVO);
        // 补上缺少的字段
        for (WelfareOrder welfareOrder : welfareOrderes) {
            String userId = welfareOrder.getUserId();
            String welfareTypeId = welfareOrder.getWelfareType();
            String mobile = magUserService.getMobileById(userId);
            UserProfile userInfo = userProfileService.findByAccountId(userId);
            if (userInfo != null) {
                String nickName = userInfo.getNickName(); // 昵称
                welfareOrder.setNickName(nickName);
            }
            welfareOrder.setMobile(mobile);
            String welfareTypeName = welfareTypeService.getTypeNameById(welfareTypeId);
            welfareOrder.setWelfareTypeName(welfareTypeName);
        }
        return new JpaPageResultVO<WelfareOrder, WelfareOrder>(welfareOrderes, WelfareOrder.class);
    }

    @RequiresPermissions("welfareOrder:view")
    @RequestMapping(value = "/fileDownLoad", method = RequestMethod.GET)
    @ResponseBody
    public void fileDownLoad(HttpServletResponse response, SearchWelfareOrderVO searchWelfareOrderVO) throws Exception {
        // 第一步，创建一个webbook，对应一个Excel文件
        @SuppressWarnings("预约详情")
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("预约详情");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("用户昵称");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue("用户姓名");
        cell.setCellStyle(style);
        cell = row.createCell((short) 2);
        cell.setCellValue("手机号");
        cell.setCellStyle(style);
        cell = row.createCell((short) 3);
        cell.setCellValue("证件类型");
        cell.setCellStyle(style);
        cell = row.createCell((short) 4);
        cell.setCellValue("证件号");
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);
        cell.setCellValue("预约类型");
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);
        cell.setCellValue("城市");
        cell.setCellStyle(style);
        cell = row.createCell((short) 7);
        cell.setCellValue("医院");
        cell.setCellStyle(style);
        cell = row.createCell((short) 8);
        cell.setCellValue("科室");
        cell.setCellStyle(style);
        cell = row.createCell((short) 9);
        cell.setCellValue("怀孕周数");
        cell.setCellStyle(style);
        cell = row.createCell((short) 10);
        cell.setCellValue("医生");
        cell.setCellStyle(style);
        cell = row.createCell((short) 11);
        cell.setCellValue("就诊时间");
        cell.setCellStyle(style);
        cell = row.createCell((short) 12);
        cell.setCellValue("申请时间");
        cell.setCellStyle(style);
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到
        List<WelfareOrder> WelfareOrders = welfareOrderService.findAll(searchWelfareOrderVO);


        if (WelfareOrders != null) {
            // 添加一些没有的字段
            for (WelfareOrder welfareOrder : WelfareOrders) {
                String userId = welfareOrder.getUserId();
                String welfareTypeId = welfareOrder.getWelfareType();
                String mobile = magUserService.getMobileById(userId);
                UserProfile userInfo = userProfileService.findByAccountId(userId);
                String nickName = userInfo.getNickName(); // 昵称
                welfareOrder.setMobile(mobile); // 手机号
                welfareOrder.setNickName(nickName); //
                String welfareTypeName = welfareTypeService.getTypeNameById(welfareTypeId);
                welfareOrder.setWelfareTypeName(welfareTypeName);
                // 修改证件号码
                String idType = welfareOrder.getIdType();
                //
                if (idType != null) {
                    String tempIdType = "";
                    switch (idType) {
                        case "10":
                            tempIdType = "居民身份证";
                            break;
                        case "11":
                            tempIdType = "居民户口簿";
                            break;
                        case "12":
                            tempIdType = "驾驶证";
                            break;
                        case "13":
                            tempIdType = "军官证";
                            break;
                        case "16":
                            tempIdType = "异常身份证";
                            break;
                        case "17":
                            tempIdType = "台湾居民来往大陆通行证";
                            break;
                        case "19":
                            tempIdType = "港澳居民来往内地通行证";
                            break;
                        case "21":
                            tempIdType = "组织机构代码证";
                            break;
                        case "51":
                            tempIdType = "护照";
                            break;
                        case "99":
                            tempIdType = "其他证件";
                            break;
                    }
                    welfareOrder.setIdType(tempIdType);
                }
            }

            for (int i = 0; i < WelfareOrders.size(); i++) {
                row = sheet.createRow((int) i + 1);
                WelfareOrder welfareOrder = WelfareOrders.get(i);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // 第四步，创建单元格，并设置值
                row.createCell((short) 0).setCellValue(welfareOrder.getNickName());
                row.createCell((short) 1).setCellValue(welfareOrder.getRealName());
                row.createCell((short) 2).setCellValue(welfareOrder.getMobile());
                row.createCell((short) 3).setCellValue(welfareOrder.getIdType());
                row.createCell((short) 4).setCellValue(welfareOrder.getIdNum());
                row.createCell((short) 5).setCellValue(welfareOrder.getWelfareTypeName());
                row.createCell((short) 6).setCellValue(welfareOrder.getCity());
                row.createCell((short) 7).setCellValue(welfareOrder.getHospital());
                row.createCell((short) 8).setCellValue(welfareOrder.getDepartments());
                row.createCell((short) 9).setCellValue(welfareOrder.getFetationNum());
                row.createCell((short) 10).setCellValue(welfareOrder.getDoctor());
                row.createCell((short) 11).setCellValue(
                        welfareOrder.getSeeDoctorDate() == null ? "" : sdf.format(welfareOrder.getSeeDoctorDate()));
                row.createCell((short) 12).setCellValue(sdf.format(welfareOrder.getCreateDate()));
                cell = row.createCell((short) 13);
            }
        }
        // 第六步，将文件存到指定位置
        try {
            OutputStream output = response.getOutputStream();
            response.setHeader("Content-disposition", "attachment; filename=" + java.net.URLEncoder.encode("预约详情" + ".xls", "UTF-8"));
            response.setContentType("application/msexcel");
            wb.write(output);
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取员工证件类型
     *
     * @return
     */
    private Map<String, String> getIdType() {
        Map<String, String> idTypes = new HashMap<String, String>();

        idTypes.put("10", "居民身份证");
        idTypes.put("11", "居民户口簿");
        idTypes.put("12", "驾驶证");
        idTypes.put("13", "军官证");
        idTypes.put("16", "异常身份证");
        idTypes.put("17", "台湾居民来往大陆通行证");
        idTypes.put("19", "港澳居民来往内地通行证");
        idTypes.put("21", "组织机构代码证");
        idTypes.put("51", "护照");
        idTypes.put("99", "其他证件");

        return idTypes;
    }
}
