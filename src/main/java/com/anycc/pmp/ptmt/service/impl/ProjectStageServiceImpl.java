package com.anycc.pmp.ptmt.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.anycc.common.CommonUtils;
import com.anycc.common.TempAuxiliaryUtils;
import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.DictionaryColumn;
import com.anycc.common.dto.FailedResponse;
import com.anycc.common.dto.ObjectResponse;
import com.anycc.common.dto.Response;
import com.anycc.common.dto.SuccessResponse;
import com.anycc.pmp.ptmt.dao.ProjectStageDAO;
import com.anycc.pmp.ptmt.entity.ProjectStage;
import com.anycc.pmp.ptmt.service.ProjectStageService;
import com.anycc.pmp.rsmt.entity.Resource;

@Service("projectStageService")
@Transactional
public class ProjectStageServiceImpl implements ProjectStageService {

	@Autowired
	private ProjectStageDAO projectstageDAO;
	@Autowired
	private TempAuxiliaryUtils<ProjectStage> tempAuxiliaryUtils;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private CommonUtils<ProjectStage> commonUtils;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.anycc.pmp.ptmt.service.ProjectService#queryByPaging(com.anycc.pmp
	 * .ptmt.entity.Project,com.anycc.rhip.common.dto.DTPager)
	 */
	@Override
	public DTData<ProjectStage> queryByPaging(Specification specification,
			DTPager pager) {
		Page<ProjectStage> projectPage = projectstageDAO.findAll(specification,
				tempAuxiliaryUtils.buildPageRequest(pager));
		List<DictionaryColumn> list = new ArrayList<DictionaryColumn>();
		list.add(new DictionaryColumn("sname", "stageName", "阶段名称"));
		// list.add(new DictionaryColumn("type","typeName","阶段类型"));
		commonUtils.convertDictionaryPage(projectPage, list);
		return new DTData<ProjectStage>(projectPage, pager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.anycc.pmp.ptmt.service.ProjectService#queryByPaging(com.anycc.pmp
	 * .ptmt.entity.Project,com.anycc.rhip.common.dto.DTPager)
	 */
	@Override
	public DTData<ProjectStage> queryByPagingWithPid(
			Specification specification, DTPager pager, String pid) {
		String sql = "select sid,sname,sseq,expbegintime,expendtime,type,pid from projectstagedivision where pid= '"
				+ pid + "' ";
		Query query = em.createNativeQuery(sql);
		int pageNumber = pager.getStart();
		int pageSize = pager.getLength();
		List rows = query.getResultList();
		query.setFirstResult(pageNumber);
		query.setMaxResults(pageSize);
		List rowspager = query.getResultList();
		List<ProjectStage> list = new ArrayList<ProjectStage>();
		for (Object object : rowspager) {
			Object[] cells = (Object[]) object;
			ProjectStage pagestage = new ProjectStage();

			pagestage.setSid(cells[0].toString());
			pagestage.setSname(cells[1] != null ? cells[1].toString() : "");
			pagestage.setSseq(Integer.parseInt(cells[2] != null ? cells[2]
					.toString() : "0"));
			pagestage.setType(Integer.parseInt(cells[5] != null ? cells[5]
					.toString() : "0"));
			pagestage.setPid(cells[5] != null ? cells[6].toString() : "0");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String expbegintime = cells[3] != null ? cells[3].toString() : "";
			String expendtime = cells[4] != null ? cells[4].toString() : "";
			try {
				if (!"".equals(expbegintime)) {
					Date date = sdf.parse(expbegintime);
					pagestage.setExpbegintime(date);
				}
				if (!"".equals(expendtime)) {
					Date datee = sdf.parse(expendtime);
					pagestage.setExpendtime(datee);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list.add(pagestage);
		}
		Page<ProjectStage> pagestageDataPage = new PageImpl<ProjectStage>(list,
				tempAuxiliaryUtils.buildPageRequest(pager),
				rows != null ? rows.size() : 0);
		return new DTData<ProjectStage>(pagestageDataPage, pager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.anycc.pmp.ptmt.service.ProjectService#queryByPaging(com.anycc.pmp
	 * .ptmt.entity.Project,com.anycc.rhip.common.dto.DTPager)
	 */
	@Override
	public DTData<Resource> queryByPagingWithStageId(
			Specification specification, DTPager pager, String pid,
			String stageid) {
		String sql = "select id,name,type,uid,uploaddate,sid from resource where pid= '"
				+ pid + "' and sid ='" + stageid + "' ";
		Query query = em.createNativeQuery(sql);
		int pageNumber = pager.getStart();
		int pageSize = pager.getLength();
		List rows = query.getResultList();
		query.setFirstResult(pageNumber);
		query.setMaxResults(pageSize);
		List rowspager = query.getResultList();
		List<Resource> list = new ArrayList<Resource>();
		for (Object object : rowspager) {
			Object[] cells = (Object[]) object;
			Resource resource = new Resource();

			resource.setId(cells[0].toString());
			resource.setName(cells[1] != null ? cells[1].toString() : "");
			resource.setType(cells[2] != null ? cells[2].toString() : "0");
			resource.setUid(Integer.parseInt(cells[3] != null ? cells[3]
					.toString() : "0"));
			resource.setSid(cells[5] != null ? cells[5].toString() : "0");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String uploaddate = cells[4] != null ? cells[4].toString() : "";
			try {
				if (!"".equals(uploaddate)) {
					Date date = sdf.parse(uploaddate);
					resource.setUploaddate(date);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list.add(resource);
		}
		Page<Resource> pagestageDataPage = new PageImpl<Resource>(list,
				tempAuxiliaryUtils.buildPageRequest(pager),
				rows != null ? rows.size() : 0);
		return new DTData<Resource>(pagestageDataPage, pager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.anycc.pmp.ptmt.service.ProjectService#addProject(com.anycc.pmp.ptmt
	 * .entity.Project)
	 */
	@Override
	public Response addProjectStage(ProjectStage projectstage) {
		Assert.notNull(projectstage);
		projectstageDAO.save(projectstage);
		if (!"".equals(projectstage.getSid()) && projectstage.getSid() != null) {
			return new SuccessResponse();
		} else {
			return new FailedResponse("后台保存project失败!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.anycc.pmp.ptmt.service.ProjectService#queryById(java.lang.Long)
	 */
	@Override
	public Response queryById(String id) {
		// Assert.isTrue(id != null);
		Assert.notNull(id);
		ProjectStage projectstage = projectstageDAO.findOne(id);
		if (projectstage != null && projectstage.getSid() != null) {
			return new ObjectResponse<ProjectStage>(projectstage);
		} else {
			return new FailedResponse("后台查询ID为[" + id + "]的信息失败！");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.anycc.pmp.ptmt.service.ProjectService#updateProject(java.lang.Long)
	 */
	@Override
	public Response updateProjectStage(ProjectStage projectstage) {
		Assert.notNull(projectstage);
		Assert.notNull(projectstage.getSid());
		projectstageDAO.save(projectstage);
		return new SuccessResponse();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.anycc.pmp.ptmt.service.ProjectService#delete(java.lang.Long)
	 */
	@Override
	public Response delete(String l) {
		// TODO Auto-generated method stub
		projectstageDAO.delete(l);
		return new SuccessResponse();
	}

	@Override
	public List<ProjectStage> findByPid(String pid) {
		// TODO Auto-generated method stub
		List<ProjectStage> list = projectstageDAO.findByPid(pid);
		/*
		 * commonUtils.convertDictionaryList(list, new DictionaryColumn("sname",
		 * "stageName", "阶段名称"));
		 */
		return commonUtils.convertDictionaryList(list, new DictionaryColumn(
				"sname", "stageName", "阶段名称", "未开始"));
	}

	@Override
	public List<ProjectStage> findTabByPid(String pid) {
		// TODO Auto-generated method stub
		List<ProjectStage> list = projectstageDAO.findByPid(pid);
		return list;
	}

	@Override
	public List<ProjectStage> findSidByUser(String id, String uid) {
		// TODO Auto-generated method stub
		List<ProjectStage> list = projectstageDAO.findSidByUser(id, uid);
		return commonUtils.convertDictionaryList(list, new DictionaryColumn(
				"sname", "stageName", "阶段名称"));
	}
}
