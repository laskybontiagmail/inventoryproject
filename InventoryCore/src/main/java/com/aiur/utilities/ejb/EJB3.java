package com.aiur.utilities.ejb;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aiur.utilities.ApplicationProperty;
import com.aiur.utilities.UtilitiesFactory;

public class EJB3 {
	private static Logger logger = LogManager.getLogger(EJB3.class);
	private static String moduleName = "ArtanisInventory";
	private static EJB3 singletonInstance = null; 
	private UtilitiesFactory utilitiesFactory = UtilitiesFactory.factory();
	
	private EJB3() {
		
	}
	
	public static EJB3 getInstance() {
		if (EJB3.singletonInstance == null) {
			EJB3.singletonInstance = new EJB3();
		}
		
		return EJB3.singletonInstance;
	}
	
	@SuppressWarnings("unchecked")
	public <Type> Type objectLookUp(Class<Type> beanInterfaceType, Class<?> beanClassType) {
		Type objectToLookUp = null;
		String methodName = "objectLookUp()";
		
		EJB3.logger.info(methodName + " {");
		
		Context context = null;
        String beanInterfaceName = beanInterfaceType.getName();
        String beanName = beanClassType.getSimpleName();
        String lookUpFormat = "";
        
		try {
			String moduleName = utilitiesFactory.getModuleName();
			EJB3.logger.info("Initialize <EJB-" + beanInterfaceType.getName() + "> started ...");
			Properties props = new Properties();
			props.put(Context.INITIAL_CONTEXT_FACTORY, ApplicationProperty.get("context.initial.context.factory"));
			props.put(Context.PROVIDER_URL, ApplicationProperty.get("context.provider.url"));
			
			EJB3.logger.info("context.initial.context.factor: " + ApplicationProperty.get("context.initial.context.factory"));
			EJB3.logger.info("context.provider.url: " + ApplicationProperty.get("context.provider.url"));
			
			context = new InitialContext(props);
	        
			if (moduleName.equals(EJB3.moduleName)) {
				moduleName = EJB3.moduleName;
			}
			
	        //lookUpFormat = "java:global" + "/" + EJB3.moduleName + "//" + beanName + "!" + beanInterfaceName;
			lookUpFormat = "java:global" + "/" + moduleName + "//" + beanName + "!" + beanInterfaceName;
	        EJB3.logger.info("EJB Lookup format: " + lookUpFormat);
	        
	        /*
	         java:global/EJBModule/TestRestBean!com.aiur.ejb.TestRestBeanInterface
			 java:app/EJBModule/TestRestBean!com.aiur.ejb.TestRestBeanInterface
			 java:module/TestRestBean!com.aiur.ejb.TestRestBeanInterface
			 java:global/EJBModule/TestRestBean
			 java:app/EJBModule/TestRestBean
			 java:module/TestRestBean
	         */
			
	        objectToLookUp = (Type) context.lookup(lookUpFormat);
	        EJB3.logger.info("Initialize <EJB-" + beanInterfaceType.getName() + "> loaded successfully!");
		} catch (Exception exception) {
			EJB3.logger.error("========== Unable to load: " + beanInterfaceName + " =========");
			EJB3.logger.error("Using this format: \n" + lookUpFormat);
			EJB3.logger.error(exception.getMessage());
		}
		
		EJB3.logger.info("} " + methodName);
		return objectToLookUp;
	}
	
}


