
package	com.anycc.pmp.rsmt.service.impl;

import com.anycc.common.CommonUtils;
import com.anycc.common.TempAuxiliaryUtils;
import com.anycc.common.dto.*;
import com.anycc.entity.main.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


import com.anycc.pmp.rsmt.entity.Resource;
import com.anycc.pmp.rsmt.dao.ResourceDAO;
import com.anycc.pmp.rsmt.service.ResourceService;

import javax.persistence.Transient;
import javax.persistence.criteria.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service("resourceService")
@Transactional
public class ResourceServiceImpl implements ResourceService {
	
	@Autowired
	private ResourceDAO resourceDAO;

	@Autowired
	private TempAuxiliaryUtils<Resource> tempAuxiliaryUtils;

	@Autowired
	private CommonUtils<Resource> commonUtils;
	/*
	 * (non-Javadoc)
	 * @see com.anycc.pmp.rsmt.service.ResourceService#queryByPaging(com.anycc.pmp.rsmt.entity.Resource,com.anycc.rhip.common.dto.DTPager)  
	 */ 
	@Override
	public DTData<Resource> queryByPaging(Resource resource, DTPager pager) {
		Page<Resource> resourcePage = resourceDAO.findAll(tempAuxiliaryUtils.buildWhereClause(resource), tempAuxiliaryUtils.buildPageRequest(pager));
		return new DTData<Resource>(resourcePage, pager);
	}

	@Override
	public DTData<Resource> queryByPaging(Specification specification, DTPager pager) {
		Page<Resource> resourcePage = resourceDAO.findAll(specification, tempAuxiliaryUtils.buildPageRequest(pager));

		List<DictionaryColumn> list = new ArrayList<>();
		list.add(new DictionaryColumn("type", "typeName", "资源类别"));
		list.add(new DictionaryColumn("projectStage.sname", "stageName", "阶段名称"));
		commonUtils.convertDictionaryPage(resourcePage, list);
		return new DTData<Resource>(resourcePage, pager);
	}


	/*
	 * (non-Javadoc) 
	 * @see com.anycc.pmp.rsmt.service.ResourceService#addResource(com.anycc.pmp.rsmt.entity.Resource)  
	 */
	@Override
	public Response addResource(Resource resource) {
		Assert.notNull(resource);
		resourceDAO.save(resource);
		if (resource.getId() != null) {
			return new SuccessResponse();
		} else {
			return new FailedResponse("后台保存resource失败!");
		}
	}

    /*
	 * (non-Javadoc)
	 * @see com.anycc.pmp.rsmt.service.ResourceService#queryById(java.lang.Long)  
	 */
	@Override
	public Response queryById(String id) {
		Assert.isTrue(id != "");
		Resource resource = resourceDAO.findOne(id);
		if (resource != null && resource.getId() != "") {
			return new ObjectResponse<Resource>(resource);
		} else {
			return new FailedResponse("后台查询ID为[" + id + "]的信息失败！");
		}
	}
    
    
     /*
	 * (non-Javadoc)
	 * @see com.anycc.pmp.rsmt.service.ResourceService#updateResource(java.lang.Long)  
	 */
	@Override
	public Response updateResource(Resource resource) {
		Assert.notNull(resource);
		Assert.notNull(resource.getId());
		Resource r = resourceDAO.findOne(resource.getId());
		BeanUtil.copy(resource, r);
		r.setUploaddate(new Date());
		resourceDAO.save(r);
		return new SuccessResponse();
	}
	
	 /*
	 * (non-Javadoc)
	 * @see com.anycc.pmp.rsmt.service.ResourceService#delete(java.lang.Long)  
	 */
	@Override
	public Response delete(String l) {
		// TODO Auto-generated method stub
		resourceDAO.delete(l);
		return new SuccessResponse();

	}

	/**
	 * 属性值复制，参考
	 * @See org.springframework.beans.BeanUtils;
	 */
	public static class BeanUtil {
		/**
		 * get,set方法的最小长度 -1
		 */
		private static final int SET_START = "set".length();

		/**
		 * is方法最小长度
		 */
		private static final int IS_START = "is".length();

		/**
		 * 把source的属性的值拷贝给target同名属性
		 * @param source
		 * @param target
		 */
		public static void copy(Object source,Object target) {
			try {
				//得到source的所有方法
				Method[] methods = source.getClass().getMethods();
				//遍历所有方法找出source所有get方法
				for(Method method : methods) {
					//得到get方法
					if(isGetter(method)) {
						//得到get方法的返回值类型
						Class<?> returnType = method.getReturnType();
						//得到source对象get方法的返回值
						Object result = method.invoke(source);
						//不拷贝source值==null属性
						if(null == result) {
							continue;
						}
						//空集合不拷贝
						if(result instanceof Collection) {
							Collection collection = (Collection) result;
							if(collection.size() <= 0) {
								continue;
							}
						}
						//瞬时属性不拷贝
						if(method.isAnnotationPresent(Transient.class)) {
							continue;
						}
						//通过get方法得到set方法名字
						String setterName = getter2Setter(method.getName());
						//得到set方法，set方法的参数类型==get方法的返回值类型
						Method setter = getMethod(source.getClass(),setterName,returnType);
						//把source对象get方法返回值，拷贝给对象的target对象的setter方法
						setter.invoke(target, result);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * 得到指定的方法
		 * @param clz	方法的类型
		 * @param methodName	方法的名字
		 * @param paramTypes	方法的参数列表
		 * @return
		 */
		public static Method getMethod(Class<? extends Object> clz,
									   String methodName, Class<?>...paramTypes) {
			Method method = null;
			try {
				method = clz.getMethod(methodName, paramTypes);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return method;
		}

		/**
		 * getter方法名转换成setter方法名
		 * @param methodName	getter方法名
		 * @return	setter方法名isUserName setUserName
		 */
		public static String getter2Setter(String methodName) {
			if(methodName.startsWith("get")) {
				return "s" + methodName.substring(1);
			} else if(methodName.startsWith("is")) {
				return "set" + methodName.substring(2);
			} else {
				throw new IllegalArgumentException("method not start with get or is.");
			}
		}

		/**
		 * 判断方法名是否符合getter。
		 * 	1，方法名以get开头，并在3个字符以上
		 * 	2，方法名以is开头，并在2个字符以上
		 * 	3，方法没有参数
		 * @param method
		 * @return
		 */
		public static boolean isGetter(Method method) {
			String name = method.getName();
			//方法没有参数
			boolean hasNoParam = method.getParameterTypes().length==0;
			//方法有返回值
			boolean noResultType = method.getReturnType() != void.class;
			//方法方法名以get开头，大于3位
			boolean startWithGet = (name.length() > SET_START) && name.startsWith("get");
			//方法以is开头，大于2位
			boolean startWithIs = (name.length() > IS_START) && name.startsWith("is");
			//不是getClass方法
			boolean notGetClass = !name.equals("getClass");

			return hasNoParam && noResultType && notGetClass && (startWithGet || startWithIs);
		}

		/**
		 * 判断方法名是否符合setter. 1.方法名是3个字符以上 2.方法名以set开头 3.方法只有一个参数 满足这三个条件，就被认为是setter
		 *
		 * @param method
		 *            方法
		 * @return boolean
		 */
		public static boolean isSetter(Method method) {
			String name = method.getName();
			boolean hasOneParam = method.getParameterTypes().length == 1;
			boolean startsWithGet = (name.length() > SET_START)
					&& name.startsWith("set");

			return startsWithGet && hasOneParam;
		}

	}
	    
}
