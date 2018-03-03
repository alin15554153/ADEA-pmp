
package ${pknDAO};

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import ${pknEntity}.${className};

public interface ${className}DAO extends JpaRepository<${className}, Long>, JpaSpecificationExecutor<${className}> {
}