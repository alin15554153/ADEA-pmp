package com.anycc.pmp.slas.service.impl;

import com.anycc.common.CommonUtils;
import com.anycc.common.ExportTableUtil;
import com.anycc.common.TempAuxiliaryUtils;
import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.DictionaryColumn;
import com.anycc.common.dto.TableData;
import com.anycc.pmp.ptmt.dao.ProjectDAO;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.slas.entity.ProBrowser;
import com.anycc.pmp.slas.entity.ProStatistics;
import com.anycc.pmp.slas.service.ProBrowserService;
import com.anycc.pmp.slas.service.ProStatisticsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
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


@Service
@Transactional
public class ProBrowserServiceImpl implements ProBrowserService {

    @Autowired
    private TempAuxiliaryUtils<Project> tempAuxiliaryUtils;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private CommonUtils<Project> commonUtils;


    @Override
    public List<Project> initCharts(String orgId) {

        try {
            String sql = "from Project p where p.webOrganization.id=?1 or p.webOrganization.parentId=?2 and p.delTag=0";
            Query query = em.createQuery(sql, Project.class);
            query.setParameter(1, Long.parseLong(orgId));
            query.setParameter(2, Long.parseLong(orgId));
            List<Project> list = query.getResultList();

            List<DictionaryColumn> dl=new ArrayList<DictionaryColumn>();
            dl.add(new DictionaryColumn("status", "statusName", "项目状态"));
            commonUtils.convertDictionaryList(list, dl);

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


	@Override
	public ResponseEntity<byte[]> exportResourceList(HttpServletRequest request)
			throws IOException {
		List<String> header = new ArrayList<String>();
		header.add("所属地区");
		header.add("所属公司");
		header.add("项目编号");
		header.add("项目名称");
		header.add("项目创建人");
		header.add("项目进度");
		header.add("项目等级");
		header.add("项目金额(万元)");
		header.add("状态");
		header.add("当前阶段");
		String sql = "select sa.name saname,so.organname,p.code,p.name,su.realname, "
				   + "IFNULL(concat(p.progress,'%'),0),dst.name as dstname,p.amt,drd.name as drdname, "
				   + "case when ps.expendtime is null then '未开始' "
				   + "when ps.expendtime<DATE_FORMAT(NOW(),'%Y-%m-%d') then concat(dnd.name,'(已延误)') "
				   + "else dnd.name  end  as stage "
	 			   + "FROM projectinfo p "
	 			   + "LEFT JOIN sys_area sa ON p.area_id = sa.id " 
	               + "LEFT JOIN sys_organization so ON p.org_id = so.id "
	 			   + "LEFT JOIN projectstagedivision ps on p.stage = ps.sid "
	 			   + "LEFT JOIN sys_user su on su.id = p.cid "
	               + "LEFT JOIN sys_dictionary dst on dst.parent_id = '25938' and p.level = dst.value "   //等级
	               + "LEFT JOIN sys_dictionary dnd on dnd.parent_id = '25942' and ps.sname = dnd.value "  //阶段
	               + "LEFT JOIN sys_dictionary drd on drd.parent_id = '25958' and p.status = drd.value "  //状态
	               + "where del_tag='0'  ";

		 if (request.getParameter("exportyw")!=null && !"".equals(request.getParameter("exportyw"))) {
	         sql += " AND ps.expendtime < DATE_FORMAT(NOW(),'%Y-%m-%d') ";
	     }
		 if (request.getParameter("exportwyw")!=null && !"".equals(request.getParameter("exportwyw"))) {
	         sql += " AND ps.expendtime >= DATE_FORMAT(NOW(),'%Y-%m-%d') ";
	     }
		 if (request.getParameter("exportstarttime")!=null && !"".equals(request.getParameter("exportstarttime"))) {
	         sql += " AND p.ctime >= '"+request.getParameter("exportstarttime")+"' ";
	     }
		 if (request.getParameter("exportendtime")!=null && !"".equals(request.getParameter("exportendtime"))) {
	         sql += " AND p.ctime <= '"+request.getParameter("exportendtime")+"' ";
	     }
		 if (request.getParameter("exportcompany")!=null && !"".equals(request.getParameter("exportcompany"))) {
	         sql += " AND p.org_id in (" + request.getParameter("exportcompany") + ")";
	     }
	     if (request.getParameter("exportstatus")!=null && !"".equals(request.getParameter("exportstatus"))) {
	         sql += " AND p.status = '" + request.getParameter("exportstatus") + "'";
	     }
	     if (request.getParameter("exportstage")!=null && !"".equals(request.getParameter("exportstage"))) {
	         sql += " AND ps.sname = '" + request.getParameter("exportstage")+ "'";
	     }
	         sql += " order by p.code  "; 
			Query query = null;
			query = em.createNativeQuery(sql);
			List<Object[]> data = new ArrayList<Object[]>();
			data = query.getResultList();

			TableData tableData = new TableData();//表格对象
			tableData.setHeader(header);//列表头
			tableData.setData(data);//列表数据
			tableData.setFilename("项目一览.xlsx");//下载的文件名,默认table.xlsx
			return ExportTableUtil.excelSave(tableData, request);
	}
}
