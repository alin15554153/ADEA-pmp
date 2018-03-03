
package ${pknController};


<#if hasDate == true>
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
</#if>

import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
<#if hasDate == true>
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
</#if>
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import net.hp.es.adm.healthcare.rphcp.util.persistence.DynamicSpecifications;
import net.hp.es.adm.healthcare.rphcp.log.Log;
import net.hp.es.adm.healthcare.rphcp.log.LogMessageObject;
import net.hp.es.adm.healthcare.rphcp.log.impl.LogUitls;
import ${pknEntity}.${className};
import ${pknService}.${className}Service;

import net.hp.es.adm.healthcare.rphcp.common.dto.*;


@Controller
@RequestMapping("/${requestMapping}")
public class ${className}Controller {

	@Autowired
	private ${className}Service ${instanceName}Service;
	private static final String LIST = "${requestMapping}/list";

	
	<#if hasDate == true>
	@InitBinder
	public void dataBinder(WebDataBinder dataBinder) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		dataBinder.registerCustomEditor(Date.class, 
				new CustomDateEditor(df, true));
	}
	
	</#if>
	
	/**
	 * 跳转到${instanceName}管理页面
	 * 
	 * @return
	 */
	@RequiresPermissions("${className}:view")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String index() {
		return LIST;
	}
	
	/**
	 * 分页查询${instanceName}列表
	 * 
	 * @return
	 */
	@RequiresPermissions("${className}:view")
	@RequestMapping(value = "queryByPaging", method = RequestMethod.POST)
	@ResponseBody
	public DTData<${className}> queryByPaging(${className} ${instanceName}, DTPager pager) {
		return ${instanceName}Service.queryByPaging(${instanceName}, pager);
	}
	
	/**
	 * 添加${instanceName}
	 * 
	 * @param ${instanceName}
	 * @return
	 */
	@Log(message="添加了id={0}的${functionName}。")
	@RequiresPermissions("${className}:save")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public Response add(${className} ${instanceName}) {
		${instanceName}Service.add${className}(${instanceName});
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{${instanceName}.getId()}));
		return new SuccessResponse();
	}
	

	/**
	 * 根据id查询信息
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("${className}:view")
	@RequestMapping(value = "{id}/view", method = RequestMethod.GET)
	@ResponseBody
	public Response queryById(@PathVariable("id") long id) {
		return ${instanceName}Service.queryById(id);
	}
	
	/**
	 * 更新${instanceName}
	 * 
	 * @param ${instanceName}
	 * @return
	 */
	@Log(message="修改了id={0}的${functionName}的信息。")
	@RequiresPermissions("${className}:edit")
	@RequestMapping(value = "{id}/update", method = RequestMethod.POST)
	@ResponseBody
	public Response update(${className} ${instanceName}) {
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{${instanceName}.getId()}));
		return ${instanceName}Service.update${className}(${instanceName});
	}
	
	/**
	 * 删除${instanceName}
	 * 
	 * @param id
	 * @return
	 */
	@Log(message="删除了id={0}的${functionName}。")
	@RequiresPermissions("${className}:delete")
	@RequestMapping(value="{id}/delete", method=RequestMethod.POST)
	@ResponseBody
	public Response delete(@PathVariable Long id) {
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{id}));
		return ${instanceName}Service.delete(id);
	}

	/**
	 * 批量删除${instanceName}
	 * 
	 * @param idArray
	 * @return
	 */
	@Log(message="批量删除了id={0}${functionName}。")
	@RequiresPermissions("${className}:delete")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Response delete(@RequestBody long[] idArray) {
		for(long id : idArray){
			${instanceName}Service.delete(id);
		}
		LogUitls.putArgs(LogMessageObject.newWrite().setObjects(new Object[]{Arrays.toString(idArray)}));
		return new SuccessResponse();
		
	}

}
