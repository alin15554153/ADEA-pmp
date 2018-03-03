package com.anycc.pmp.api.service;

import com.anycc.pmp.ptmt.entity.Project;
import com.anycc.pmp.util.EncryptUtil;
import com.anycc.pmp.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by DELL on 2016/11/1.
 */
@Service("ApiService")
public class ApiService {

    public final String secret = "CmpAndPmp";

    @PersistenceContext
    private EntityManager em;

    //public final String cmpUrl = "http://192.168.18.32:8006/cmp/home/api/";
    public final String cmpUrl = "http://58.220.249.173:8888/cmp/home/api/";

    private static final Logger log = LoggerFactory.getLogger(ApiService.class);

    private ExecutorService exec;

    @PostConstruct
    public void init() {
        log.debug("ApiService init ");
        exec = Executors.newFixedThreadPool(3);
    }


    public List projectProgress() {
        String sql = "SELECT code,name,progress,`status` FROM projectinfo WHERE `status` ='2' AND del_tag=0 and type=2 ORDER BY ctime desc";
        Query query = em.createNativeQuery(sql);
        query.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }


    public void updateProject(Project project) {
        exec.execute(new updateProjectThread(project));
    }

    /**
     * 更新cmp的项目进度
     *
     * @return
     */
    private class updateProjectThread implements Runnable {
        private Project project;

        public updateProjectThread(Project project) {
            this.project = project;
        }

        @Override
        public void run() {
            StringBuilder sb=new StringBuilder();
            sb.append("time="+ System.currentTimeMillis());
            sb.append("&pcode="+project.getCode());
            sb.append("&status="+project.getStatus());
            sb.append("&pname="+project.getName());
            String param = sb.toString();
            String md5 = EncryptUtil.getMD5Str(param + secret).toLowerCase();
            String base64Encode = EncryptUtil.BASE64Encode(param);
            log.debug("真是的参数："+param+"   base64的参数:"+base64Encode+"   MD5："+md5);
            String url = cmpUrl + md5 + "/updateProject";
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("param",base64Encode);
            HttpUtil.doPost(url,paramMap,null);
        }


    }

    /**
     * 检查是否合法
     *
     * @param md5
     * @return
     */
    public Map<String, Object> check(String param, String md5) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "ok");
        if (StringUtils.isEmpty(param)) {
            result.put("code", 404);
            result.put("msg", "param not find");
            return result;
        }
        String temp = EncryptUtil.BASE64Decode(param);
        if (!EncryptUtil.getMD5Str(temp + secret).toLowerCase().equals(md5)) {
            result.put("code", 405);
            result.put("msg", "secret not find");
            return result;
        }

        String[] params = temp.split("&");
        for (String value : params) {
            String[] values = value.split("=");
            if (values.length != 2) {
                continue;
            }
            result.put(values[0], values[1]);
        }

        String time = (String) result.get("time");
        if (StringUtils.isEmpty(time) || !StringUtils.isNumeric(time)) {
            result.put("code", 404);
            result.put("msg", "time not find");
            return result;
        }
        int minute = (int) (Math.abs(System.currentTimeMillis() - Long.parseLong(time)) / 1000 / 60);
        if (minute > 3) {
            result.put("code", 406);
            result.put("msg", "time out");
            return result;
        }
        return result;
    }
}
