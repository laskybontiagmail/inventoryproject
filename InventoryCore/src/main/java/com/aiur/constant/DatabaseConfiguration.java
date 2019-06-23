package com.aiur.constant;

import java.io.Serializable;

/**
 * Database connection constants
 *
 */
public enum DatabaseConfiguration implements Serializable {
	HibernateDialectPropertyKey("hibernate.dialect"),
	DatabaseConnectionParameters("?zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=true"),
	MySQLDriverName("mysql"),
	PostgresDriverName("postgres"),
	MySQLDriverPackageName("com.mysql.jdbc.Driver"),
	PostgresDriverPackageName("org.postgresql.Driver"),
	MySQLHibernateDialect("org.hibernate.dialect.MySQLDialect"),
	PostgresHibernateDialect("org.hibernate.dialect.PostgreSQLDialect")
	;
	
	private String value;
	
	/**
	 * Constructor
	 */
	private DatabaseConfiguration(String value) {
		this.value = value;
	}
	
	/**
	 * Value
	 */
	public String value() {
		return this.value;
	}
	
}


