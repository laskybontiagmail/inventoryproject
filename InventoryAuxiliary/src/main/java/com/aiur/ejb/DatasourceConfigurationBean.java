package com.aiur.ejb;

import javax.ejb.Local;

import com.aiur.model.rest.Response;

@Local
public interface DatasourceConfigurationBean {
	Response getDatasourceConfiguration(String datasource);
}
