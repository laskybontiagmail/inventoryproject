package com.aiur.ejb.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.aiur.common.DatasourceConfiguration;
import com.aiur.common.service.ServiceManager;
import com.aiur.constant.common.errors.InternalErrorException;
import com.aiur.entity.Department;
import com.aiur.factory.EntityManagerHandlerFactory;
import com.aiur.model.rest.Response;
import com.aiur.repository.DepartmentRepository;
import com.aiur.repository.DepartmentRepositoryImplementation;

@Startup
@Singleton
public class TestStartupBeanImplementation extends ServiceManager { //although no interface yet
	private static Logger logger = LogManager.getLogger(TestStartupBeanImplementation.class);
	
	public TestStartupBeanImplementation() {
		System.out.println("Zeratul says: TestStartupBean has been instantiated!!!");
	}
	
	@PostConstruct
    public void postConstruct() {
        /*
            Notice that we have made use of the Apache Lang library here to trim our log message.
         */
		DatasourceConfiguration datasourceConfiguration = new DatasourceConfiguration();
		
		datasourceConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/{MYSQL_DATABASE}?zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=true");
		datasourceConfiguration.setType("mysql");
		datasourceConfiguration.setUser("artanis");
		datasourceConfiguration.setPassword("pansit");
		datasourceConfiguration.setDbHost("artanis_inventory");
		
		testRepository("artanis_inventory", datasourceConfiguration);
        
		System.out.println("  TestStartupBean.postConstruct() called");
    }
	
	public Response testRepository(String datasource, DatasourceConfiguration datasourceConfiguration) {
		Response response = new Response();
		String methodName = "testRepository(...)";
		TestStartupBeanImplementation.logger.info(methodName + " {");
		
		EntityManagerFactory entityManagerFactory = null;
		EntityManager entityManager = null;
		try {
			if (datasource == null) {
				throw new InternalErrorException("Datasource is null");
			}
			entityManagerFactory = EntityManagerHandlerFactory.factory().getEntityManagerFactory(datasource, datasourceConfiguration);
			if (entityManagerFactory != null) {
				entityManager = entityManagerFactory.createEntityManager();
				
				DepartmentRepository departmentRepository = new DepartmentRepositoryImplementation(entityManager);
				List<Department> departmentList = departmentRepository.getAll();
				
				if (departmentList != null && !departmentList.isEmpty()) {
					response.setResponseCode(HttpStatus.OK.value());
					response.setData(departmentList);
					
					System.out.println("testRepository() HAS RESULTS!!!");
				} else {
					response = this.serviceNotFound();
					System.out.println("testRepository() has FAILED!!!");
				}
			} else {
				response = this.serviceUnavailable();
				System.out.println("testRepository() has FAILED!!!");
			}
			
		} catch (Exception exception) {
			response = this.internalServiceError(exception);
			System.out.println("testRepository() has FAILED!!!");
		} finally {
			if (entityManager != null) {
				entityManager.clear();
				entityManager.close();
				entityManager = null;
			}
		}
		
		TestStartupBeanImplementation.logger.info("} " + methodName);
		return response;
	}
}
