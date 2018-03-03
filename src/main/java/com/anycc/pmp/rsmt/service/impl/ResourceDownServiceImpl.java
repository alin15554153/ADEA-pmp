package	com.anycc.pmp.rsmt.service.impl;

import com.anycc.common.CommonUtils;
import com.anycc.common.ExportTableUtil;
import com.anycc.common.TempAuxiliaryUtils;
import com.anycc.common.dto.*;
import com.anycc.pmp.ptmt.dao.ProjectDAO;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.ptmt.entity.ProjectStage;
import com.anycc.pmp.rsmt.dao.ResourceDAO;
import com.anycc.pmp.rsmt.entity.Resource;
import com.anycc.pmp.rsmt.entity.ResourceDownCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.anycc.pmp.rsmt.entity.ResourceDown;
import com.anycc.pmp.rsmt.dao.ResourceDownDAO;
import com.anycc.pmp.rsmt.service.ResourceDownService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("resourceDownService")
@Transactional
public class ResourceDownServiceImpl implements ResourceDownService {

	@Autowired
	private ProjectDAO projectDAO;
	@Autowired
	private ResourceDAO resourceDAO;
	@Autowired
	private ResourceDownDAO resourceDownDAO;
	@Autowired
	private TempAuxiliaryUtils<Resource> resourceAuxiliaryUtils;
	@Autowired
	private TempAuxiliaryUtils<ResourceDown> tempAuxiliaryUtils;
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private CommonUtils<Resource> commonUtils;

	///////////////////////////////////////////////////////////////////////////////////////////////
	//资源申请页面
	///////////////////////////////////////////////////////////////////////////////////////////////
	/*@Override
	public DTData<ResourceDown> queryByPaging(ResourceDown resourceDown, DTPager pager) {
		Page<ResourceDown> resourceDownPage = resourceDownDAO.findAll(tempAuxiliaryUtils.buildWhereClause(resourceDown), tempAuxiliaryUtils.buildPageRequest(pager));
		return new DTData<ResourceDown>(resourceDownPage, pager);
	}*/

	@Override
	public DTData<Resource> findResourceList(Specification specification, DTPager pager) {
		Page<Resource> resourcePage = resourceDAO.findAll(specification, tempAuxiliaryUtils.buildPageRequest(pager));
		List<DictionaryColumn> listDictionaryColumn = new ArrayList<DictionaryColumn>();
		listDictionaryColumn.add(new DictionaryColumn("type", "typeName", "资源类别"));
		listDictionaryColumn.add(new DictionaryColumn("sid", "stageName", "阶段名称", "未开始"));
		commonUtils.convertDictionaryPage(resourcePage, listDictionaryColumn);
		return new DTData<Resource>(resourcePage, pager);
	}

