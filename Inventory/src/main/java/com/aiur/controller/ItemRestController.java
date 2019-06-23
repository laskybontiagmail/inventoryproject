package com.aiur.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.aiur.ejb.ItemRestBean;
import com.aiur.ejb.beans.ItemRestBeanImplementation;
import com.aiur.model.rest.Response;
import com.aiur.utilities.ejb.EJB3;

public class ItemRestController {
	private static Logger logger = LogManager.getLogger(ItemRestController.class);
	private static ItemRestBean itemRestBean = null;
	
	public ItemRestController() {
		String methodName = "ItemRestController()";
		ItemRestController.logger.info(methodName + " {");
		
		if (itemRestBean == null) {
			ItemRestController.logger.info("Creating " + ItemRestBean.class.getSimpleName() + " (via EJB3)");
			ItemRestController.itemRestBean = EJB3.getInstance().objectLookUp(ItemRestBean.class, ItemRestBeanImplementation.class);
		} else {
			ItemRestController.logger.info(ItemRestController.itemRestBean + " has been created already.");
		}
		
		ItemRestController.logger.info("} " + methodName);
	}
	
	public Response testGet() {
		Response response = new Response();
		String methodName = "testGet()";
		
		ItemRestController.logger.info(methodName + " {");
		
		if (ItemRestController.itemRestBean != null) {
			response = ItemRestController.itemRestBean.testGet();
		} else {
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setData(ItemRestBean.class.getName() + " was not loaded!");
		}
		
		ItemRestController.logger.info("} " + methodName);
		return response;
	}
	
	public Response getItems() {
		Response response = new Response();
		String methodName = "getItems()";
		
		ItemRestController.logger.info(methodName + " {");
		
		if (ItemRestController.itemRestBean != null) {
			response = ItemRestController.itemRestBean.getItems();
		} else {
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setData(ItemRestBean.class + " was not loaded!");
		}
		
		ItemRestController.logger.info("} " + methodName);
		return response;
	}
}
