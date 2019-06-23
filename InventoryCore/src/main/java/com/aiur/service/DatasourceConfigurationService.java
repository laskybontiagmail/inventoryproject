package com.aiur.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.aiur.auxiliary.entity.DatasourceConfigurationEntity;
import com.aiur.auxiliary.repository.DatasourceConfigurationRepository;
import com.aiur.auxiliary.repository.DatasourceConfigurationRepositoryImplementation;
import com.aiur.common.DatasourceConfiguration;
import com.aiur.common.service.ServiceManager;
import com.aiur.constant.common.errors.InternalErrorException;
import com.aiur.factory.EntityManagerHandlerFactory;
import com.aiur.model.rest.Response;

public class DatasourceConfigurationService extends ServiceManager {
	private static Logger logger = LogManager.getLogger(DatasourceConfiguration.class);
	
	public Response getDatasourceConfiguration(String datasource) {
		Response response = new Response();
		String methodName = "getDatasourceConfiguration()";
		logger.info(methodName + " {");
		logger.info(methodName + " datasource:" + datasource);
		
		EntityManagerFactory entityManagerFactory = null;
		EntityManager entityManager = null;
		try {
			if (datasource == null) {
				throw new InternalErrorException("Datasource is null");
			}
			entityManagerFactory = EntityManagerHandlerFactory.factory().getEntityManagerFactory();
			if (entityManagerFactory != null) {
				entityManager = entityManagerFactory.createEntityManager();
				if (entityManager != null) {
					DatasourceConfigurationRepository datasourceConfigurationRepository = new DatasourceConfigurationRepositoryImplementation(entityManager);
					DatasourceConfigurationEntity datasourceConfigurationEntity = datasourceConfigurationRepository.getDatasourceConfiguration(datasource);
					if (datasourceConfigurationEntity != null) {
						response.setResponseCode(HttpStatus.OK.value());
						response.setData(datasourceConfigurationEntity.convert()); //TODO: clear out all references to DatasourceConfiguration and DatasourceConfigurationEntity
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
		
		logger.info("} " + methodName);
		return response;
	}
}