	@Override
	public DTData<Resource> findResourceListBySql(ResourceDown resourceDown, DTPager pager){
		String userId = resourceDown.getUid().toString();//当前用户ID
		String sql = "SELECT r.name rname, " +
					 "r.type, " +
					 "p.name pname, " +
					 "su.REALNAME, " +
					 "DATE_FORMAT(r.uploaddate,'%Y-%m-%d'), " +
					 "r.id, " +
					 "ps.sname, " +
					 //"r.sid, " +
					 "sa.name, " +
					 "so.organname, " +
					 "r.pid, " +
					 "p.area_id, " +
					 "r.times, " +
					 "r.path, " +
					 "p.manager " +
					 "FROM resource r, " +
					 "projectinfo p, " +
					 "projectstagedivision ps, " +
					 "sys_user su, " +
					 //"projectstagedivision ps, " +
					 "sys_area sa, " +
					 "sys_organization so " +
					 "WHERE r.pid = p.id AND r.uid = su.ID AND p.area_id = sa.id AND p.org_id = so.id AND r.sid = ps.sid AND p.del_tag = 0 " +
					 "AND ( r.id NOT IN (SELECT DISTINCT rd.id FROM t_resource_down rd WHERE rd.uid = '"+userId+"')) ";
					 //"AND r.sid = ps.sid";
		//其它搜索条件
		if(resourceDown.getResourceName()!=null&&!"".equals(resourceDown.getResourceName())){
			sql += " AND r.name LIKE '%" + resourceDown.getResourceName() + "%'";
		}
		if(resourceDown.getResourceType()!=null&&!"".equals(resourceDown.getResourceType())){
			sql += " AND r.type = '" + resourceDown.getResourceType() + "'";
		}
		if(resourceDown.getProjectName()!=null&&!"".equals(resourceDown.getProjectName())){
			sql += " AND p.name LIKE '%" + resourceDown.getProjectName() + "%'";
		}
		if(resourceDown.getStageId()!=null&&!"".equals(resourceDown.getStageId())){
			sql += " AND ps.sname = '" + resourceDown.getStageId() + "'";
		}
		sql += " ORDER BY r.uploaddate DESC, r.id DESC";

		String sqlCnt = "SELECT COUNT(*) FROM ("+sql+") cnt";
		Query queryCnt = em.createNativeQuery(sqlCnt);
		List listCnt = queryCnt.getResultList();
		int cnt = Integer.valueOf(listCnt.get(0).toString());

		List<Resource> list = new ArrayList<Resource>();
		Query query = em.createNativeQuery(sql);
		int pageNumber = pager.getStart();
		int pageSize = pager.getLength();
		query.setFirstResult(pageNumber);
		query.setMaxResults(pageSize);
		List rows = query.getResultList();
		for (Object object : rows) {
			Object[] cells = (Object[])object;
			Resource temp = new Resource();
			temp.setName(cells[0] != null ? cells[0].toString() : "");
			temp.setType(cells[1] != null ? cells[1].toString() : "");
			temp.setProjectName(cells[2] != null ? cells[2].toString() : "");
			temp.setUserName(cells[3] != null ? cells[3].toString() : "");
			temp.setDateString(cells[4] != null ? cells[4].toString() : "");
			temp.setId(cells[5] != null ? cells[5].toString() : null);
			//temp.setStageName(cells[6] != null ? cells[6].toString() : "");
			temp.setSid(cells[6] != null ? cells[6].toString() : "");
			temp.setAreaName(cells[7] != null ? cells[7].toString() : "");
			temp.setOrgName(cells[8] != null ? cells[8].toString() : "");
			temp.setPid(cells[9] != null ? cells[9].toString() : "");
			temp.setAreaId(cells[10] != null ? cells[10].toString() : "");
			temp.setTimes(cells[11] != null ? Integer.parseInt(cells[11].toString()) : 0);
			temp.setPath(cells[12] != null ? cells[12].toString() : "");
			temp.setRemark(cells[13] != null ? cells[13].toString() : "");//项目经理ID
			list.add(temp);
		}
		Page<Resource> resourcePage = new PageImpl<Resource>(list, resourceAuxiliaryUtils.buildPageRequest(pager),cnt);

		List<DictionaryColumn> listDictionaryColumn = new ArrayList<DictionaryColumn>();
		listDictionaryColumn.add(new DictionaryColumn("type", "typeName", "资源类别"));
		listDictionaryColumn.add(new DictionaryColumn("sid", "stageName", "阶段名称", "未开始"));

		commonUtils.convertDictionaryPage(resourcePage, listDictionaryColumn);

		return new DTData<Resource>(resourcePage, pager);
	}

