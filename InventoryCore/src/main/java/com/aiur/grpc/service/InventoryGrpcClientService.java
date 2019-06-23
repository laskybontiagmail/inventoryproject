package com.aiur.grpc.service;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.aiur.common.DatasourceConfiguration;
import com.aiur.inventorygrpc.InventoryAuxiliaryGrpc;
import com.aiur.inventorygrpc.InventoryAuxiliaryRequest;
import com.aiur.inventorygrpc.InventoryAuxiliaryResponse;
import com.aiur.inventorygrpc.TestGrpc;
import com.aiur.inventorygrpc.TestReply;
import com.aiur.inventorygrpc.TestRequest;
import com.aiur.utilities.UtilitiesFactory;
import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class InventoryGrpcClientService {
	private static final Logger logger = LogManager.getLogger(InventoryGrpcClientService.class);
	
	private final ManagedChannel channel;
	private final TestGrpc.TestBlockingStub testBlockingStub;
	private final InventoryAuxiliaryGrpc.InventoryAuxiliaryBlockingStub inventoryAuxiliaryStub;
	private static UtilitiesFactory utilitiesFactory = UtilitiesFactory.factory();
	  
	public InventoryGrpcClientService(String host, int port) {
		this(ManagedChannelBuilder.forAddress(host, port)
		        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
		        // needing certificates.
		        .usePlaintext(true)
		        .build());
	}
	
	InventoryGrpcClientService(ManagedChannel channel) {
	    this.channel = channel;
	    testBlockingStub = TestGrpc.newBlockingStub(channel);
	    inventoryAuxiliaryStub = InventoryAuxiliaryGrpc.newBlockingStub(channel);
	    //add stubs here for future services
	}
	
	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}
	
	
	public String testRemoteCall(String message) {
		String methodName = "testRemoteCall()";
		InventoryGrpcClientService.logger.info(methodName + "{ ");
		
		String responseMessage = "no response!";
		TestRequest request = TestRequest.newBuilder().setRequestMessage(message).build();
		TestReply response = null;
		
		try {
			response = testBlockingStub.testRemoteCall(request);
			InventoryGrpcClientService.logger.info("Greeting, the server responded ---> " + response.getReplyMessage() + "\n\n================\n");
			responseMessage = response.getReplyMessage();
	    } catch (StatusRuntimeException exception) {
	    	InventoryGrpcClientService.logger.warn("RPC failed: {0}", exception.getStatus());
	    } catch (Exception exception) {
	    	logger.error("RPC failed: {0}", exception.getMessage());
	    	exception.printStackTrace();
		}
		
		InventoryGrpcClientService.logger.info("} " + methodName);
		return responseMessage;
	}
	
	public DatasourceConfiguration getDatasourceConfiguration(String datasource) {
		String methodName = "getDatasourceConfiguration()";
		logger.info(methodName + "{ ");
		
		DatasourceConfiguration datasourceConfiguration = null;
		ByteString datasourceByteString = ByteString.copyFrom(utilitiesFactory.serializeToByteArray(datasource));
		InventoryAuxiliaryResponse inventoryAuxiliaryResponse = null;
		InventoryAuxiliaryRequest inventoryAuxiliaryRequest = InventoryAuxiliaryRequest.newBuilder()
				.setData(datasourceByteString)
				.build();
		try {
			inventoryAuxiliaryResponse = inventoryAuxiliaryStub.getDatasourceConfiguration(inventoryAuxiliaryRequest);
			int responseCode = inventoryAuxiliaryResponse.getResponseCode();
			if (responseCode == HttpStatus.OK.value()) {
				datasourceConfiguration = utilitiesFactory.<DatasourceConfiguration>deserializeFromByteArray(inventoryAuxiliaryResponse.getData().toByteArray());
			}
		} catch (StatusRuntimeException exception) {
			logger.warn("RPC failed: {0}", exception.getStatus());
		} catch (Exception exception) {
			logger.error("RPC failed: {0}", exception.getMessage());
	    	exception.printStackTrace();
		}
		
		logger.info("} " + methodName);
		return datasourceConfiguration;
	}
	
}


