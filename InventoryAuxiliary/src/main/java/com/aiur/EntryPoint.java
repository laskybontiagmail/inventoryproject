package com.aiur;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
//import org.slf4j.LoggerFactory;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.Swarm;
//import org.wildfly.swarm.undertow.WARArchive;
//import org.wildfly.swarm.undertow.descriptors.WebXmlAsset;
//import org.wildfly.swarm.undertow.WARArchive;
//import org.wildfly.swarm.undertow.descriptors.WebXmlAsset;
//import org.wildfly.swarm.bootstrap.MainInvoker;

import com.aiur.common.DatasourceConfiguration;
import com.aiur.auxiliary.entity.DatasourceConfigurationEntity;
import com.aiur.auxiliary.repository.DatasourceConfigurationRepository;
import com.aiur.auxiliary.repository.DatasourceConfigurationRepositoryImplementation;
import com.aiur.factory.EntityManagerHandlerFactory;
import com.aiur.grpc.service.InventoryGrpcClientService;
import com.aiur.grpc.service.InventoryGrpcServer;
import com.aiur.model.TestSerializableObject;
import com.aiur.service.TestService;
import com.aiur.utilities.UtilitiesFactory;

//import org.wildfly.swarm.jaxrs.JAXRSArchive;
//import org.apache.logging.log4j.Logger;


public class EntryPoint {
//	static {
//		System.setProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager");
//	}
	
	private static Logger logger = null;
	//private static Logger logger = LogManager.getLogger(EntryPoint.class);
	private static java.util.logging.Logger javaUtilityLogger = null;
	
	private static int loadBreather = 2000;
	private static int maxIterations = 30;
	private static boolean deployDone = false;
	private static int inventoryGrpcPort = 50051; //TODO: make this injectable
	
	public static void main(String... args) {
		String methodName = "EntryPoint()";
		String startMainMethodMessage = methodName +  " {";
		String endMainMethodMessage = "} " + methodName;
		System.out.println(startMainMethodMessage);
		
		EntryPoint.setLogger();
		
		//this is only working for main but no web and ejbs instantiated
		try {
			(new Swarm()).start().deploy(); //definitely working for war
//			logger = LogManager.getLogger(EntryPoint.class);
//			logger = LogManager.getLogger();
			
			//this one is working only for main
//			Swarm swarm = new Swarm();
//			WARArchive war = ShrinkWrap.create(WARArchive.class, "InventoryAuxiliary.war")
//					.addPackage(EntryPoint.class.getPackage())
//					.addAsWebInfResource(new ClassLoaderAsset("web.xml", EntryPoint.class.getClassLoader()), WebXmlAsset.NAME)
//					//.addAllDependencies() //will not solve the web/ejbs loading problem
//					;
//			swarm.start().deploy(war);
			
//			Swarm swarm = new Swarm();
//			JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class, "InventoryAuxiliary-swarm.jar");
//			deployment.addPackage( EntryPoint.class.getPackage() );
//			deployment.addAllDependencies();
//			swarm.start().deploy(deployment);
			System.out.println("=========================================================================");
			System.out.println("                                                                         ");
			System.out.println("                                                                         ");
			System.out.println("MAIN is stil UNDER CONSTRUCTION right now!!!");
			System.out.println("log4j is not yet working properly in this case");
			System.out.println("                                                                         ");
			System.out.println("                                                                         ");
			System.out.println("=========================================================================");
			//EntryPoint.setLogger();
		} catch (Exception exception) {
			System.out.println("Unable to custom launch swarm deployer!!!");
		}
		
		//not working
//		try {
//			Swarm swarm = new Swarm();
//			WARArchive war = ShrinkWrap.create(WARArchive.class)
//				.addPackage(EntryPoint.class.getPackage())
//				.addAsWebInfResource(new ClassLoaderAsset("web.xml", EntryPoint.class.getClassLoader()), WebXmlAsset.NAME)
//				.addAllDependencies();
//			war.addAllDependencies();
//			swarm.start().deploy(war);
//		} catch (Exception exception) {
//			System.out.println("Unable to custom launch swarm deployer!!!");
//		}
		
		//this is working already after the provided scope of the dependencies were removed but except for logging 
//		try {
//			new Swarm().start().deploy();
//		} catch (Exception exception) {
//			System.out.println("Unable to custom launch swarm deployer!!!");
//		}
		
//		try {
//			Swarm swarm = new Swarm();
//			WARArchive war = ShrinkWrap.create(WARArchive.class)
//			        .addClass(EntryPoint.class)
//			        .addAsWebInfResource("web.xml", WebXmlAsset.NAME);
//			swarm.start().deploy(war);
//		} catch (Exception exception) {
//			System.out.println("Unable to custom launch swarm deployer!!!");
//		}
		
		
//		try {
//			Swarm swarm = new Swarm();
//			swarm.start();
//			
//			JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class);
//	        deployment.addPackage(EntryPoint.class.getPackage());
//	        deployment.addAllDependencies();
//
//	        swarm.deploy(deployment);
//		} catch (Exception exception) {
//			System.out.println("Unable to custom launch swarm deployer!!!");
//		}
		
		EntryPoint.deployDone = true;
		
		//EntryPoint.logger.error("lag por jiy: IROR ni!");
		
		EntryPoint.doMainProcedure(args);
		System.out.println(endMainMethodMessage);
	}
	
