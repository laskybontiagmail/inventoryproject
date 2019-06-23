package com.aiur.grpc.service;

import java.util.Date;

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
import com.aiur.model.rest.Response;
import com.aiur.service.DatasourceConfigurationService;
import com.aiur.utilities.UtilitiesFactory;
import com.google.protobuf.ByteString;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

/**
 * 
 *
 */
public class InventoryGrpcServer {
	static class TestImpl extends TestGrpc.TestImplBase {
		@Override
		public void testRemoteCall(TestRequest testRequest, StreamObserver<TestReply> responseObserver) {
			String methodName = "testRemoteCall(...)";
			InventoryGrpcServer.logger.info(methodName + " {");
			
			InventoryGrpcServer.logger.info(methodName + " received: " + testRequest.getRequestMessage());
			
			String prefixMessage = methodName + "[" + (new Date()).toString() + "]";
			String responseMessage = prefixMessage + testRequest.getRequestMessage();
			
			TestReply testReply =  TestReply.newBuilder().setReplyMessage(responseMessage).build();
			InventoryGrpcServer.logger.info(methodName + " response: " + prefixMessage);
			
			responseObserver.onNext(testReply);
			responseObserver.onCompleted();
			
			InventoryGrpcServer.logger.info("} " + methodName);
		}
		
	}
	
	static class InventoryAuxiliaryImpl extends InventoryAuxiliaryGrpc.InventoryAuxiliaryImplBase {
		@Override
		public void getDatasourceConfiguration(InventoryAuxiliaryRequest inventoryAuxiliaryRequest, StreamObserver<InventoryAuxiliaryResponse> responseObserver) {
			String methodName = "getDatasourceConfiguration(...)";
			InventoryGrpcServer.logger.info(methodName + " {");
			
			ByteString dataByteString = inventoryAuxiliaryRequest.getData();
			byte[] dataBytes = dataByteString.toByteArray();
			String datasource = utilitiesFactory.deserializeFromByteArray(dataBytes);
			
			logger.info(methodName + " received datasource: " + datasource);
			
			responseObserver.onNext(this.getDatasourceConfiguration(datasource));
			responseObserver.onCompleted();
			
			InventoryGrpcServer.logger.info("} " + methodName);
		}
		
		private InventoryAuxiliaryResponse getDatasourceConfiguration(String datasource) {
			InventoryAuxiliaryResponse inventoryAuxiliaryResponse = null;
			DatasourceConfiguration datasourceConfiguration = null;
			
			Response response = (new DatasourceConfigurationService()).getDatasourceConfiguration(datasource);
			if (response != null&& response.getResponseCode() == HttpStatus.OK.value()
					&& !response.isHasError()) {
				datasourceConfiguration = (DatasourceConfiguration) response.getData();
				ByteString dataInByteString = ByteString.copyFrom(utilitiesFactory.serializeToByteArray(datasourceConfiguration));
				inventoryAuxiliaryResponse = InventoryAuxiliaryResponse.newBuilder()
						.setResponseCode(response.getResponseCode())
						.setData(dataInByteString)
						.build();
			} else {
				inventoryAuxiliaryResponse = InventoryAuxiliaryResponse.newBuilder()
						.setResponseCode(response.getResponseCode())
						.build();
			}
			
			return inventoryAuxiliaryResponse;
		}
	}
	
	private static Logger logger = LogManager.getLogger(InventoryGrpcServer.class);
	private static UtilitiesFactory utilitiesFactory = UtilitiesFactory.factory();
	private static int monitorCheckGap = 60000;
	private Server server;
	private int port;
	private boolean serverThreadIsRunning = true;
	
	public InventoryGrpcServer(int port) {
		this.port = port;
	}
	
	private void initialize() {
		try {
			String message = InventoryGrpcServer.class.getSimpleName() + " started and listenging to port:" + this.port;
			this.server = ServerBuilder.forPort(this.port)
				.addService(new TestImpl())
				.addService(new InventoryAuxiliaryImpl())
				//add more services here as needed
				.build()
				.start();
			System.out.println(message);
			InventoryGrpcServer.logger.info(message);
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					String message = "*** shutting down gRPC " + InventoryGrpcServer.class.getSimpleName() + " server since JVM is shutting down";
					// Use stderr here since the logger may have been reset by its JVM shutdown hook.
			        System.err.println(message);
			        InventoryGrpcServer.logger.error(message);
			        
			        InventoryGrpcServer.this.stop();
			        
			        message = "*** server shut down!";
			        System.err.println(message);
			        InventoryGrpcServer.logger.error(message);
			      }
			});
		} catch (Exception exception) {
			InventoryGrpcServer.logger.error(InventoryGrpcServer.class.getSimpleName() + " failed to start: " + exception.getMessage());
			exception.printStackTrace();
			//TODO: systematic way of outputting exceptions
		}
	}
	
	private void stop() {
		if (server != null) {
			this.server.shutdown();
		}
	}
	
	/**
	 * Await termination on the main thread since the grpc library uses daemon threads.
	 */
	private void blockUntilShutdown() throws InterruptedException {
		if (this.server != null) {
			this.server.awaitTermination();
		}
	}
	
	public void runServerThread() {
		final String methodName = "runServerThread()";
		
		Runnable mainTask = () -> {
			this.setServerThreadIsRunning(true);
			try {
				String message = methodName + " has started!";
				InventoryGrpcServer.this.initialize();
				System.out.println(message);
				//InventoryGrpcServer.logger.info(message);
				message = methodName + " will be listening for requests...";
				System.out.println(message);
				//InventoryGrpcServer.logger.info(message);
				InventoryGrpcServer.this.blockUntilShutdown();
			} catch (InterruptedException interruptedException) {
				InventoryGrpcServer.logger.error(methodName + " failed to start: " + interruptedException.getMessage());
				interruptedException.printStackTrace();
			} catch (Exception exception) {
				InventoryGrpcServer.logger.error(methodName + " failed to start: " + exception.getMessage());
				exception.printStackTrace();
			}
			
			this.setServerThreadIsRunning(true);
			InventoryGrpcServer.logger.error(methodName + " has ended!");
		};
		
		Runnable monitorTask = () -> {
			
			while(this.serverThreadIsRunning) {
				try {
					InventoryGrpcServer.logger.error(methodName + " is still running...");
					Thread.sleep(InventoryGrpcServer.monitorCheckGap);
				} catch (Exception exception) {
					InventoryGrpcServer.logger.error(methodName + " is unable to monitor server thread!");
					exception.printStackTrace();
					break;
				}
			}
		};
		
		new Thread(mainTask).start();
		new Thread(monitorTask).start();
	}
	
	public void stopServerThread() {
		this.stop();
		InventoryGrpcServer.logger.error("runServerThread() has been requested to stop!");
	}
	
	private synchronized void setServerThreadIsRunning(boolean status) {
		this.serverThreadIsRunning = status;
	}
}



