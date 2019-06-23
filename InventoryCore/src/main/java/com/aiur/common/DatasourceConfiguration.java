package com.aiur.common;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Datasource configuration
 *
 */
@Getter
@Setter
@AllArgsConstructor
@SuppressWarnings("serial")
public class DatasourceConfiguration implements Serializable {
	private String datasource;
	private String jdbcUrl;
	private String user;
	private String password;
	private String type;
	private String dbHost;
	
	/**
	 * Constructor
	 */
	public DatasourceConfiguration() {
		
	}
}
