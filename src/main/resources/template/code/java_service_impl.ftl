
package	${pknServiceImpl};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


import ${pknEntity}.${className};
import net.hp.es.adm.healthcare.rphcp.common.TempAuxiliaryUtils;
import net.hp.es.adm.healthcare.rphcp.common.dto.DTData;
import net.hp.es.adm.healthcare.rphcp.common.dto.DTPager;
import net.hp.es.adm.healthcare.rphcp.common.dto.FailedResponse;
import net.hp.es.adm.healthcare.rphcp.common.dto.ObjectResponse;
import net.hp.es.adm.healthcare.rphcp.common.dto.Response;
import net.hp.es.adm.healthcare.rphcp.common.dto.SuccessResponse;
import ${pknDAO}.${className}DAO;
import ${pknService}.${className}Service;

@Service("${instanceName}Service")
@Transactional
public class ${className}ServiceImpl implements ${className}Service {
	
	@Autowired
	private ${className}DAO ${instanceName}DAO;
	@Autowired
	private TempAuxiliaryUtils<${className}> tempAuxiliaryUtils;

	/*
	 * (non-Javadoc)
	 * @see ${pknService}.${className}Service#queryByPaging(${pknEntity}.${className},net.hp.es.adm.healthcare.rphcp.common.dto.DTPager)  
	 */ 
	@Override
	public DTData<${className}> queryByPaging(${className} ${instanceName}, DTPager pager) {
		Page<${className}> ${instanceName}Page = ${instanceName}DAO.findAll(tempAuxiliaryUtils.buildWhereClause(${instanceName}), tempAuxiliaryUtils.buildPageRequest(pager));
		return new DTData<${className}>(${instanceName}Page, pager);
	}
	
	
	/*
	 * (non-Javadoc) 
	 * @see ${pknService}.${className}Service#add${className}(${pknEntity}.${className})  
	 */
	@Override
	public Response add${className}(${className} ${instanceName}) {
		Assert.notNull(${instanceName});
		${instanceName}DAO.save(${instanceName});
		if (${instanceName}.getId() > 0) {
			return new SuccessResponse();
		} else {
			return new FailedResponse("后台保存${instanceName}失败!");
		}
	}

    /*
	 * (non-Javadoc)
	 * @see ${pknService}.${className}Service#queryById(java.lang.Long)  
	 */
	@Override
	public Response queryById(long id) {
		Assert.isTrue(id > 0);
		${className} ${instanceName} = ${instanceName}DAO.findOne(id);
		if (${instanceName} != null && ${instanceName}.getId() > 0) {
			return new ObjectResponse<${className}>(${instanceName});
		} else {
			return new FailedResponse("后台查询ID为[" + id + "]的信息失败！");
		}
	}
    
    
     /*
	 * (non-Javadoc)
	 * @see ${pknService}.${className}Service#update${className}(java.lang.Long)  
	 */
	@Override
	public Response update${className}(${className} ${instanceName}) {
		Assert.notNull(${instanceName});
		Assert.notNull(${instanceName}.getId());
		${instanceName}DAO.save(${instanceName});
		return new SuccessResponse();
	}
	
	 /*
	 * (non-Javadoc)
	 * @see ${pknService}.${className}Service#delete(java.lang.Long)  
	 */
	@Override
	public Response delete(long l) {
		// TODO Auto-generated method stub
		${instanceName}DAO.delete(l);
		return new SuccessResponse();
	}
	    
}
