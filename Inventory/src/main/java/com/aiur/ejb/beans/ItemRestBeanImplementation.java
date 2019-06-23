package com.aiur.ejb.beans;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.aiur.ejb.ItemRestBean;
import com.aiur.entity.Item;
import com.aiur.grpc.service.InventoryGrpcClientService;
import com.aiur.model.rest.Response;
import com.aiur.repository.ItemRepository;
import com.aiur.repository.ItemRepositoryImplentation;
import com.aiur.utilities.UtilitiesFactory;

@Stateless
public class ItemRestBeanImplementation implements ItemRestBean {
	private static Logger logger = LogManager.getLogger(ItemRestBean.class);
	private final static int MAX_CHOICES = 5;
	private UtilitiesFactory utilitiesFactory = UtilitiesFactory.factory();
	
	@Override
	public Response getItems() {
		Response response = new Response();
		String methodName = "getItems()";
		ItemRestBeanImplementation.logger.info(methodName + " {");
		
		ItemRepository productRepository = new ItemRepositoryImplentation();
		List<Item> productList = productRepository.getAllItems();
		
		if (productList != null && !productList.isEmpty()) {
			response.setResponseCode(HttpStatus.OK.value());
			response.setData(productList);
		} else {
			response.setResponseCode(HttpStatus.NOT_FOUND.value());
		}
		
		ItemRestBeanImplementation.logger.info("} " + methodName);
		return response;
	}
	
	@Override
	public Response testGet() {
		Response response = new Response();
		String methodName = "testGet()";
		ItemRestBeanImplementation.logger.info(methodName + " {");
		
		int choiceOfTheMoment = this.utilitiesFactory.generateRandomNumber(MAX_CHOICES) % ItemRestBeanImplementation.MAX_CHOICES;
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Hello! From Artanis' Inventory Web Application!");
		stringBuffer.append("\n[" + ItemRestBeanImplementation.class.getSimpleName() + methodName + "]"
				+ " request made on: " + (new Date()).toString() + "\n");
		switch(choiceOfTheMoment) {
			case 0:
				stringBuffer.append("--Coke Zero*");
				break;
			case 1:
				stringBuffer.append("--One punch man!");
				break;
			case 2:
				stringBuffer.append("--Tic Tac Toe--");
				break;
			case 3:
				stringBuffer.append("^Pine Tree ^");
				break;
			case 4:
				stringBuffer.append("...Southern Lights...");
				break;
		}
		
		//test GrpcCall
		stringBuffer.append("<br />Grpc Server said: " + this.testRemoteCallToGrpcServer());
		
		response.setResponseCode(HttpStatus.OK.value());
		response.setData(stringBuffer.toString());
		
		ItemRestBeanImplementation.logger.info("} " + methodName);
		return response;
	}
	
	public String testRemoteCallToGrpcServer() {
		return (new InventoryGrpcClientService("localhost", 50053)).testRemoteCall("Halu from InventoryGrpcClient!");
	}
}


