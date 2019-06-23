package com.aiur.controller;

import com.aiur.common.DatasourceConfiguration;
import com.aiur.constant.Grpc;
import com.aiur.grpc.service.InventoryGrpcClientService;

public class GenericController {
	
	public DatasourceConfiguration getDatasourceConfiguration(String datasource) {
		DatasourceConfiguration datasourceConfiguration = null;
		
		InventoryGrpcClientService inventoryGrpcClientService = new InventoryGrpcClientService(Grpc.GRPC_HOST,
				Grpc.GRPC_PORT);
		datasourceConfiguration = inventoryGrpcClientService.getDatasourceConfiguration(datasource);
		
		return datasourceConfiguration;
	}
}
