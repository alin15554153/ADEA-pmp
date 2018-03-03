
package com.anycc.pmp.rsmt.service;

import com.anycc.common.dto.DTData;
import com.anycc.common.dto.DTPager;
import com.anycc.common.dto.Response;
import com.anycc.pmp.rsmt.entity.Resource;
import org.springframework.data.jpa.domain.Specification;

public interface ResourceService {
		
	DTData<Resource> queryByPaging(Resource resource, DTPager pager);

	DTData<Resource> queryByPaging(Specification specification, DTPager pager);

	Response addResource(Resource resource);

	Response updateResource(Resource resource);

	Response queryById(String id);

	Response delete(String l);
		
}
