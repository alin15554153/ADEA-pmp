package com.anycc.pmp.rsmt.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anycc.common.CommonUtils;
import com.anycc.common.TempAuxiliaryUtils;
import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.DictionaryColumn;
import com.anycc.common.dto.ObjectResponse;
import com.anycc.common.dto.Response;
import com.anycc.common.dto.SuccessResponse;
import com.anycc.pmp.rsmt.dao.ResourceDownCheckDAO;
import com.anycc.pmp.rsmt.dao.ResourceDownDAO;
import com.anycc.pmp.rsmt.entity.ResourceDown;
import com.anycc.pmp.rsmt.entity.ResourceDownCheck;
import com.anycc.pmp.rsmt.service.ResourceDownCheckService;

@Service("resourceDownCheckService")
@Transactional
public class ResourceDownCheckServiceImpl implements ResourceDownCheckService {

	/*
	 * @Autowired private TempAuxiliaryUtils<ResourceDownCheck>
	 * tempAuxiliaryUtils;
	 */
	@Autowired
	private ResourceDownDAO resourceDownDAO;
	@Autowired
	private ResourceDownCheckDAO resourceDownCheckDAO;
	@Autowired
	private TempAuxiliaryUtils<ResourceDown> resourceDownAuxiliaryUtils;
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private CommonUtils<ResourceDown> commonUtils;

