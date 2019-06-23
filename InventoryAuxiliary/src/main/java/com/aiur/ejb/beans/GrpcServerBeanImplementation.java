package com.aiur.ejb.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aiur.auxiliary.entity.DatasourceConfigurationEntity;
import com.aiur.auxiliary.repository.DatasourceConfigurationRepository;
import com.aiur.auxiliary.repository.DatasourceConfigurationRepositoryImplementation;
import com.aiur.constant.Grpc;
import com.aiur.factory.EntityManagerHandlerFactory;
import com.aiur.grpc.service.InventoryGrpcServer;

@Startup
@Singleton
public class GrpcServerBeanImplementation { //although no interface yet
	private static Logger logger = LogManager.getLogger(GrpcServerBeanImplementation.class);
	public GrpcServerBeanImplementation() {
		System.out.println("Zeratul says: GrpcServerBean has been instantiated!!!");
	}
	
	@PostConstruct
    public void postConstruct() {
        /*
            Notice that we have made use of the Apache Lang library here to trim our log message.
         */
		
		this.testRepository();
        
		// yes the gRPC service can be kicked-off from here too! (aside from EntryPoint.main)
        InventoryGrpcServer inventoryGrpcServer = new InventoryGrpcServer(Grpc.GRPC_PORT);
        inventoryGrpcServer.runServerThread();
		
		System.out.println("GrpcServerBean.postConstruct() called");
    }
	
	public void testRepository() {
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
					GrpcServerBeanImplementation.logger.info("datasourceConfigurationList has contents!");
				} else {
					GrpcServerBeanImplementation.logger.info("datasourceConfigurationList has NO contents!");
					System.out.println("datasourceConfigurationList has NO contents!");
				}
			} else {
				GrpcServerBeanImplementation.logger.info("Unable to create entity manager!");
				System.out.println("Unable to create entity manager!");
			}
			
		} catch (Exception exception) {
			GrpcServerBeanImplementation.logger.info("Exception occured when getting all datasourceConfigurationList: " + exception.getMessage());
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


