package com.anycc.pmp.slas.service.impl;

import com.anycc.common.CommonUtils;
import com.anycc.common.ExportTableUtil;
import com.anycc.common.TempAuxiliaryUtils;
import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.DictionaryColumn;
import com.anycc.common.dto.TableData;
import com.anycc.dao.DictionaryDAO;
import com.anycc.entity.main.Dictionary;
import com.anycc.pmp.ptmt.dao.ProjectDAO;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.slas.entity.ProStatistics;
import com.anycc.pmp.slas.service.ProStatisticsService;

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


@Service
@Transactional
public class ProStatisticsServiceImpl implements ProStatisticsService {

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private TempAuxiliaryUtils<Project> tempAuxiliaryUtils;

    @PersistenceContext
    private EntityManager em;
    
    @Autowired
	private DictionaryDAO dictionaryDAO;

    @Override
    public DTData<ProStatistics> queryByPaging(ProStatistics proStatistics, DTPager pager) {

        /*String sql = "SELECT " +
                "sa.name saname," +
                "so.organname," +
                "p.name, " +
                "r.rCount," +
                "r.totalTimes, " +
                "p.id " +
                "FROM projectinfo p " +
                "LEFT JOIN sys_area sa ON p.area_id = sa.id " +
                "LEFT JOIN sys_organization so ON p.org_id = so.id " +
                "LEFT JOIN (select pid, count(*) rCount,sum(times) totalTimes  from resource) r ON r.pid = p.id" +
                " WHERE 1=1 ";*/
    	String sql = "select sa.name saname,so.organname,p.type,ps.sname ,count(*),p.stage "
    			   + "FROM projectinfo p "
    			   + "LEFT JOIN sys_area sa ON p.area_id = sa.id " 
                   + "LEFT JOIN sys_organization so ON p.org_id = so.id "
    			   + "LEFT JOIN projectstagedivision ps on p.stage=ps.sid where del_tag='0'  ";
    	
    	if (proStatistics.getStarttime()!=null && !"".equals(proStatistics.getStarttime())) {
            sql += " AND p.ctime >= '" + proStatistics.getStarttime() + "'";
        }
    	if (proStatistics.getEndtime()!=null && !"".equals(proStatistics.getEndtime())) {
            sql += " AND p.ctime <= '" + proStatistics.getEndtime() + "'";
        }
    	if (proStatistics.getOrgids()!=null && !"".equals(proStatistics.getOrgids())) {
            sql += " AND p.org_id in (" + proStatistics.getOrgids() + ")";
        }
        if (proStatistics.getType()!=null && !"".equals(proStatistics.getType())) {
            sql += " AND p.type = '" + proStatistics.getType() + "'";
        }
        if (proStatistics.getStage()!=null && !"".equals(proStatistics.getStage())) {
            sql += " AND ps.sname = '" + proStatistics.getStage()+ "'";
        }
            sql += " group by p.area_id,p.org_id,p.type,ps.sname  ";   

        String sqlCnt = "SELECT COUNT(*) FROM ("+sql+") cnt";
        Query queryCnt = em.createNativeQuery(sqlCnt);
        List listCnt = queryCnt.getResultList();
        int cnt = Integer.valueOf(listCnt.get(0).toString());

        List<ProStatistics> list = new ArrayList<ProStatistics>();
        Query query = em.createNativeQuery(sql);
        int pageNumber = pager.getStart();
        int pageSize = pager.getLength();
        query.setFirstResult(pageNumber);
        query.setMaxResults(pageSize);
        List rows = query.getResultList();
        for (Object object : rows) {
            Object[] cells = (Object[]) object;
            ProStatistics temp = new ProStatistics();
            temp.setArea(cells[0] != null ? cells[0].toString() : "");
            temp.setCompany(cells[1] != null ? cells[1].toString() : "");
            //项目类别
            String type = cells[2] != null ? cells[2].toString() : "";
            String typeName = getNameWithValue("项目类别",type,"未分类");
            temp.setType(typeName);
            //阶段名称
            String stage = cells[3] != null ? cells[3].toString() : "";
            String stageName = getNameWithValue("阶段名称",stage,"未开始");
            temp.setStage(stageName);
            temp.setTotalTimes(cells[4] != null ? cells[4].toString() : "");
            list.add(temp);
        }
        
        Page<ProStatistics> resourcePage = new PageImpl<ProStatistics>(list, tempAuxiliaryUtils.buildPageRequest(pager),cnt);
        return new DTData<ProStatistics>(resourcePage, pager);
    }
    
    public String getNameWithValue(String theme,String value,String defaultvalue){
        Dictionary dict = dictionaryDAO.getNameByThemeAndValue(theme,value);
        if (dict != null && dict.getId() > 0) {
   		 return dict.getName();
   		}else{
   			return defaultvalue;
   		}
    }

	@Override
	public ResponseEntity<byte[]> exportResourceList(
			HttpServletRequest request)
			throws IOException {
		
		List<String> header = new ArrayList<String>();
		header.add("所属地区");
		header.add("所属公司");
		header.add("项目类别");
		header.add("项目阶段");
		header.add("项目数量");
		String sql = "select sa.name saname,so.organname,dst.name,ifnull(dnd.name,'未开始') ,count(*) "
 			   + "FROM projectinfo p "
 			   + "LEFT JOIN sys_area sa ON p.area_id = sa.id " 
               + "LEFT JOIN sys_organization so ON p.org_id = so.id "
 			   + "LEFT JOIN projectstagedivision ps on p.stage=ps.sid "
               + "LEFT JOIN sys_dictionary dst on dst.parent_id='25930' and p.type=dst.value "
               + "LEFT JOIN sys_dictionary dnd on dnd.parent_id='25942' and ps.sname=dnd.value "
               + "where del_tag='0'  ";
	if (request.getParameter("exportstarttime")!=null && !"".equals(request.getParameter("exportstarttime"))) {
	     sql += " AND p.ctime >= '"+request.getParameter("exportstarttime")+"' ";
	}
	if (request.getParameter("exportendtime")!=null && !"".equals(request.getParameter("exportendtime"))) {
	     sql += " AND p.ctime <= '"+request.getParameter("exportendtime")+"' ";
	 }
 	 if (request.getParameter("exportOrgIds")!=null && !"".equals(request.getParameter("exportOrgIds"))) {
         sql += " AND p.org_id in (" + request.getParameter("exportOrgIds") + ")";
     }
     if (request.getParameter("exportType")!=null && !"".equals(request.getParameter("exportType"))) {
         sql += " AND p.type = '" + request.getParameter("exportType") + "'";
     }
     if (request.getParameter("exportStage")!=null && !"".equals(request.getParameter("exportStage"))) {
         sql += " AND ps.sname = '" + request.getParameter("exportStage")+ "'";
     }
         sql += " group by p.area_id,p.org_id,p.type,ps.sname  "; 
		Query query = null;
		query = em.createNativeQuery(sql);
		List<Object[]> data = new ArrayList<Object[]>();
		data = query.getResultList();

		TableData tableData = new TableData();//表格对象
		tableData.setHeader(header);//列表头
		tableData.setData(data);//列表数据
		tableData.setFilename("项目统计.xlsx");//下载的文件名,默认table.xlsx
		return ExportTableUtil.excelSave(tableData, request);
	}
	
}
