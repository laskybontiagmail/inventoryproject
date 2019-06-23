package com.aiur.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.aiur.common.DatasourceConfiguration;
import com.aiur.ejb.DepartmentRestBean;
import com.aiur.ejb.beans.DepartmentRestBeanImplementation;
import com.aiur.model.rest.Response;
import com.aiur.utilities.ejb.EJB3;

public class DepartmentRestController extends GenericController {
	private static Logger logger = LogManager.getLogger(DepartmentRestController.class);
	private static DepartmentRestBean departmentRestBean = null;
	
	public DepartmentRestController() {
		String methodName = "DepartmentRestController()";
		DepartmentRestController.logger.info(methodName + " {");
		
		if (departmentRestBean == null) {
			DepartmentRestController.logger.info("Creating " + DepartmentRestBean.class.getSimpleName() + " (via EJB3)");
			DepartmentRestController.departmentRestBean = EJB3.getInstance()
					.objectLookUp(DepartmentRestBean.class, DepartmentRestBeanImplementation.class);
		} else {
			DepartmentRestController.logger.info(DepartmentRestController.departmentRestBean + " has been created already.");
		}
		
		DepartmentRestController.logger.info("} " + methodName);
	}
	
	public Response getDepartments(String datasource) {
		Response response = new Response();
		String methodName = "getDepartments()";
		
		DepartmentRestController.logger.info(methodName + " {");
		
		DatasourceConfiguration datasourceConfiguration = getDatasourceConfiguration(datasource);
		if (DepartmentRestController.departmentRestBean != null && datasourceConfiguration != null) {
			response = DepartmentRestController.departmentRestBean.getDepartments(datasource, datasourceConfiguration);
		} else {
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			if (DepartmentRestController.departmentRestBean == null) {
				response.setData(DepartmentRestBean.class + " was not loaded!");
			} else {
				response.setData("Something is wrong with the retrieved datasourceConfiguration!");
			}
		}
		
		DepartmentRestController.logger.info("} " + methodName);
		return response;
	}
	
}


