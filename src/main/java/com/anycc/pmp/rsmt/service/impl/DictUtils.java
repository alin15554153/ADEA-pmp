package com.anycc.pmp.rsmt.service.impl;

import com.anycc.common.dto.DictionaryColumn;
import com.anycc.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Shibin on 2016/3/23.
 */
@Component
public class DictUtils<T> {
    @Autowired
    private DictionaryService dictionaryService;

    public DictUtils() {
    }

    public Page<T> convertDictionaryPage(Page<T> page, List<DictionaryColumn> dictColumns) {
        try{
            for (T entity : page.getContent()) {
                for (DictionaryColumn dictColumn : dictColumns) {
                    Field field = entity.getClass().getDeclaredField(dictColumn.getColumnCNName());
                    Method setMethod = entity.getClass().getMethod(
                            "set" + captureName(dictColumn.getColumnCNName()), field.getType());

                    //支持A.B
                    String[] names = StringUtils.split(dictColumn.getColumnIDName(), ".");
                    Method getMethod = null;
                    Object objectValue = entity;


                    for (String name: names) {
                        if (objectValue!=null) {
                            getMethod = objectValue.getClass().getMethod("get" + captureName(name), new Class[0]);
                            objectValue = getMethod.invoke(objectValue, new Object[0]);
                        }
                    }
					/*Method getMethod = entity.getClass().getMethod(
	                        "get" + captureName(dictColumn.getColumnIDName()));*/
                    String dictValue="未对应";
                    if(objectValue!=null && !objectValue.equals("")) {
                        dictValue=dictionaryService.getNameByThemeAndValue(dictColumn.getTheme(),objectValue.toString());
                    }
                    setMethod.invoke(entity,dictValue);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }

    public Page<T> convertDictionaryPage(Page<T> page, DictionaryColumn dictColumn) {
        try{
            for (T entity : page.getContent()) {
                Field field = entity.getClass().getDeclaredField(dictColumn.getColumnCNName());
                Method setMethod = entity.getClass().getMethod(
                        "set" + captureName(dictColumn.getColumnCNName()), field.getType());

                //支持A.B形式
                String[] names = StringUtils.split(dictColumn.getColumnIDName(), ".");
                Method getMethod = null;
                Object objValue = entity;
                for (String name : names) {
                    if (objValue!=null) {
                        getMethod = objValue.getClass().getMethod("get" + captureName(name), new Class[0]);
                        objValue = getMethod.invoke(objValue, new Object[0]);
                    }
                }

				/*Method getMethod = entity.getClass().getMethod(
                        "get" + captureName(dictColumn.getColumnIDName()));
				Object objValue=getMethod.invoke(entity);*/
                String dictValue="未对应";
                if(objValue!=null && !objValue.equals(""))
                    dictValue=dictionaryService.getNameByThemeAndValue(dictColumn.getTheme(),objValue.toString());
                setMethod.invoke(entity,dictValue);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }

    public static String captureName(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=(cs[0]>96&&cs[0]<123)?32:0;
        return String.valueOf(cs);
    }

}
