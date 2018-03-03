package com.anycc.pmp.util.excel;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 导出打印实体类
 * Created by aoc on 2016/7/1.
 */
public class ExcelEntity {



    private String title;   //显示的导出标题
    private String type;    //类型
    private List<String> rowNames = new ArrayList<String>();//导出表的列名
    private List<String> fields = new ArrayList<String>();    //导出对象属性名
    private List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>(); //数据

    private List<InfoEntity> info = new ArrayList<>();  //基本信息

    public ExcelEntity() {

    }

    public  Map<String,Object> transform(Object o){
        Field ofields[]   =   o.getClass().getDeclaredFields();
        String[]   name   =   new   String[ofields.length];
        Object[]   value   =   new   Object[ofields.length];
        Map<String,Object> map = new HashMap<>();
        try{
            Field.setAccessible(ofields,   true);
            for   (int   i   =   0;   i   <   name.length;   i++)   {
                name[i]   =   ofields[i].getName();
                value[i]   =   ofields[i].get(o);
                //System.out.println( name[i] +" "+value[i]);
                map.put(name[i], value[i]);
            }
            datas.add(map);
        }
        catch(Exception   e){
            e.printStackTrace();
        }
        return  map;
    }

    public void initField(Object o){
        Field ofields[]   =   o.getClass().getDeclaredFields();
        String[]   name   =   new   String[ofields.length];
        try{
            Field.setAccessible(ofields,   true);
            for   (int   i   =   0;   i   <   name.length;   i++)   {
                name[i]   =   ofields[i].getName();
                fields.add(name[i]);
            }
        }
        catch(Exception   e){
            e.printStackTrace();
        }
    }

    public void addField(Map<String,String> addMap){
        Iterator<String> it = addMap.keySet().iterator();
        while (it.hasNext()){
            String key = it.next();
            fields.add(key);
            Map<String,Object> map = new HashMap<>();
            map.put(key, addMap.get(key));
            datas.add(map);
        }
    }

    public  void delField(String name){
        fields.remove(name);
       for (Map<String,Object> map:datas){
           map.remove(name);
       }
    }

    public ExcelEntity(HttpServletRequest request) {
        String title = request.getParameter("title");
        String type = request.getParameter("type");
        String rowStr = request.getParameter("rowsName");
        String[] rowsName = rowStr != null ? rowStr.split(",") : new String[]{};
        String fieldStr = request.getParameter("fields");
        String[] fields = fieldStr.split(",");

        String infoStr = request.getParameter("infoData");
        String[] infos = infoStr != null ? infoStr.split(",") : new String[]{};
        List<InfoEntity> infoData = new ArrayList<>();
        for (String info : infos) {
            InfoEntity map = new InfoEntity();
            map.setName(info.split("=")[0]);
            map.setValue(info.split("=")[1]);
            infoData.add(map);
        }

        this.setTitle(title);
        this.setType(type);
        this.setRowNames(Arrays.asList(rowsName));
        this.setFields(Arrays.asList(fields));
        this.setInfo(infoData);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getRowNames() {
        return rowNames;
    }

    public void setRowNames(List<String> rowNames) {
        this.rowNames = rowNames;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<Map<String,Object>> getDatas() {
        return datas;
    }

    public void setDatas(List<Map<String,Object>> datas) {
        this.datas = datas;
    }

    public List<InfoEntity> getInfo() {
        return info;
    }

    public void setInfo(List<InfoEntity> info) {
        this.info = info;
    }


}

class InfoEntity {

    public InfoEntity() {

    }
    public InfoEntity(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    private String name;
    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}