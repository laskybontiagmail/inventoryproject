package com.aiur.ejb.beans;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.aiur.common.DatasourceConfiguration;
import com.aiur.common.service.ServiceManager;
import com.aiur.constant.common.errors.InternalErrorException;
import com.aiur.ejb.DepartmentRestBean;
import com.aiur.entity.Department;
import com.aiur.factory.EntityManagerHandlerFactory;
import com.aiur.model.rest.Response;
import com.aiur.repository.DepartmentRepository;
import com.aiur.repository.DepartmentRepositoryImplementation;

@Stateless
public class DepartmentRestBeanImplementation extends ServiceManager implements DepartmentRestBean {
	private static Logger logger = LogManager.getLogger(DepartmentRestBeanImplementation.class);
	
	@Override
	public Response getDepartments(String datasource, DatasourceConfiguration datasourceConfiguration) {
		Response response = new Response();
		String methodName = "getDepartments()";
		DepartmentRestBeanImplementation.logger.info(methodName + " {");
		
		EntityManagerFactory entityManagerFactory = null;
		EntityManager entityManager = null;
		try {
			if (datasource == null) {
				throw new InternalErrorException("Datasource is null");
			}
			entityManagerFactory = EntityManagerHandlerFactory.factory().getEntityManagerFactory(datasource, datasourceConfiguration);
			if (entityManagerFactory != null) {
				entityManager = entityManagerFactory.createEntityManager();
				if (entityManager != null) {
					DepartmentRepository departmentRepository = new DepartmentRepositoryImplementation(entityManager);
					List<Department> departmentList = departmentRepository.getAll();
					
					if (departmentList != null && !departmentList.isEmpty()) {
						response.setResponseCode(HttpStatus.OK.value());
						response.setData(departmentList);
					} else {
						response = this.serviceNotFound();
					}
				} else {
					response = this.serviceUnavailable();
					response.setData("Entity manager factory failed");
				}
			} else {
				response = this.serviceUnavailable();
			}
			
		} catch (Exception exception) {
			response = this.internalServiceError(exception);
		} finally {
			if (entityManager != null) {
				entityManager.clear();
				entityManager.close();
				entityManager = null;
			}
		}
		
		DepartmentRestBeanImplementation.logger.info("} " + methodName);
		return response;
	}
}
