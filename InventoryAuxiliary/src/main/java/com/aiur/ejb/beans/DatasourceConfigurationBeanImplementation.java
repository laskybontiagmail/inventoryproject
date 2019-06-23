package com.aiur.ejb.beans;

import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aiur.common.service.ServiceManager;

import com.aiur.ejb.DatasourceConfigurationBean;
import com.aiur.model.rest.Response;
import com.aiur.service.DatasourceConfigurationService;


@Stateless
public class DatasourceConfigurationBeanImplementation extends ServiceManager implements DatasourceConfigurationBean {
	private static Logger logger = LogManager.getLogger(DatasourceConfigurationBeanImplementation.class);
	
	@Override
	public Response getDatasourceConfiguration(String datasource) {
		Response response = null;
		String methodName = "getDatasourceConfiguration()";
		logger.info(methodName + " {");
		logger.info(methodName + " datasource: " + datasource);
		
		response = (new DatasourceConfigurationService()).getDatasourceConfiguration(datasource);
		
		logger.info("} " + methodName);
		return response;
	}

}