	@Override
	public DTData<ResourceDown> findUncheckedApplicationListBySql(ResourceDown resourceDown, String myId, String myRoleId, DTPager pager) {
		// 如果当前角色为地区管理员,获取该人所在的地区ID
		String areaId = "";
		if ("14003".equals(myRoleId)) {
			String sqlAreaId = "SELECT a.id FROM sys_area a, sys_organization o, sys_user u WHERE u.ID = " + myId + " AND u.ORGANIZATION_ID = o.id AND o.area_id = a.id";
			Query query = em.createNativeQuery(sqlAreaId);
			try {
				if (query.getResultList().size() > 0) {
					areaId = query.getResultList().get(0).toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		String sql = "SELECT r.name rname, " +
							"r.type, " +
							"p.name pname, " +
							"u.REALNAME, " +
							"DATE_FORMAT(rd.atime,'%Y-%m-%d'), " +
							"rd.aremark, " +
							"rd.aid, " +
							"ps.sname, " +
							"rd.process_type, " +
							"p.org_id " +
							"FROM t_resource_down rd, " +
							"resource r, " +
							"projectinfo p, " +
							"sys_user u, " +
							"projectstagedivision ps " +
							"WHERE rd.id = r.id AND r.pid = p.id AND rd.uid = u.ID AND r.sid = ps.sid ";
		// 如果当前角色为项目经理,则只显示该PM所负责的项目
		if ("14002".equals(myRoleId)) {
			sql += " AND p.manager = " + myId + " AND rd.status = '1'";// 审核状态为1:待审核
		}
		// 如果当前角色为项目经理地区管理员,则只显示该MG所在地区的所有项目
		if ("14003".equals(myRoleId)) {
			sql += " AND p.area_id = " + areaId + " AND rd.status = '2'";// 审核状态为2:PM通过;
		}
		// 如果当前角色为项目经理地区管理员,则只显示该MG所在地区的所有项目
		if ("14004".equals(myRoleId)) {
			sql += " AND rd.status = '4' AND rd.process_type = '2' ";// 审核状态为4:MG通过;
		}
		// 如果查不到角色ID
		if ("999".equals(myRoleId)) {
			sql += " AND rd.status = '999'";
		}
		// 其它搜索条件
		if (resourceDown.getResourceName() != null && !"".equals(resourceDown.getResourceName())) {
			sql += " AND r.name LIKE '%" + resourceDown.getResourceName() + "%'";
		}
		if (resourceDown.getResourceType() != null && !"".equals(resourceDown.getResourceType())) {
			sql += " AND r.type = '" + resourceDown.getResourceType() + "'";
		}
		if (resourceDown.getProjectName() != null && !"".equals(resourceDown.getProjectName())) {
			sql += " AND p.name LIKE '%" + resourceDown.getProjectName() + "%'";
		}
		if (resourceDown.getStageId() != null && !"".equals(resourceDown.getStageId())) {
			sql += " AND ps.sname = " + resourceDown.getStageId();
		}
		if (resourceDown.getId() != null && !"".equals(resourceDown.getId())) {
			sql += " AND rd.aid = '" + resourceDown.getId() + "'";
		}
		sql += " ORDER BY rd.atime DESC, rd.aid DESC ";

		String sqlCnt = "SELECT COUNT(*) FROM (" + sql + ") cnt";
		Query queryCnt = em.createNativeQuery(sqlCnt);
		List listCnt = queryCnt.getResultList();
		int cnt = Integer.valueOf(listCnt.get(0).toString());

		List<ResourceDown> list = new ArrayList<ResourceDown>();
		Query query = em.createNativeQuery(sql);
		int pageNumber = pager.getStart();
		int pageSize = pager.getLength();
		query.setFirstResult(pageNumber);
		query.setMaxResults(pageSize);
		List rows = query.getResultList();
		for (Object object : rows) {
			Object[] cells = (Object[]) object;
			ResourceDown temp = new ResourceDown();
			temp.setResourceName(cells[0] != null ? cells[0].toString() : "");
			temp.setResourceType(cells[1] != null ? cells[1].toString() : "");
			temp.setProjectName(cells[2] != null ? cells[2].toString() : "");
			temp.setUserName(cells[3] != null ? cells[3].toString() : "");
			temp.setAtimeString(cells[4] != null ? cells[4].toString() : "");
			temp.setAremark(cells[5] != null ? cells[5].toString() : "");
			temp.setId(cells[6] != null ? cells[6].toString() : "");
			temp.setProcessType(cells[8] != null ? Integer.parseInt(cells[8].toString()) : 0);
			temp.setOrgId(cells[9] != null ? cells[9].toString() : "");
			list.add(temp);
		}
		Page<ResourceDown> resourceDownPage = new PageImpl<ResourceDown>(list, resourceDownAuxiliaryUtils.buildPageRequest(pager), cnt);
		commonUtils.convertDictionaryPage(resourceDownPage, new DictionaryColumn("resourceType", "resourceTypeName", "资源类别"));
		// Page<ResourceDown> resourceDownPage =
		// resourceDownDAO.findAll(resourceDownAuxiliaryUtils.buildWhereClause(resourceDown),
		// resourceDownAuxiliaryUtils.buildPageRequest(pager));
		return new DTData<ResourceDown>(resourceDownPage, pager);
	}

	/*
	 * @Override public DTData<ResourceDown>
	 * findUncheckedApplicationList(Specification<ResourceDown> specification,
	 * DTPager pager) { Page<ResourceDown> tempPage =
	 * resourceDownDAO.findAll(specification,
	 * resourceDownAuxiliaryUtils.buildPageRequest(pager,new
	 * Sort(Sort.Direction.DESC,"atime"))); return new
	 * DTData<ResourceDown>(tempPage, pager); }
	 */

	// 审核资源下载申请
	@Override
	public Response checkApplication(String id, int sid, String status, String remark) {
		ResourceDownCheck resourceDownCheck = new ResourceDownCheck();
		resourceDownCheck.setRid(id);// 申请ID
		resourceDownCheck.setStime(new Date());// 审核时间
		resourceDownCheck.setSid(sid);// 审核人ID
		resourceDownCheck.setSremark(remark);// 审核原因
		try {
			resourceDownCheckDAO.save(resourceDownCheck);
			ResourceDown resourceDown = resourceDownDAO.findOne(String.valueOf(id));
			resourceDown.setStatus(status);
			try {
				resourceDownDAO.save(resourceDown);
				//return new SuccessResponse();
				return new ObjectResponse<String>(resourceDown.getId());
			} catch (Exception e) {
				e.printStackTrace();
				return new ObjectResponse<String>("后台保存审核状态失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ObjectResponse<String>("后台保存审核信息失败！");
		}
	}

	// 查看过往审核记录
	@Override
	public Response showPreRemark(String id) {
		List<ResourceDownCheck> list = new ArrayList<ResourceDownCheck>();
		String hql = "from ResourceDownCheck where rid = '" + id + "' order by stime desc, id desc";
		Query query = em.createQuery(hql);
		try {
			list = query.getResultList();
			if (list.size() > 0) {
				for (ResourceDownCheck resourceDownCheck : list) {
					String sql = "SELECT USERNAME FROM sys_user WHERE ID = " + resourceDownCheck.getSid();
					Query queryForName = em.createNativeQuery(sql);
					try {
						resourceDownCheck.setSname(queryForName.getResultList().get(0).toString());
					} catch (Exception e) {
						e.printStackTrace();
						resourceDownCheck.setSname("未知");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ObjectResponse<List<ResourceDownCheck>>(list);
	}

	@Override
	public List<ResourceDown> findTopSexList(ResourceDown resourceDown, String myId, String myRoleId) {
		// 如果当前角色为地区管理员,获取该人所在的地区ID
		String areaId = "";
		if ("14003".equals(myRoleId)) {
			String sqlAreaId = "SELECT a.id "
					+ "FROM sys_area a, sys_organization o, sys_user u "
					+ "WHERE u.ID = " + myId
					+ " AND u.ORGANIZATION_ID = o.id AND o.area_id = a.id";
			Query query = em.createNativeQuery(sqlAreaId);
			try {
				if (query.getResultList().size() > 0) {
					areaId = query.getResultList().get(0).toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		String sql = "SELECT r.name rname, r.type, p.name pname, u.REALNAME, DATE_FORMAT(rd.atime,'%Y-%m-%d'), rd.aremark, rd.aid, ps.sname "
				+ "FROM t_resource_down rd, resource r, projectinfo p, sys_user u, projectstagedivision ps "
				+ "WHERE rd.id = r.id AND r.pid = p.id AND rd.uid = u.ID AND r.sid = ps.sid ";
		// 如果当前角色为项目经理,则只显示该PM所负责的项目
		if ("14002".equals(myRoleId)) {
			sql += " AND p.manager = " + myId + " AND rd.status = '1'";// 审核状态为1:待审核
		}
		// 如果当前角色为项目经理地区管理员,则只显示该MG所在地区的所有项目
		if ("14003".equals(myRoleId)) {
			sql += " AND p.area_id = " + areaId + " AND rd.status = '2'";// 审核状态为2:PM通过;
		}
		// 如果当前角色为项目经理地区管理员,则只显示该MG所在地区的所有项目
		if ("14004".equals(myRoleId)) {
			sql += " AND rd.status = '4' AND rd.process_type = '2' ";// 审核状态为4:MG通过;
		}
		// 如果查不到角色ID
		if ("999".equals(myRoleId)) {
			sql += " AND rd.status = '999'";// 审核状态为4:MG通过;
		}
		sql += " ORDER BY rd.atime DESC";

		List<ResourceDown> list = new ArrayList<ResourceDown>();
		Query query = em.createNativeQuery(sql);
		List rows = query.getResultList();
		for (Object object : rows) {
			Object[] cells = (Object[]) object;
			ResourceDown temp = new ResourceDown();
			temp.setResourceName(cells[0] != null ? cells[0].toString() : "");
			temp.setResourceType(cells[1] != null ? cells[1].toString() : "");
			temp.setProjectName(cells[2] != null ? cells[2].toString() : "");
			temp.setUserName(cells[3] != null ? cells[3].toString() : "");
			temp.setAtimeString(cells[4] != null ? cells[4].toString() : "");
			temp.setAremark(cells[5] != null ? cells[5].toString() : "");
			temp.setId(cells[6] != null ? cells[6].toString() : "");
			list.add(temp);
		}
		if (list.size() > 6)
			list = list.subList(0, 6);
		return commonUtils.convertDictionaryList(list, new DictionaryColumn(
				"resourceType", "resourceTypeName", "资源类别"));
	}

}
