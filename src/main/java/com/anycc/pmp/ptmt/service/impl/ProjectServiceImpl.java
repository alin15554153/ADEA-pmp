package com.anycc.pmp.ptmt.service.impl;

import com.anycc.commmon.web.dao.WebUserDAO;
import com.anycc.commmon.web.entity.WebUser;
import com.anycc.common.CommonUtils;
import com.anycc.common.TempAuxiliaryUtils;
import com.anycc.common.dto.*;
import com.anycc.dao.DictionaryDAO;
import com.anycc.entity.main.Dictionary;
import com.anycc.pmp.api.service.ApiService;
import com.anycc.pmp.ptmt.dao.ProjectDAO;
import com.anycc.pmp.ptmt.dao.ProjectMemberDao;
import com.anycc.pmp.ptmt.dao.ProjectStageDAO;
import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.ptmt.entity.ProjectMember;
import com.anycc.pmp.ptmt.service.ProjectService;
import com.anycc.pmp.util.DateUtil;
import com.anycc.pmp.util.excel.ExcelEntity;
import com.anycc.pmp.util.excel.ExportExcel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("projectService")
@Transactional
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private ProjectStageDAO projectstageDAO;

    @Autowired
    private WebUserDAO webuserDAO;

    @Autowired
    private ProjectMemberDao projectMemberDao;

    @Autowired
    private TempAuxiliaryUtils<Project> tempAuxiliaryUtils;

    @Autowired
    private CommonUtils<Project> commonUtils;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ApiService apiService;

    @Autowired
    private DictionaryDAO dictionaryDAO;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.anycc.pmp.ptmt.service.ProjectService#queryByPaging(com.anycc.pmp
     * .ptmt.entity.Project,com.anycc.rhip.common.dto.DTPager)
     */
    @Override
    public DTData<Project> queryByPaging(Specification specification,
                                         DTPager pager) {
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "type"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "level"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "status"));
        Page<Project> projectPage = projectDAO.findAll(specification,
                tempAuxiliaryUtils.buildPageRequest(pager, new Sort(orders)));
        List<DictionaryColumn> list = new ArrayList<DictionaryColumn>();
        list.add(new DictionaryColumn("level", "levelName", "项目等级"));
        list.add(new DictionaryColumn("status", "statusName", "项目状态"));
        list.add(new DictionaryColumn("projectstage.sname", "stageName", "阶段名称", "未开始"));
        commonUtils.convertDictionaryPage(projectPage, list);
        return new DTData<Project>(projectPage, pager);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.anycc.pmp.ptmt.service.ProjectService#queryByPaging(com.anycc.pmp
     * .ptmt.entity.Project,com.anycc.rhip.common.dto.DTPager)
     */
    @Override
    public DTData<WebUser> queryuserlist(Specification specification,
                                         DTPager pager) {
        Page<WebUser> projectPage = webuserDAO.findAll(specification,
                tempAuxiliaryUtils.buildPageRequest(pager));
        return new DTData<WebUser>(projectPage, pager);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.anycc.pmp.ptmt.service.ProjectService#addProject(com.anycc.pmp.ptmt
     * .entity.Project)
     */
    @Override
    public Response addProject(Project project) {
        Assert.notNull(project);
        // String uupid = project.getUupid();
        // List<ProjectStage> projectstages = projectstageDAO.findByPid(uupid);
        /*
		 * if(projectstages.size()>0 && projectstages !=null){
		 * project.setProjectstage(projectstages.get(0)); }
		 */
        String code = getMaxProjectCode();
        project.setCode(code);
        projectDAO.save(project);
        if (!"".equals(project.getId())) {
            String pid = project.getId();
            String perids = project.getPerids();
            if (!"".equals(perids)) {
                String perid[] = perids.split(",");
                for (int i = 0; i < perid.length; i++) {
                    ProjectMember projectMember = new ProjectMember();
                    projectMember.setPid(pid);
                    projectMember.setUid(perid[i]);
                    projectMemberDao.save(projectMember);
                }
            }

			/*
			 * for(ProjectStage projectstage:projectstages){
			 * projectstage.setPid(pid); }
			 */
            // projectstageDAO.save(projectstages);
            //更新cmp的项目
            apiService.updateProject(project);
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
        // Assert.isTrue(id > 0);
        Assert.notNull(id);
        Project project = projectDAO.findOne(id);
        List<ProjectMember> projectMembers = projectMemberDao.findByPid(project
                .getId());
        String names = "";
        String ids = "";
        if (projectMembers.size() > 0 && projectMembers != null) {
            for (ProjectMember projectMember : projectMembers) {
                names += projectMember.getWebUser().getRealname() + ",";
                ids += projectMember.getWebUser().getId().toString() + ",";
            }
            names = names.substring(0, names.length() - 1);
            ids = ids.substring(0, ids.length() - 1);
        }
        project.setPernames(names);
        project.setPerids(ids);
        if (project != null && project.getId() != null) {
            return new ObjectResponse<Project>(project);
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
    public Response updateProject(Project project) {
        Assert.notNull(project);
        Assert.notNull(project.getId());
        Project oldproject = projectDAO.findOne(project.getId());
        project.setProjectstage(oldproject.getProjectstage());
        project.setWebOrganization(oldproject.getWebOrganization());
        project.setWebArea(oldproject.getWebArea());
        project.setCtime(oldproject.getCtime());
        project.setWebUser(oldproject.getWebUser());
        String perids = project.getPerids();
        String newper[] = perids.split(",");
        List<String> tempList = Arrays.asList(newper);
        List<String> params = new ArrayList<String>();
        List<ProjectMember> oldProjectMembers = projectMemberDao
                .findByPid(project.getId());
        String addstr = "";  //新增人员字符串（用于邮件）
        String delstr = "";  //删除人员字符串（用于邮件）
        String oldstr = "";  //原人员字符串（用于邮件）
        for (ProjectMember projectMember : oldProjectMembers) {
            params.add(projectMember.getUid());
            if (!tempList.contains(projectMember.getUid())) {// 需要删除的
                delstr += projectMember.getUid() + ",";
                projectMemberDao.delete(projectMember.getMid());
            } else {//保留的原参与人员
                oldstr += projectMember.getUid() + ",";
            }
        }
        if (!"".equals(perids)) {
            for (int i = 0; i < newper.length; i++) {
                if (!params.contains(newper[i])) {// 需要新增的
                    addstr += newper[i] + ",";
                    ProjectMember projectMember = new ProjectMember();
                    projectMember.setPid(project.getId());
                    projectMember.setUid(newper[i]);
                    projectMemberDao.save(projectMember);
                }
            }
        }
        projectDAO.save(project);
        //更新cmp的项目
        apiService.updateProject(project);
        if (oldstr.length() > 0)
            oldstr = oldstr.substring(0, oldstr.length() - 1);
        if (delstr.length() > 0)
            delstr = delstr.substring(0, delstr.length() - 1);
        if (addstr.length() > 0)
            addstr = addstr.substring(0, addstr.length() - 1);
        // 原来的-删除-新增-项目名字
        oldstr = oldstr + "-" + delstr + "-" + addstr + "-" + project.getName();
        return new ObjectResponse<String>(oldstr);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.anycc.pmp.ptmt.service.ProjectService#delete(java.lang.Long)
     */
    @Override
    public Response delete(String l) {
        //删除参与人员
		/*List<ProjectMember> projectMembers = projectMemberDao.findByPid(l);
		if (projectMembers.size() > 0 && projectMembers != null) {
			for (ProjectMember projectMember : projectMembers) {
				projectMemberDao.delete(projectMember.getMid());
			}
		}	
		projectDAO.delete(l);*/
        String sql1 = "update projectinfo set del_tag='1' where id='" + l + "' ";
        Query query1 = em.createNativeQuery(sql1);
        query1.executeUpdate();
        return new SuccessResponse();
    }

    @Override
    public List<Project> queryProjectList() {
        // TODO Auto-generated method stub
        String hql = "from Project p where p.status <> '99' ";
        List project = tempAuxiliaryUtils.findBySearch(hql);
        return project;
    }

    // 查询project List luqing(已使用，后续方法勿改)
    @Override
    public List<Project> queryProjectList2(String myId, String pid) {
        // TODO Auto-generated method stub
        String hql = "from Project p where p.delTag = '0' and p.webUser='"
                + myId + "' and id='" + pid + "'";
        List project = tempAuxiliaryUtils.findBySearch(hql);
        return project;
    }

    @Override
    public void export(ServletRequest request, HttpServletResponse response) {
        ExcelEntity excelEntity = new ExcelEntity();
        String title ="云擎项目汇总-"+ DateUtil.format(DateUtil.DATE_FORMAT,new Date());
        excelEntity.setTitle(title);
        List<String> rowsNames = new ArrayList<>();
        rowsNames.add("编号");
        rowsNames.add("来源单位");
        rowsNames.add("项目名称");
        rowsNames.add("总预算");
        rowsNames.add("总包");
        rowsNames.add("分包");
        rowsNames.add("状态");
        rowsNames.add("牵头人");
        rowsNames.add("预计签约");
        rowsNames.add("预计签约时间");
        rowsNames.add("协作单位");
        rowsNames.add("协作内容");
        rowsNames.add("备注");
        excelEntity.setRowNames(rowsNames);
        List<String> fields = new ArrayList<>();
        fields.add("index");
        fields.add("source");
        fields.add("name");
        fields.add("amt");
        fields.add("totalPackage");
        fields.add("subPackage");
        fields.add("status");
        fields.add("director");
        fields.add("budget");
        fields.add("expectedTime");
        fields.add("unit");
        fields.add("content");
        fields.add("remark");
        excelEntity.setFields(fields);


        String type = request.getParameter("type");
        if (StringUtils.isBlank(type)) {
            type = "1";
        }

        String sql = "SELECT id,project_source as source,name,amt,total_package as totalPackage,sub_package as subPackage,status,director,signing_budget as budget,expected_time as expectedTime,assistance_unit as unit,assistance_content as content from projectinfo  a LEFT JOIN (SELECT pid,max(create_time) as createTime from project_follow GROUP BY pid ) b on a.id=b.pid  WHERE type="+type+" and status!=4 and del_tag=0 ORDER BY b.createTime DESC";

        Query query = em.createNativeQuery(sql);
        query.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map> list = query.getResultList();
        Map<String,List<Integer>>  groupMap = new HashMap<>();//按项目来源分组
        List<String> sourceOreder = new ArrayList<>();
        int index = 0;
        for(Map map:list){
            String source ="noSource";
            if (null != map.get("source")){
                source=  map.get("source").toString();
            }
            List<Integer> group =groupMap.get(source);
            if(null==group){
                group= new ArrayList<>();
                groupMap.put(source,group);
            }
            group.add(index);
            if(!sourceOreder.contains(source)){
                sourceOreder.add(source);
            }
            index++;
        }

        List<Map> resultList = new ArrayList<>();
        for(String source:sourceOreder){
            List<Integer> group =groupMap.get(source);
            for(Integer i:group){
                resultList.add(list.get(i));
            }
        }



         index = 1;
        List<Dictionary> statusList = dictionaryDAO.findAllItems("项目状态");
        List<Dictionary> packetList = dictionaryDAO.findAllItems("项目总包");

        List<CellRangeAddress> cellRangeAddressesList = new ArrayList<>();
        int cellStart = 0;
        int cesllNum = 0;
        String source = "";
        List<Short> colorList = new ArrayList<>();
        short lastColor = HSSFColor.WHITE.index;
        for (Map map : resultList) {
            map.put("index", index);

            if (null != map.get("status")) {
                for (Dictionary dictionary : statusList) {
                    if (dictionary.getValue().equals(map.get("status").toString())) {
                        map.put("status", dictionary.getName());
                    }
                }
            }
            if (null != map.get("totalPackage")) {
                for (Dictionary dictionary : packetList) {
                    if (dictionary.getValue().equals(map.get("totalPackage").toString())) {
                        map.put("totalPackage", dictionary.getName());
                    }
                }
            }

            sql = "SELECT follow_time,content  FROM project_follow WHERE pid='" + map.get("id") + "' ORDER BY follow_time desc LIMIT 3 ";
            map.remove("id");
            query = em.createNativeQuery(sql);
            query.unwrap(SQLQuery.class)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Map> remarkList = query.getResultList();
            String remark = "";
            for (int i=0;i<remarkList.size();i++) {
                Map temp = remarkList.get(i);
                if(StringUtils.isBlank((String) temp.get("content"))){
                    continue;
                }
                remark += "[" + temp.get("follow_time") + "]" + temp.get("content");
                if(i<remarkList.size()-1){
                    remark+=(char)10;
                }
            }
            map.put("remark", remark);
            excelEntity.getDatas().add(map);


            if (null != map.get("source") && source.equals(map.get("source").toString())) {
                cesllNum++;
                index++;
                colorList.add(lastColor);
              if(index>resultList.size()){
                  if (cesllNum > 0) {
                      CellRangeAddress cra = new CellRangeAddress(cellStart, cellStart + cesllNum, 1, 1);
                      cellRangeAddressesList.add(cra);
                  }
              }
                continue;
            }

            if (cesllNum > 0) {
                CellRangeAddress cra = new CellRangeAddress(cellStart, cellStart + cesllNum, 1, 1);
                cellRangeAddressesList.add(cra);
            }
            cellStart = index + 2;
            cesllNum = 0;
            if (null != map.get("source")) {
                source = map.get("source").toString();
            }
            if (lastColor == HSSFColor.WHITE.index) {
                lastColor = IndexedColors.LIGHT_GREEN.getIndex();
            } else {
                lastColor = HSSFColor.WHITE.index;
            }
            index++;
            colorList.add(lastColor);

        }

        ExportExcel ex = new ExportExcel(excelEntity);
        try {
            ex.setFileName(title+".xls");
            ex.setMergedList(cellRangeAddressesList);
            ex.setColorList(colorList);
            ex.export(response);
            //ex.export("d:");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //生成新的项目编号
    public String getMaxProjectCode() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String dateString = formatter.format(currentTime);
        String code = "";
        if (projectDAO.queryMaxCode() == null) {//没有数据
            code = "P-" + dateString + "-01";
        } else if (dateString.equals(projectDAO.queryMaxCode().substring(2, 8))) {
            //本日已创建项目
            String num = projectDAO.queryMaxCode().substring(9, 11);
            int num_int = Integer.parseInt(num) + 1;
            if (num_int < 10)
                num = "-0" + num_int;
            else num = "-" + num_int;
            code = "P-" + dateString + num;
        } else {//本日未创建项目
            code = "P-" + dateString + "-01";
        }
        return code;
    }

    @Override
    public Project get(String id) {
        Assert.notNull(id);
        Project project = projectDAO.findOne(id);
        return project;
    }

}
