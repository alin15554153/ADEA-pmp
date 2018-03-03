
package com.anycc.pmp.rsmt.controller;


import com.anycc.common.dto.*;
import com.anycc.log.Log;
import com.anycc.log.LogMessageObject;
import com.anycc.log.impl.LogUitls;
import com.anycc.pmp.rsmt.entity.Resource;
import com.anycc.pmp.rsmt.service.ResourceService;
import com.anycc.util.persistence.DynamicSpecifications;
import com.anycc.utils.FileUtils;
import com.anycc.utils.Identities;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/management/pmp/rsmt/resource")
public class ResourceController {

	@Autowired
	private ResourceService resourceService;

	private static final String LIST = "management/pmp/rsmt/resource/list";
	
	@InitBinder
	public void dataBinder(WebDataBinder dataBinder) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		dataBinder.registerCustomEditor(Date.class, 
				new CustomDateEditor(df, true));
	}
	
	
	/**
	 * 跳转到resource管理页面
	 * 
	 * @return
	 */
	@RequiresPermissions("PMP_RSMT_Resource:view")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String index() {
		return LIST;
	}
	
	/**
	 * 分页查询resource列表
	 * 
	 * @return
	 */
	@RequiresPermissions("PMP_RSMT_Resource:view")
	@RequestMapping(value = "queryByPaging", method = RequestMethod.POST)
	@ResponseBody
	public DTData<Resource> queryByPaging(ServletRequest request, DTPager pager) {
		Specification<Resource> specification = DynamicSpecifications.bySearchFilter(request, Resource.class);
		return resourceService.queryByPaging(specification, pager);
	}
	
	/**
	 * 添加resource
	 * 
	 * @param resource
	 * @return
	 */
	@Log(message="添加了id={0}的资源维护。")
	@RequiresPermissions("PMP_RSMT_Resource:save")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Response add(Resource resource) {
		resourceService.addResource(resource);
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{resource.getId()}));
		return new SuccessResponse();
	}
	

	/**
	 * 根据id查询信息
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("PMP_RSMT_Resource:view")
	@RequestMapping(value = "{id}/view", method = RequestMethod.GET)
	@ResponseBody
	public Response queryById(@PathVariable("id") String id) {
		return resourceService.queryById(id);
	}

	/**
	 * 更新resource
	 * 
	 * @param resource
	 * @return
	 */
	@Log(message="修改了id={0}的资源维护的信息。")
	@RequiresPermissions("PMP_RSMT_Resource:edit")
	@RequestMapping(value = "{id}/update", method = RequestMethod.POST)
	@ResponseBody
	public Response update(Resource resource) {
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{resource.getId()}));
		return resourceService.updateResource(resource);
	}
	
	/**
	 * 删除resource
	 * 
	 * @param id
	 * @return
	 */
	@Log(message="删除了id={0}的资源维护。")
	@RequiresPermissions("PMP_RSMT_Resource:delete")
	@RequestMapping(value="{id}/delete", method=RequestMethod.POST)
	@ResponseBody
	public Response delete(@PathVariable String id) {
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{id}));
		return resourceService.delete(id);
	}

	/**
	 * 批量删除resource
	 * 
	 * @param idArray
	 * @return
	 */
	@Log(message="批量删除了id={0}资源维护。")
	@RequiresPermissions("PMP_RSMT_Resource:delete")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Response delete(@RequestBody String[] idArray) {
		for(String id : idArray){
			resourceService.delete(id);
		}
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{Arrays.toString(idArray)}));
		return new SuccessResponse();
		
	}

	@Log(message="上传了{0}。")
	@RequiresPermissions("PMP_RSMT_Resource:edit")
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public Response upload( HttpServletRequest request,@RequestParam("files") MultipartFile[] files,@RequestParam("company") String company,@RequestParam("procode") String procode  ) {

		HashMap<String, Object> result = new HashMap<String, Object>();
		for (MultipartFile file : files) {
			try {
				//String company ="11";
				//String procode ="11";//,company,procode
				String uuid = FileUtils.writeFileWithProject(request, file,company,procode);//写入磁盘文件,改文件名,无后缀
				String url = uuid+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				result.put("fileNames", file.getOriginalFilename());
				result.put("fileSize", file.getSize());
				result.put("uuid", uuid);
				result.put("url", url);
			} catch (Exception e) {
				return new FailedResponse();
			}
		}
		ObjectResponse<Map<String, Object>> obj=new ObjectResponse<Map<String, Object>>(result);
		return new ObjectResponse<Map<String, Object>>(result);
	}

	@RequiresPermissions("PMP_RSMT_Resource:view")
	@RequestMapping(value = "/delUploadFile", method = RequestMethod.POST)
	@ResponseBody
	public Response delUploadFile( HttpServletRequest request, @RequestBody String[] paths) {

		for (String path : paths) {
			FileUtils.deleteFile(request, path);
		}
		return new SuccessResponse();
	}

	private ResponseEntity<String> upload2File(MultipartFile file, String uploadPath) {
		// 检查扩展名
		String fileName = file.getOriginalFilename();
		String fileExt = FileUtils.getFileExt(fileName);

		String uuid = Identities.uuid2();
		String newFileName = uuid + "." + fileExt;

		File uploadedFile = new File(uploadPath, newFileName);
		try {
			org.apache.commons.io.FileUtils.writeByteArrayToFile(uploadedFile, file.getBytes());

			Resource resource = new Resource();


			//resourceService.saveOrUpdate(resource);
			LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{fileName}));
		} catch (Exception e) {
			if (uploadedFile.exists()) {
				uploadedFile.delete();
			}

			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	private String getFileStorePath(HttpServletRequest request) {
		return request.getSession().getServletContext().getRealPath("/") + "/upload";
	}

}