	public static void doMainProcedure(String... args) {
		String methodName = "doMainProcedure(...)";
		
		System.out.println(methodName + " {");
		
		int counter = 0;
		try {
			new TestService();
			for (counter = 0; counter < EntryPoint.maxIterations && EntryPoint.logger == null; counter++) {
				System.out.println("[" + counter + "]" + " waiting for EntryPoint to deploy...");
				Thread.sleep(EntryPoint.loadBreather / 2);
			}
			
			if (EntryPoint.logger != null) {
				java.util.logging.Logger javaLogger = java.util.logging.LogManager.getLogManager().getLogger(EntryPoint.class.getName());
				javaLogger.setLevel(Level.INFO);
				
				javaLogger.info("java logger says how are you artanis?");
				javaLogger.log(Level.SEVERE, "java logger says how are you artanis?");
				
				
				org.apache.logging.log4j.Logger log4jLogger = LogManager.getLogger(EntryPoint.class);
				//org.apache.logging.log4j.Logger log4jLogger = LogManager.getLogger(JULI.);
				
				//java.util.logging.Logger JULI2 = LoggerFactory.getLogger(EntryPoint.class).
				
				javaUtilityLogger.info("juli logger says how are you artanis?");
				javaUtilityLogger.log(Level.SEVERE, "juli logger says how are you artanis?");
				
				log4jLogger.info("log4j logger says how are you artanis?");
				log4jLogger.error("log4j logger says how are you artanis?");
				
				EntryPoint entryPoint = new EntryPoint();
				entryPoint.testSerializationAndDesiralization();
		        entryPoint.testRepository();
		        entryPoint.testGrpcService();
		        
			} else {
				throw new Exception("ERROR! Logger setup timed out! Unable to execute Main Procedure!!!");
			}
			
		} catch (Exception exception) {
			System.out.println("ERROR on executing Main Procedure!!!");
			exception.printStackTrace();
		}
		
		System.out.println("} " + methodName);
	}
	
