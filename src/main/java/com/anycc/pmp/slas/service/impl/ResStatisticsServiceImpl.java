package com.anycc.pmp.slas.service.impl;

import com.anycc.common.ExportTableUtil;
import com.anycc.common.TempAuxiliaryUtils;
import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.ObjectResponse;
import com.anycc.common.dto.Response;
import com.anycc.common.dto.TableData;
import com.anycc.pmp.ptmt.dao.ProjectDAO;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.slas.entity.ResStatistics;
import com.anycc.pmp.slas.service.ResStatisticsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shibin on 2016/3/17.
 */
@Service
@Transactional
public class ResStatisticsServiceImpl implements ResStatisticsService {

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private TempAuxiliaryUtils<Project> tempAuxiliaryUtils;

    @PersistenceContext
    private EntityManager em;

    @Override
    public DTData<ResStatistics> queryByPaging(Project project, DTPager pager) {

        String sql = "SELECT " +
                "sa.name saname," +
                "so.organname," +
                "p.name, " +
                "r.rCount," +
                "r.totalTimes, " +
                "p.id " +
                "FROM projectinfo p " +
                "LEFT JOIN sys_area sa ON p.area_id = sa.id " +
                "LEFT JOIN projectstagedivision ps ON p.stage = ps.sid " +
                "LEFT JOIN sys_organization so ON p.org_id = so.id " +
                "LEFT JOIN (select pid, count(*) rCount,sum(times) totalTimes  from resource GROUP BY pid) r ON r.pid = p.id " +
                "WHERE p.del_tag='0' ";
        if (project.getStarttime()!=null && !"".equals(project.getStarttime())) {
            sql += " AND p.ctime >= '" + project.getStarttime() + "'";
        }
    	if (project.getEndtime()!=null && !"".equals(project.getEndtime())) {
            sql += " AND p.ctime <= '" + project.getEndtime() + "'";
        }
        if (project.getOrgids()!=null && !"".equals(project.getOrgids())) {
            sql += " AND p.org_id in (" + project.getOrgids() + ")";
        }
        if (project.getName()!=null && !"".equals(project.getName())) {
            sql += " AND p.name like '%" + project.getName() + "%'";
        }
        if (project.getStageName()!=null && !"".equals(project.getStageName())) {
            sql += " AND ps.sname = " + project.getStageName();
        }


        String sqlCnt = "SELECT COUNT(*) FROM ("+sql+") cnt";
        Query queryCnt = em.createNativeQuery(sqlCnt);
        List listCnt = queryCnt.getResultList();
        int cnt = Integer.valueOf(listCnt.get(0).toString());

        List<ResStatistics> list = new ArrayList<ResStatistics>();
        Query query = em.createNativeQuery(sql);
        int pageNumber = pager.getStart();
        int pageSize = pager.getLength();
        query.setFirstResult(pageNumber);
        query.setMaxResults(pageSize);
        List rows = query.getResultList();
        for (Object object : rows) {
            Object[] cells = (Object[]) object;
            ResStatistics temp = new ResStatistics();
            temp.setArea(cells[0] != null ? cells[0].toString() : "");
            temp.setCompany(cells[1] != null ? cells[1].toString() : "");
            temp.setName(cells[2] != null ? cells[2].toString() : "");
            temp.setResCount(cells[3] != null ? cells[3].toString() : "");
            temp.setTotalTimes(cells[4] != null ? cells[4].toString() : "");
            temp.setId(cells[5] != null ? cells[5].toString() : "");
            list.add(temp);
        }
        Page<ResStatistics> resourcePage = new PageImpl<ResStatistics>(list, tempAuxiliaryUtils.buildPageRequest(pager),cnt);
        return new DTData<ResStatistics>(resourcePage, pager);
    }

    @Override
    public Response queryResStatisticsByAreaAndCompany(ResStatistics resStatistics) {

        String sql = "SELECT " +
                "sa.name saname," +
                "so.organname," +
                "p.name, " +
                "r.rCount," +
                "r.totalTimes " +
                "FROM projectinfo p " +
                "LEFT JOIN sys_area sa ON p.area_id = sa.id " +
                "LEFT JOIN sys_organization so ON p.org_id = so.id " +
                "LEFT JOIN (select pid, count(*) rCount,sum(times) totalTimes  from resource GROUP BY pid) r ON r.pid = p.id " +
                "WHERE 1=1 ";

        List<ResStatistics> list = new ArrayList<ResStatistics>();

        Query query = em.createNativeQuery(sql);
        List rows = query.getResultList();

        for (Object object : rows) {
            Object[] cells = (Object[]) object;
            ResStatistics temp = new ResStatistics();
            temp.setArea(cells[0] != null ? cells[0].toString() : "");
            temp.setCompany(cells[1] != null ? cells[1].toString() : "");
            temp.setName(cells[2] != null ? cells[2].toString() : "");
            temp.setResCount(cells[3] != null ? cells[3].toString() : "");
            temp.setTotalTimes(cells[4] != null ? cells[4].toString() : "");
            list.add(temp);
        }

        return new ObjectResponse<List<ResStatistics>>(list);
    }

	@Override
	public ResponseEntity<byte[]> exportResourceList(HttpServletRequest request)
			throws IOException {
		List<String> header = new ArrayList<String>();
		header.add("所属地区");
		header.add("所属公司");
		header.add("项目名称");
		header.add("资源数量");
		header.add("下载总数");

		String sql = "SELECT " +
                "sa.name saname," +
                "so.organname," +
                "p.name, " +
                "ifnull(r.rCount,'0')," +
                "ifnull(r.totalTimes,'0') " +
                "FROM projectinfo p " +
                "LEFT JOIN sys_area sa ON p.area_id = sa.id " +
                "LEFT JOIN projectstagedivision ps ON p.stage = ps.sid " +
                "LEFT JOIN sys_organization so ON p.org_id = so.id " +
                "LEFT JOIN (select pid, count(*) rCount,sum(times) totalTimes  from resource GROUP BY pid) r ON r.pid = p.id " +
                "WHERE p.del_tag='0' ";
		if (request.getParameter("exportstarttime")!=null && !"".equals(request.getParameter("exportstarttime"))) {
	         sql += " AND p.ctime >= '"+request.getParameter("exportstarttime")+"' ";
	    }
		if (request.getParameter("exportendtime")!=null && !"".equals(request.getParameter("exportendtime"))) {
	         sql += " AND p.ctime <= '"+request.getParameter("exportendtime")+"' ";
	    }
        if (request.getParameter("exportcompany")!=null && !"".equals(request.getParameter("exportcompany"))) {
            sql += " AND p.org_id in (" + request.getParameter("exportcompany") + ")";
        }
        if (request.getParameter("exportname")!=null && !"".equals(request.getParameter("exportname"))) {
            sql += " AND p.name like '%" + request.getParameter("exportname") + "%'";
        }
        if (request.getParameter("exportstage")!=null && !"".equals(request.getParameter("exportstage"))) {
            sql += " AND ps.sname = " + request.getParameter("exportstage");
        }
        Query query = null;
		query = em.createNativeQuery(sql);
		List<Object[]> data = new ArrayList<Object[]>();
		data = query.getResultList();

		TableData tableData = new TableData();//表格对象
		tableData.setHeader(header);//列表头
		tableData.setData(data);//列表数据
		tableData.setFilename("资源统计.xlsx");//下载的文件名,默认table.xlsx
		return ExportTableUtil.excelSave(tableData, request);
	}
}
