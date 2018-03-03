
package ${pknService};

import net.hp.es.adm.healthcare.rphcp.common.dto.DTData;
import net.hp.es.adm.healthcare.rphcp.common.dto.DTPager;
import net.hp.es.adm.healthcare.rphcp.common.dto.Response;
import ${pknEntity}.${className};

public interface ${className}Service {
		
	DTData<${className}> queryByPaging(${className} ${instanceName}, DTPager pager);

	Response add${className}(${className} ${instanceName});

	Response update${className}(${className} ${instanceName});

	Response queryById(long id);

	Response delete(long l);
		
}
