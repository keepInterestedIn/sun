package com.bizdata.app.device.devicesource.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bizdata.app.device.devicesource.constant.AccreditTypeEnum;
import com.bizdata.app.device.devicesource.controller.vo.DeviceSourceVO;
import com.bizdata.app.device.devicesource.domain.DeviceSource;
import com.bizdata.app.device.devicesource.service.DeviceSourceService;
import com.bizdata.app.miaodetail.excleutil.ExcelRead;
import com.bizdata.app.miaodetail.excleutil.ExcelUtil;
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
@RequestMapping("/admin/devicesource")
public class DevicesourceController {

	private final JqGridPageVO2JpaPageParamVO jqGridPageVO2JpaPageParamVO;

	private final JqGridSortVO2JpaSortParamVO jqGridSortVO2JpaSortParamVO;

	private final DeviceSourceService deviceSourceService;

	private final FileUploadConfig fileUploadConfig;

	@Autowired
	public DevicesourceController(JqGridPageVO2JpaPageParamVO jqGridPageVO2JpaPageParamVO,
			JqGridSortVO2JpaSortParamVO jqGridSortVO2JpaSortParamVO, DeviceSourceService deviceSourceService,
			FileUploadConfig fileUploadConfig) {
		this.jqGridPageVO2JpaPageParamVO = jqGridPageVO2JpaPageParamVO;
		this.jqGridSortVO2JpaSortParamVO = jqGridSortVO2JpaSortParamVO;
		this.deviceSourceService = deviceSourceService;
		this.fileUploadConfig = fileUploadConfig;
	}

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		// 对于需要转换为Date类型的属性，使用DateEditor进行处理
		binder.registerCustomEditor(Date.class, new DateEditor());
		//String类型html转义
        binder.registerCustomEditor(String.class, new StringEditor());
	}

	@RequiresPermissions("source_device:view")
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView view() {
		return new ModelAndView("app_page/device/devicesource/list");
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView viewadd() {
		return new ModelAndView("app_page/device/devicesource/add");
	}

	@RequiresPermissions("source_device:view")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public JpaPageResultVO<DeviceSource, DeviceSource> list(JqGridPageVO jqGridPageVO, JqGridSortVO jqGridSortVO,
			DeviceSourceVO deviceSourceVO) {
		JpaPageParamVO jpaPageParamVO = jqGridPageVO2JpaPageParamVO.convert(jqGridPageVO);
		JpaSortParamVO jpaSortParamVO = jqGridSortVO2JpaSortParamVO.convert(jqGridSortVO);
		Page<DeviceSource> deviceSources = deviceSourceService.findAllByPage(jpaPageParamVO, jpaSortParamVO,
				deviceSourceVO);
		return new JpaPageResultVO<>(deviceSources, DeviceSource.class);
	}

	@RequiresPermissions("source_device:view")
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public ResultVO add(DeviceSource deviceSource) {
		ResultVO resultVO;
		String logo = deviceSource.getDeviceLogo();
		if (logo.indexOf("http:") == -1) {
			deviceSource.setDeviceLogo(fileUploadConfig.getServerPrefix() + deviceSource.getDeviceLogo()); // 璁剧疆鍓嶇紑
		}
		String[] nums = deviceSource.getFunctionType().split(",");
		if (nums.length != 0) {
			int sum = 0;
			for (int i = 0; i < nums.length; i++) {
				sum += (int) Math.pow(2, Integer.valueOf(nums[i]) - 1);
			}
			deviceSource.setFunctionType(String.valueOf(sum));
		}
		if (deviceSourceService.save(deviceSource)) {
			resultVO = ResultUtil.create(0, "deviceSource保存成功!");
		} else {
			resultVO = ResultUtil.create(-1, "deviceSource保存失败!");
		}
		return resultVO;
	}

	@RequiresPermissions("source_device:view")
	@RequestMapping(value = "/edit_view", method = RequestMethod.GET)
	public ModelAndView readOne(String id) {
		DeviceSource deviceSource = deviceSourceService.findOne(id);
		String functionType = deviceSource.getFunctionType();
		Integer num = 0;
		if (StringUtils.isNumeric(functionType)) {
			List<Boolean> isChecked = new ArrayList<Boolean>();
			num = Integer.valueOf(functionType);
			String temp = Integer.toBinaryString(num);
			char[] c = temp.toCharArray();
			for (int i = 0; i < 8; i++) {
				if (i < c.length) {
					if ('1' == c[c.length - i - 1]) {
						isChecked.add(true);
					} else {
						isChecked.add(false);
					}
				} else {
					isChecked.add(false);
				}
			}
			deviceSource.setIsChecked(isChecked);
		}
		//编辑时转义
		deviceSource = StringUnescapeHtml.unescapeHtml(deviceSource);
		return new ModelAndView("app_page/device/devicesource/edit", "deviceSource", deviceSource);
	}

	@RequiresPermissions("source_device:view")
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public boolean XlsUpload(@RequestParam(value = "excelFile", required = false) MultipartFile file) {
		boolean result = false;
		if (file == null) {
			return result;
		}
		String name = file.getOriginalFilename();
		long size = file.getSize();
		if (name == null || ExcelUtil.EMPTY.equals(name) && size == 0) {
			return result;
		}
		try {
			List<ArrayList<String>> list = new ExcelRead().readExcel(file, 9);
			for (int i = 0; i < list.size(); i++) {
				String deviceNo = list.get(i).get(0);
				String link_type = list.get(i).get(1);
				String connect_type = list.get(i).get(2);
				String device_name = list.get(i).get(3);
				String device_des = list.get(i).get(4);
				String des_url = list.get(i).get(5);
				String type_id = list.get(i).get(6);
				String logo = list.get(i).get(7);
				String functional_type = list.get(i).get(8);

				DeviceSource deviceSource = new DeviceSource();
				deviceSource.setDeviceNo(deviceNo);
				deviceSource.setLinkType(link_type);
				deviceSource.setConnectionType(connect_type);
				deviceSource.setDeviceName(device_name);
				deviceSource.setDescription(device_des);
				deviceSource.setDeviceLogo(logo);
				deviceSource.setFunctionType(functional_type);
				deviceSource.setDescriptionUrl(des_url);
				deviceSource.setTypeId(type_id);
				deviceSource.setAccreditType(AccreditTypeEnum.NULL);

				String functionType = "";
				Integer num = 0;
				num = Integer.valueOf(functional_type);
				String temp = Integer.toBinaryString(num);
				char[] c = temp.toCharArray();
				for (int j = 0; j < 8; j++) {
					if (j < c.length) {
						if ('1' == c[c.length - j - 1]) {
							functionType += (j + 1) + ",";
						}
					}
				}
				if (functionType.length() > 0) {
					functionType = functionType.substring(0, functionType.lastIndexOf(",")); // 鎴彇瀛楃涓�
					deviceSource.setFunctionTypeDes(functionType);
				}
				deviceSourceService.save(deviceSource); // 淇濆瓨
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequiresPermissions("source_device:view")
	@RequestMapping(value = "/fileDownLoad", method = RequestMethod.GET)
	@ResponseBody
	public void fileDownLoad(HttpServletResponse response) throws Exception {
		@SuppressWarnings("resource")
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("open_device");
		HSSFRow row = sheet.createRow((int) 0);
		HSSFCellStyle style = wb.createCellStyle();
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("device_sn");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("link_type");
		cell.setCellStyle(style);
		cell = row.createCell((short) 2);
		cell.setCellValue("connect_type");
		cell.setCellStyle(style);
		cell = row.createCell((short) 3);
		cell.setCellValue("device_name");
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);
		cell.setCellValue("device_des");
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);
		cell.setCellValue("des_url");
		cell.setCellStyle(style);
		cell = row.createCell((short) 6);
		cell.setCellValue("type_id");
		cell.setCellStyle(style);
		cell = row.createCell((short) 7);
		cell.setCellValue("logo");
		cell.setCellStyle(style);
		cell = row.createCell((short) 8);
		cell.setCellValue("functional_type");
		cell.setCellStyle(style);
		List<DeviceSource> deviceSources = deviceSourceService.findAll();
		if (deviceSources != null) {
			for (int i = 0; i < deviceSources.size(); i++) {
				row = sheet.createRow((int) i + 1);
				DeviceSource deviceSource = deviceSources.get(i);
				row.createCell((short) 0).setCellValue(deviceSource.getDeviceNo());
				row.createCell((short) 1).setCellValue(deviceSource.getLinkType());
				row.createCell((short) 2).setCellValue(deviceSource.getConnectionType());
				row.createCell((short) 3).setCellValue(deviceSource.getDeviceName());
				row.createCell((short) 4).setCellValue(deviceSource.getDescription());
				row.createCell((short) 5).setCellValue(deviceSource.getDescriptionUrl());
				row.createCell((short) 6).setCellValue(deviceSource.getTypeId());
				row.createCell((short) 7).setCellValue(deviceSource.getDeviceLogo());
				row.createCell((short) 8).setCellValue(deviceSource.getFunctionType());
				cell = row.createCell((short) 9);
			}
		}
		try {
			OutputStream output = response.getOutputStream();
			response.setHeader("Content-disposition", "attachment; filename=" + "open_device" + ".xls");
			response.setContentType("application/msexcel");
			wb.write(output);
			output.flush();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
