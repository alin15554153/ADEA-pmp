
package	com.anycc.pmp.ptmt.service.impl;

import com.anycc.common.CommonUtils;
import com.anycc.common.TempAuxiliaryUtils;
import com.anycc.common.dto.*;
import com.anycc.pmp.comm.entity.TempDemo;
import com.anycc.pmp.ptmt.dao.ProjectDAO;
import com.anycc.pmp.ptmt.dao.ProjectStageDAO;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.ptmt.entity.ProjectStage;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.anycc.pmp.ptmt.entity.ProjectChangeLog;
import com.anycc.pmp.ptmt.dao.ProjectChangeLogDAO;
import com.anycc.pmp.ptmt.service.ProjectChangeLogService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("projectChangeLogService")
@Transactional
public class ProjectChangeLogServiceImpl implements ProjectChangeLogService {
	
	@Autowired
	private ProjectChangeLogDAO projectChangeLogDAO;

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	private ProjectStageDAO projectstageDAO;

	@Autowired
	private TempAuxiliaryUtils<ProjectChangeLog> tempAuxiliaryUtils;

	@Autowired
	private CommonUtils<ProjectChangeLog> commonUtilsPjChange;
	
	@Autowired
	private CommonUtils<ProjectStage> commonUtils;

	/*
	 * (non-Javadoc)
	 * @see com.anycc.pmp.ptmt.service.ProjectChangeLogService#queryByPaging(com.anycc.pmp.ptmt.entity.ProjectChangeLog,com.anycc.rhip.common.dto.DTPager)  
	 */ 
	@Override
	public DTData<ProjectChangeLog> queryByPaging(ProjectChangeLog projectChangeLog, DTPager pager) {
		Page<ProjectChangeLog> projectChangeLogPage = projectChangeLogDAO.findAll(tempAuxiliaryUtils.buildWhereClause(projectChangeLog), tempAuxiliaryUtils.buildPageRequest(pager));
		return new DTData<ProjectChangeLog>(projectChangeLogPage, pager);
	}

	@Override
	public DTData<ProjectChangeLog> queryByPaging(Specification specification, DTPager pager) {
		try{
			Page<ProjectChangeLog> projectChangeLogPage = projectChangeLogDAO.findAll(specification, tempAuxiliaryUtils.buildPageRequest(pager));
			List<DictionaryColumn> list=new ArrayList<DictionaryColumn>();
			list.add(new DictionaryColumn("bfchangeType","bfchangeTypeName","阶段名称","未开始"));
			list.add(new DictionaryColumn("sname","stageName","阶段名称","未开始"));
			commonUtilsPjChange.convertDictionaryPage(projectChangeLogPage, list);
			return new DTData<ProjectChangeLog>(projectChangeLogPage, pager);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}


	/*
	 * (non-Javadoc) 
	 * @see com.anycc.pmp.ptmt.service.ProjectChangeLogService#addProjectChangeLog(com.anycc.pmp.ptmt.entity.ProjectChangeLog)  
	 */
	@Override
	public Response addProjectChangeLog(ProjectChangeLog projectChangeLog) {
		Assert.notNull(projectChangeLog);

		try{
			String projectId = projectChangeLog.getPid();



			ComputingCondition condition = new ComputingCondition();
			condition.setCondition("id", projectChangeLog.getSid(), ComputingSymbol.EQUAL);
			StringBuffer hql = new StringBuffer("from ProjectStage");
			List<ProjectStage> projectStages = tempAuxiliaryUtils.findBySearch(hql, condition);
			ProjectStage projectStage = null;
			if (projectStages!=null && projectStages.size()>0) {
				projectStage = projectStages.get(0);
			}
			BeanUtils.copyProperties(projectStage, projectChangeLog, new String[]{"type","pid"});

			//更改项目阶段状态
			Project project = projectDAO.findOne(projectId);

			ProjectStage curStage = project.getProjectstage();
			if (curStage != null) {
				curStage.setActendtime(new Date());
			}

			projectStage.setActbegintime(new Date());
			project.setProjectstage(projectStage);


			projectChangeLog.setChangeTime(new Date());

			projectChangeLogDAO.save(projectChangeLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (projectChangeLog.getId() != "") {
			return new SuccessResponse();
		} else {
			return new FailedResponse("后台保存“阶段变更”失败!");
		}
	}

    /*
	 * (non-Javadoc)
	 * @see com.anycc.pmp.ptmt.service.ProjectChangeLogService#queryById(java.lang.Long)  
	 */
	@Override
	public Response queryById(String id) {
		Assert.isTrue(id != "");
		ProjectChangeLog projectChangeLog = projectChangeLogDAO.findOne(id);
		if (projectChangeLog != null && projectChangeLog.getId() != "") {
			return new ObjectResponse<ProjectChangeLog>(projectChangeLog);
		} else {
			return new FailedResponse("后台查询ID为[" + id + "]的信息失败！");
		}
	}
    
    
     /*
	 * (non-Javadoc)
	 * @see com.anycc.pmp.ptmt.service.ProjectChangeLogService#updateProjectChangeLog(java.lang.Long)  
	 */
	@Override
	public Response updateProjectChangeLog(ProjectChangeLog projectChangeLog) {
		Assert.notNull(projectChangeLog);
		Assert.notNull(projectChangeLog.getId());
		projectChangeLogDAO.save(projectChangeLog);
		return new SuccessResponse();
	}
	
	 /*
	 * (non-Javadoc)
	 * @see com.anycc.pmp.ptmt.service.ProjectChangeLogService#delete(java.lang.Long)  
	 */
	@Override
	public Response delete(String l) {
		// TODO Auto-generated method stub
		projectChangeLogDAO.delete(l);
		return new SuccessResponse();
	}

	@Override
	public Response initStageByProject(String id) {

		StringBuffer hql = new StringBuffer("from ProjectStage");
		ComputingCondition condition = new ComputingCondition("pid", id, ComputingSymbol.EQUAL);
		List<ProjectStage> projectStages = new ArrayList<>();
		projectStages = tempAuxiliaryUtils.findBySearch(hql, condition);
		commonUtils.convertDictionaryList(projectStages, new DictionaryColumn("sname","stageName","阶段名称"));
		if (projectStages!=null && projectStages.size()>0) {
			return new ObjectResponse<List<ProjectStage>>(projectStages);
		}

		return new ObjectResponse<List<ProjectStage>>(projectStages);

	}

}
