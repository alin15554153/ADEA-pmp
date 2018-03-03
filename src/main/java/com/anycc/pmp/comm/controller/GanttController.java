package com.anycc.pmp.comm.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anycc.common.CommonUtils;
import com.anycc.common.dto.DictionaryColumn;
import com.anycc.pmp.comm.entity.Gantt;
import com.anycc.pmp.comm.entity.GanttProperty;
import com.anycc.pmp.ptmt.entity.ProjectStage;
import com.anycc.pmp.ptmt.service.ProjectStageService;

/**
 * @author 方锦文
 * @Description: 甘特图类
 * @date 2016年04月02日 下午22:00:00
 */
@Controller
@RequestMapping("/management/pmp/comm/gantt")
public class GanttController {
	@Autowired
	private ProjectStageService projectstageService;
	@Autowired
	private CommonUtils<ProjectStage> commonUtils;
	private static final String GANTT = "management/comm/gantt/gantt";
	/**
	 * 跳转到甘特图页面
	 * @return
	 */
	@RequiresPermissions("commGantt:view")
	@RequestMapping(value = "{id}/toGantt", method = RequestMethod.GET)
	public String toGantt(@PathVariable("id") String id,Map<String, Object> map){
		map.put("id", id);
		return GANTT;
	}
	/**
	 * 根据项目编号查询阶段信息
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("commGantt:view")
	@RequestMapping(value = "{id}/queryGantt", method = RequestMethod.GET)
	@ResponseBody
	public Gantt queryGantt(@PathVariable("id") String id) {
		List<ProjectStage> stageList=projectstageService.findTabByPid(id);
		commonUtils.convertDictionaryList(stageList,new DictionaryColumn("sname","stageName","阶段名称"));
		List<GanttProperty> list=new ArrayList<GanttProperty>();
		int i=1;
		for (ProjectStage stage : stageList) {
			//添加计划阶段 begin
			GanttProperty p=new GanttProperty();
			p.setId(String.valueOf(i));
			p.setName(stage.getStageName()+"[计划]");
			p.setStart(stage.getExpbegintime().getTime());
			p.setEnd(stage.getExpendtime().getTime());
			p.setDuration(getDutyDays(stage.getExpbegintime(),stage.getExpendtime()));
//			p.setDuration((p.getEnd()-p.getStart())/(1000*3600*24));
			p.setStatus("STATUS_ACTIVE");
			list.add(p);
			//添加计划阶段 end
			i++;
			//添加实际阶段 begin
			if(stage.getActbegintime()!=null && stage.getActendtime()!=null){
				p=new GanttProperty();
				p.setId(String.valueOf(i));
				p.setName(stage.getStageName()+"[实际]");
				p.setStart(stage.getActbegintime().getTime());
				p.setEnd(stage.getActendtime().getTime());
				p.setDuration(getDutyDays(stage.getExpbegintime(),stage.getExpendtime()));
//				p.setDuration((p.getEnd()-p.getStart())/(1000*3600*24));
				p.setStatus("STATUS_SUSPENDED");
				list.add(p);
				i++;
			}
			//添加实际阶段 end
		}
		Gantt gantt=new Gantt();
		gantt.setTasks(list);
		return gantt;
	}
	
	public int getDutyDays(Date startDate,Date endDate) {  
		int result = 0;  
		while (startDate.compareTo(endDate) <= 0) {  
			if (startDate.getDay() != 6 && startDate.getDay() != 0)  
				result++;  
			startDate.setDate(startDate.getDate() + 1);  
		}  
		return result;  
	}  
}