	/**
	 * 根据当前用户ID获取所参与的项目ID列表
	 */
	@Override
	public List<Object> getMyProjectIds(String userid){
		String sql = "SELECT DISTINCT pid FROM projectmember WHERE uid = " + userid;
		Query query = em.createNativeQuery(sql);
		List<Object> list = new ArrayList<Object>();
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	/**
	 * 根据当前用户ID获取所属于的区域
	 */
	@Override
	public String getMyAreaId(String userId){
		String sql = "SELECT DISTINCT o.area_id FROM sys_organization o, sys_user u WHERE u.id = " + userId + " AND u.organization_id = o.id";
		Query query = em.createNativeQuery(sql);
		String areaId = "";
		try {
			areaId = query.getResultList().get(0).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return areaId;

	}

	/**
	 * 发送下载申请
     */
	@Override
	public Response sendApplication(String rid, int uid, String remark, int processType) {
		ResourceDown resourceDown = new ResourceDown();
		resourceDown.setRid(rid);
		resourceDown.setUid(uid);
		resourceDown.setAtime(new Date());
		resourceDown.setAremark(remark);
		resourceDown.setStatus("1");
		resourceDown.setProcessType(processType);
		try {
			resourceDownDAO.save(resourceDown);
			//return new SuccessResponse();
			return new ObjectResponse<String>(resourceDown.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return new FailedResponse("后台保存resourceDown失败!");
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	//已经申请资源列表页面
	///////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 已申请列表
     */
	@Override
	public DTData<ResourceDown> findApplicationList(Specification<ResourceDown> specification, DTPager pager) {
		Page<ResourceDown> tempPage = null;

		//List<DictionaryColumn> listDictionaryColumn = new ArrayList<DictionaryColumn>();
		//listDictionaryColumn.add(new DictionaryColumn("resource.type", "userName", "资源类别"));

		//resourceDownCommonUtils.convertDictionaryPage(tempPage, listDictionaryColumn);
		try {
			tempPage = resourceDownDAO.findAll(specification, tempAuxiliaryUtils.buildPageRequest(pager));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new DTData<ResourceDown>(tempPage, pager);
	}

	/**
	 * 获取最近一条的审核记录
     */
	@Override
	public ResourceDownCheck findCheckById(String id){
		String hql = "from ResourceDownCheck where rid = '" + id + "' order by stime desc, id desc";
		Query query = em.createQuery(hql);
		List<ResourceDownCheck> list = new ArrayList<ResourceDownCheck>();
		try {
			list = query.getResultList();
			if (list.size() > 0){
				ResourceDownCheck resourceDownCheck = list.get(0);
				String sql = "SELECT REALNAME FROM sys_user WHERE ID = " + resourceDownCheck.getSid();
				Query queryForName = em.createNativeQuery(sql);
				try {
					resourceDownCheck.setSname(queryForName.getResultList().get(0).toString());
				} catch (Exception e) {
					e.printStackTrace();
					resourceDownCheck.setSname("未知");
				}
				return resourceDownCheck;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 重新发送下载申请
	 */
	@Override
	public Response reSendApplication(String id, String remark) {
		ResourceDown resourceDown = new ResourceDown();
		try {
			resourceDown = resourceDownDAO.findOne(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resourceDown.setAtime(new Date());
		resourceDown.setAremark(remark);
		resourceDown.setStatus("1");
		try {
			resourceDownDAO.save(resourceDown);
			//return new SuccessResponse();
			return new ObjectResponse<String>(resourceDown.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return new FailedResponse("后台保存resourceDown失败!");
		}
	}

	/**
	 * 增加下载次数
	 */
	@Override
	public Response addDownloadTimes(String rid) {
		Resource resource = new Resource();
		try {
			resource = resourceDAO.findOne(rid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (resource.getTimes() == null || "".equals(resource.getTimes())) {
			resource.setTimes(1);
		} else {
			int times = resource.getTimes();
			resource.setTimes(times + 1);
		}
		try {
			resourceDAO.save(resource);
			return new SuccessResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return new FailedResponse("后台保存resource失败!");
		}
	}

	//获得阶段下拉列表
	@Override
	public List<ProjectStage> findProjectStageList(){
		List<ProjectStage> list = new ArrayList<ProjectStage>();
		String hql = "from ProjectStage order by sid";
		Query query = em.createQuery(hql);
		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	//EXCEL导出
	@Override
	public ResponseEntity<byte[]> exportResourceList(HttpServletRequest request) throws IOException {
		String resourceName = new String(request.getParameter("resourceName").getBytes("iso8859-1"), "UTF-8");
		String resourceType = request.getParameter("resourceType");
		String stageId = request.getParameter("stageId");
		String projectName = request.getParameter("projectName");

		List<String> header = new ArrayList<String>();
		header.add("资源名称");
		header.add("资源类别");
		header.add("项目名称");
		//header.add("阶段");
		header.add("区域名称");
		header.add("公司名称");
		header.add("上传人");
		header.add("上传时间");
		header.add("下载次数");

		String sql = "SELECT " +
					 "r.name rname, " +
					 "r.type, " +
					 "p.name pname, " +
					 "sa.name, " +
					 "so.organname, " +
					 "su.REALNAME, " +
					 "DATE_FORMAT(r.uploaddate,'%Y-%m-%d'), " +
					 "r.times " +
					 "FROM resource r, " +
					 "projectinfo p, " +
					 "sys_user su, " +
					 "sys_area sa, " +
					 "sys_organization so " +
					 "WHERE r.pid = p.id AND r.uid = su.ID AND p.area_id = sa.id AND p.org_id = so.id ";
		//"AND r.sid = ps.sid";
		//其它搜索条件
		if(resourceName!=null&&!"".equals(resourceName)){
			sql += " AND r.name LIKE '%" + resourceName + "%'";
		}
		if(resourceType!=null&&!"".equals(resourceType)){
			sql += " AND r.type = '" + resourceType + "'";
		}
		if(projectName!=null&&!"".equals(projectName)){
			sql += " AND p.name LIKE '%" + projectName + "%'";
		}
		if(stageId!=null&&!"".equals(stageId)){
			sql += " AND r.sid = " + stageId;
		}
		sql += " ORDER BY r.uploaddate DESC";
		Query query = null;
		query = em.createNativeQuery(sql);
		List<Object[]> data = new ArrayList<Object[]>();
		data = query.getResultList();
		/*int count = 1;
		for(Object[] object : data){
			object[0] = count;
			count++;
		}*/

		TableData tableData = new TableData();//表格对象
		tableData.setHeader(header);//列表头
		tableData.setData(data);//列表数据
		//tableData.setFilename(filename);//下载的文件名,默认table.xlsx

		return ExportTableUtil.excelSave(tableData, request);

	}

	/**
	 * 通过资源ID查找项目
	 * @param rid
	 * @return Project
     */
	@Override
	public Project findProjectByResourceId(String rid){
		Resource resource = new Resource();
		Project project = new Project();
		try {
			resource = resourceDAO.findOne(rid);
			String pid = resource.getPid();
			project = projectDAO.findOne(pid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return project;
	}
	    
}
