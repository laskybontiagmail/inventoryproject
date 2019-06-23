package com.aiur.ejb;

import javax.ejb.Local;

import com.aiur.common.DatasourceConfiguration;
import com.aiur.model.rest.Response;

@Local
public interface DepartmentRestBean {
	Response getDepartments(String datasource, DatasourceConfiguration datasourceConfiguration);
}

