package com.aiur.ejb.beans;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.aiur.auxiliary.entity.DatasourceConfigurationEntity;
import com.aiur.auxiliary.repository.DatasourceConfigurationRepository;
import com.aiur.auxiliary.repository.DatasourceConfigurationRepositoryImplementation;
import com.aiur.ejb.TestRestBean;
import com.aiur.factory.EntityManagerHandlerFactory;
import com.aiur.model.rest.Response;
import com.aiur.utilities.UtilitiesFactory;

@Stateless
public class TestRestBeanImplementation implements TestRestBean {
	private static Logger logger = LogManager.getLogger(TestRestBeanImplementation.class);
	private UtilitiesFactory utilitiesFactory = UtilitiesFactory.factory();
	private final static int MAX_CHOICES = 5;
	
	static {
		TestRestBeanImplementation.logger.info(TestRestBeanImplementation.class.getSimpleName() + " instantiated!");
	}
	
	@Override
	public Response testGet() {
		Response response = new Response();
		String methodName = "testGet()";
		TestRestBeanImplementation.logger.info(methodName + " {");
		
		int choiceOfTheMoment = this.utilitiesFactory.generateRandomNumber(MAX_CHOICES) % TestRestBeanImplementation.MAX_CHOICES;
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Hello! From Artanis' Inventory Web Application!");
		stringBuffer.append("\n[" + TestRestBeanImplementation.class.getSimpleName() + methodName + "]"
				+ " request made on: " + (new Date()).toString() + "\n");
		switch(choiceOfTheMoment) {
			case 0:
				stringBuffer.append("--Naruto@");
				break;
			case 1:
				stringBuffer.append("--One punch man!");
				break;
			case 2:
				stringBuffer.append("--Gokou--");
				break;
			case 3:
				stringBuffer.append("^Ultraman ^");
				break;
			case 4:
				stringBuffer.append("...Superman...");
				break;
		}
		response.setResponseCode(HttpStatus.OK.value());
		response.setData(stringBuffer.toString());
		
		this.testRepository();
		
		TestRestBeanImplementation.logger.info("} " + methodName);
		return response;
	}
	
	private void testRepository() {
		String methodName = "testRepository()";
		System.out.println(methodName + " {" );
		
		EntityManagerFactory entityManagerFactory = null;
		EntityManager entityManager = null;
		try {
			entityManagerFactory = EntityManagerHandlerFactory.factory().getEntityManagerFactory();
			if (entityManagerFactory != null) {
				entityManager = entityManagerFactory.createEntityManager();
				
				DatasourceConfigurationRepository datasourceConfigurationRepository = new DatasourceConfigurationRepositoryImplementation(entityManager);
				List<DatasourceConfigurationEntity> datasourceConfigurationList = datasourceConfigurationRepository.getAll();
				
				if (datasourceConfigurationList != null && !datasourceConfigurationList.isEmpty()) {
					System.out.println("datasourceConfigurationList has contents!");
					TestRestBeanImplementation.logger.info("datasourceConfigurationList has contents!");
				} else {
					TestRestBeanImplementation.logger.info("datasourceConfigurationList has NO contents!");
					System.out.println("datasourceConfigurationList has NO contents!");
				}
			} else {
				TestRestBeanImplementation.logger.info("Unable to create entity manager!");
				System.out.println("Unable to create entity manager!");
			}
			
		} catch (Exception exception) {
			TestRestBeanImplementation.logger.info("Exception occured when getting all datasourceConfigurationList: " + exception.getMessage());
			System.out.println("Exception occured when getting all datasourceConfigurationList: " + exception.getMessage());
		} finally {
			if (entityManager != null) {
				entityManager.clear();
				entityManager.close();
				entityManager = null;
			}
		}
		
		System.out.println("} " + methodName);
    }
}
