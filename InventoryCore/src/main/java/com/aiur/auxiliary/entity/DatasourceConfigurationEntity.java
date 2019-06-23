package com.aiur.auxiliary.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.aiur.common.DatasourceConfiguration;

import lombok.Getter;
import lombok.Setter;

/**
 * JPA for departments table
 *
 */
@Getter
@Setter
@Entity
@Table(name = "datasource_configurations")
@SuppressWarnings("serial")
public class DatasourceConfigurationEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "row_id")
	private Integer rowId;
	
	@Column(name = "datasource_name")
	private String datasourceName;
	
	@Column(name = "db_connection_string")
	private String dbConnectionString;
	
	@Column(name = "db_type")
	private String dbType;
	
	@Column(name = "db_username")
	private String dbUsername;
	
	@Column(name = "db_password")
	private String dbPassword;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "updated_at")
	private Date updatedAt;
	
	public DatasourceConfiguration convert() {
		DatasourceConfiguration datasourceConfiguration = new DatasourceConfiguration();
		
		datasourceConfiguration.setDatasource(getDatasourceName());
		datasourceConfiguration.setJdbcUrl(getDbConnectionString());
		datasourceConfiguration.setPassword(getDbPassword());
		datasourceConfiguration.setType(getDbType());
		datasourceConfiguration.setUser(getDbUsername());
		
		return datasourceConfiguration;
	}
}


