package com.aiur.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aiur.ejb.DatasourceConfigurationBean;
import com.aiur.ejb.beans.DatasourceConfigurationBeanImplementation;
import com.aiur.model.rest.Response;
import com.aiur.utilities.ejb.EJB3;

public class DatasourceConfigurationRestController {
	private static Logger logger = LogManager.getLogger(DatasourceConfigurationRestController.class);
	private static DatasourceConfigurationBean datasourceConfigurationBean = null;
	
	public DatasourceConfigurationRestController() {
		String methodName = "DatasourceConfigurationRestController()";
		logger.info(methodName + " {");
		
		if (datasourceConfigurationBean == null) {
			logger.info("Creating " + DatasourceConfigurationBean.class.getSimpleName() + " (via EJB3)");
			datasourceConfigurationBean = EJB3.getInstance().objectLookUp(DatasourceConfigurationBean.class, DatasourceConfigurationBeanImplementation.class);
		} else {
			logger.info(DatasourceConfigurationRestController.datasourceConfigurationBean + " has been created already.");
		}
		
		logger.info("} " + methodName);
	}
	
	public Response getDatasouceConfiguration(String datasource) {
		Response response = new Response();
		String methodName = "getDatasouceConfiguration()";
		logger.info(methodName + " {");
		
		if (datasourceConfigurationBean != null) {
			response = datasourceConfigurationBean.getDatasourceConfiguration(datasource);
		}
		
		logger.info("} " + methodName);
		return response;
	}
}