	public static void setLogger() {
		Runnable waitAndLoadTask = new Runnable() {
			@Override
			public void run() {
				System.out.println("setLogger() thread is now starting...");
				int counter = 0;
				try {
					for (counter = 0; counter < EntryPoint.maxIterations && !EntryPoint.deployDone; counter++) {
						System.out.println("[" + counter + "]" + " waiting for EntryPoint to deploy...");
						Thread.sleep(EntryPoint.loadBreather);
					}
					
					if (EntryPoint.deployDone) {
						EntryPoint.logger = LogManager.getLogger(EntryPoint.class);
						EntryPoint.javaUtilityLogger =
								org.apache.logging.log4j.jul.LogManager.getLogManager().getLogger(EntryPoint.class.getName());
						String message = "Logger was successfully loaded!";
						System.out.println(message);
						EntryPoint.logger.info("log4J says:" + message);
					} else {
						throw new Exception("Logger Setting Timed Out!");
					}
				} catch (Exception exception) {
					System.out.println("ERROR! Unable to wait for swarm deployer!!!");
					exception.printStackTrace();
				}
				
				System.out.println("setLogger() thread has ended.");
			}
		};
		
		(new Thread(waitAndLoadTask)).start();
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
					EntryPoint.logger.info("datasourceConfigurationList has contents!");
				} else {
					EntryPoint.logger.info("datasourceConfigurationList has NO contents!");
					System.out.println("datasourceConfigurationList has NO contents!");
				}
			} else {
				EntryPoint.logger.info("Unable to create entity manager!");
				System.out.println("Unable to create entity manager!");
			}
			
		} catch (Exception exception) {
			EntryPoint.logger.info("Exception occured when getting all datasourceConfigurationList: " + exception.getMessage());
			System.out.println("Exception occured when getting all datasourceConfigurationList: " + exception.getMessage());
		} finally {
			if (entityManager != null) {
				entityManager.clear();
				entityManager.close();
				entityManager = null;
			}
		}
		
		System.out.println("(sysout)} " + methodName);
		EntryPoint.logger.info("(logger)} " + methodName);
    }
	
	public void testGrpcService() {
		InventoryGrpcServer inventoryGrpcServer = new InventoryGrpcServer(inventoryGrpcPort);
        inventoryGrpcServer.runServerThread();
        
        InventoryGrpcClientService inventoryGrpcClientService = new InventoryGrpcClientService("localhost", inventoryGrpcPort);
        String requestMessage = "Halu from InventoryGrpcClient!";
        String responseMessage = inventoryGrpcClientService.testRemoteCall(requestMessage);
        javaUtilityLogger.info("Request sent to gRPC(testRemoteCall): " + requestMessage);
        javaUtilityLogger.info("Response from gRPC Server: " + responseMessage);
        
        String datasourceName = "artanis_inventory";
        DatasourceConfiguration datasourceConfiguration = inventoryGrpcClientService.getDatasourceConfiguration(datasourceName);
        javaUtilityLogger.info("Datasource name sent to gRPC(testRemoteCall): " + datasourceName);
        javaUtilityLogger.info("Response from gRPC Server: " + UtilitiesFactory.factory().toJsonString(datasourceConfiguration));
        
        datasourceName = "どうもありがとうございました";
        datasourceConfiguration = inventoryGrpcClientService.getDatasourceConfiguration(datasourceName);
        javaUtilityLogger.info("Datasource name sent to gRPC(testRemoteCall): " + datasourceName);
        javaUtilityLogger.info("Response from gRPC Server: " + UtilitiesFactory.factory().toJsonString(datasourceConfiguration));
	}
	
	public void testSerializationAndDesiralization() {
		TestSerializableObject sourceObject = new TestSerializableObject();
		TestSerializableObject sourceObjectLocalized = new TestSerializableObject();
		TestSerializableObject destinationObject = null;
		TestSerializableObject destinationObjectLocalized = null;
		UtilitiesFactory utilitiesFactory = UtilitiesFactory.factory();
		
		sourceObject.setNumber(1234567890);
		sourceObject.setString("The quick brown fox jumps over the lazy dogs!");
		sourceObjectLocalized.setNumber(1023456789);
		sourceObjectLocalized.setString("どうもありがとうございました！");
		
		destinationObject = utilitiesFactory.deserializeFromByteArray(utilitiesFactory.serializeToByteArray(sourceObject));
		destinationObjectLocalized = utilitiesFactory.deserializeFromByteArray(utilitiesFactory.serializeToByteArray(sourceObjectLocalized));
		
		EntryPoint.javaUtilityLogger.info("Source: \n" + utilitiesFactory.toJsonString(sourceObject));
		EntryPoint.javaUtilityLogger.info("After serialization: \n" + utilitiesFactory.toJsonString(destinationObject));
		
		EntryPoint.javaUtilityLogger.info("Source: \n" + utilitiesFactory.toJsonString(sourceObjectLocalized));
		EntryPoint.javaUtilityLogger.info("After serialization: \n" + utilitiesFactory.toJsonString(destinationObjectLocalized));
	}
}





