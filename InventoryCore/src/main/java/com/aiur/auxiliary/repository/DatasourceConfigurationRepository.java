package com.aiur.auxiliary.repository;

import java.util.List;

import com.aiur.auxiliary.entity.DatasourceConfigurationEntity;


public interface DatasourceConfigurationRepository {
	List<DatasourceConfigurationEntity> getAll() throws Exception;
	DatasourceConfigurationEntity getDatasourceConfiguration(String dataSourceName) throws Exception;
}


