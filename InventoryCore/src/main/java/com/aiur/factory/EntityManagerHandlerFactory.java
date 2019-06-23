package com.aiur.factory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManagerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.aiur.common.DatasourceConfiguration;
import com.aiur.common.GenericCache;
import com.aiur.utilities.UtilitiesFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity manager factory
 *
 */
public class EntityManagerHandlerFactory {
	/**
	 * Local container entity manager factory store
	 *
	 */
	@Getter
	@Setter
	@AllArgsConstructor
	@SuppressWarnings("serial")
	class LocalContainerEntityManagerFactoryStore implements Serializable {
		private Date date;
		private LocalContainerEntityManagerFactoryBean containerFactory;
		
		/**
		 * Constructor
		 */
		public LocalContainerEntityManagerFactoryStore() {
			
		}
	}
	
	/**
	 * Entity manager factory task
	 *
	 */
	class EntityManagerFactoryTask extends TimerTask {
		private final transient Logger logger = LogManager.getLogger(EntityManagerFactoryTask.class);
	
		/**
		 * Constructor.
		 */
		public EntityManagerFactoryTask() {
			logger.info("EntityManagerFactoryTask started ...");
		}

		/**
		 * Run.
		 */
		public void run() {
			validateAndDestroy();
		}

		/**
		 * Validate and destroy
		 */
		@SuppressWarnings({ "rawtypes"})
		private void validateAndDestroy() {
			final String prefix = "validateAndDestroy() ";
			
			final long minutesIdle = 30; // set the idle interval for 30 mins.
			
			try {
				Map factories = getLocalContainerEntityManagerFactoryBeans();
				if (factories != null && !factories.isEmpty()) {
					Iterator itFactory = factories.entrySet().iterator();
					Map.Entry entry;
					LocalContainerEntityManagerFactoryStore containerStore;
					long diff, minutes = 0;
					long dateTimeNow = new Date().getTime();
					LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean;
					while (itFactory.hasNext()) {
						entry = (Map.Entry) itFactory.next();
						if (entry.getValue() instanceof LocalContainerEntityManagerFactoryStore) {
							containerStore = (LocalContainerEntityManagerFactoryStore) entry.getValue();
							if (containerStore != null) {
								if (containerStore.getContainerFactory() != null) {
									diff = dateTimeNow - containerStore.getDate().getTime();
									minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
									if (minutes >= minutesIdle) {
										try {
											localContainerEntityManagerFactoryBean = (LocalContainerEntityManagerFactoryBean) containerStore
													.getContainerFactory();
											((HikariDataSource) localContainerEntityManagerFactoryBean.getDataSource()).close();
											if (localContainerEntityManagerFactoryBean.getObject().isOpen()) {
												logger.info(prefix + "remove idle database connection:" + entry.getKey());
												localContainerEntityManagerFactoryBean.getObject().close();
											}
										} catch(Exception exc) {
											exc.printStackTrace();
											
										} finally {
											remove((String) entry.getKey());
										}
									}
								}
							}
						}
					}
				}
			} catch (Exception exc) {
				logger.error(prefix + exc.getMessage());
			}
		}
	}
	
	
	/**
	 * Store configuration
	 *
	 */
	public enum Configuration implements Serializable {
		HibernateDialect("hibernate.dialect"),
		MySQLType("mysql"),
		PostgresType("postgres"),
		MySQLDriver("com.mysql.jdbc.Driver"),
		PostgresDriver("org.postgresql.Driver"),
		MySQLHibernateDialect("org.hibernate.dialect.MySQLDialect"),
		PostgresHibernateDialect("org.hibernate.dialect.PostgreSQLDialect");
		
		private String value;
		
		/**
		 * Constructor
		 */
		private Configuration(String value) {
			this.value = value;
		}
		
		/**
		 * Value
		 */
		public String value() {
			return this.value;
		}
		
	}
	
	private static Logger logger = LogManager.getLogger(EntityManagerHandlerFactory.class);
	private transient UtilitiesFactory utilsFactory = UtilitiesFactory.factory();
	private transient String entityBasePackage;
	private transient String pgPackages;
	private transient String myPackages;
	private transient String cfgHbm2ddAuto;
	private transient Boolean cfgShowSql;
	private transient String cfgContextClass;
	private transient Integer cfgBatchFetchSize;
	private transient Boolean cfgGenerateStatistics;

	private static EntityManagerHandlerFactory instance;
	private transient GenericCache<String, LocalContainerEntityManagerFactoryStore> cache;
	private transient final Timer timerEntityManagerFactory = new Timer();
	private transient DatasourceConfiguration internalDatasourceConfiguration = new DatasourceConfiguration();
	private static String internalSyncDbKey = "internalSyncDbKey";
	
	private EntityManagerHandlerFactory() {
		entityBasePackage = utilsFactory.getConfig("entity.base.package");
		pgPackages = utilsFactory.getConfig("entity.Postgres.packages");
		myPackages = utilsFactory.getConfig("entity.MySQL.packages");
		cfgHbm2ddAuto = utilsFactory.getConfig("org.hibernate.cfg.hbm2ddl_auto");
		cfgShowSql = Boolean.valueOf(utilsFactory.getConfig("org.hibernate.cfg.show_sql"));
		cfgContextClass = utilsFactory.getConfig("org.hibernate.cfg.current_session_context_class");
		cfgBatchFetchSize = Integer.valueOf(utilsFactory.getConfig("org.hibernate.cfg.default_batch_fetch_size"));
		cfgGenerateStatistics = Boolean.valueOf(utilsFactory.getConfig("org.hibernate.cfg.generate_statistics"));
		
		cache = new GenericCache<String, LocalContainerEntityManagerFactoryStore>();
		
		// TODO: this should be stopped when everything else is stopped!
		// schedule to run 15 mins.
		timerEntityManagerFactory.schedule(new EntityManagerFactoryTask(), 0, 1000 * 60 * 15);
		
		// sync database credentials
		String jdbcUrl = "jdbc:postgresql://{HOSTNAME}{PORT}/{DATABASE}?zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=true";
		jdbcUrl = jdbcUrl.replace("{HOSTNAME}", System.getenv("PG_DB_HOST"));
		jdbcUrl = jdbcUrl.replace("{PORT}", (":" + System.getenv("PG_DB_PORT")));
		jdbcUrl = jdbcUrl.replace("{DATABASE}", System.getenv("PG_DB_DBNAME"));
		internalDatasourceConfiguration.setJdbcUrl(jdbcUrl);
		internalDatasourceConfiguration.setUser(System.getenv("PG_DB_USER"));
		internalDatasourceConfiguration.setPassword(System.getenv("PG_DB_PASSWORD"));
		internalDatasourceConfiguration.setType(Configuration.PostgresType.value());
	}
	
	/**
	 * Instance
	 */
	public static EntityManagerHandlerFactory factory() {
		if (instance == null) {
			instance = new EntityManagerHandlerFactory();
		}
		return instance;
	}
	
	/**
	 * Get entity manager factory
	 * @param database
	 */
	public EntityManagerFactory getEntityManagerFactory() {
		return getEntityManagerFactory(internalSyncDbKey, internalDatasourceConfiguration);
	}

	/**
	 * Update
	 * @param key
	 */
	public void update(String key) {
		LocalContainerEntityManagerFactoryStore containerStore = cache.get(key);
		if (containerStore != null) {
			containerStore.setDate(new Date());
			cache.set(key, containerStore);
		}
	}
	
	/**
	 * Remove
	 * @param key
	 * @throws Exception
	 */
	private void remove(String key) {
		cache.delete(key);
	}
	
	/**
	 * Get local container entity manager factory beans
	 * @return {Map}
	 */
	@SuppressWarnings("rawtypes")
	private Map getLocalContainerEntityManagerFactoryBeans() {
		return cache.getMap();
	}
	
	/**
	 * Get entity manager factory
	 * @param key
	 * @param datasourceConfiguration
	 * @return
	 */
	public EntityManagerFactory getEntityManagerFactory(String key, DatasourceConfiguration datasourceConfiguration) {
		String prefix = "getEntityManagerFactory() ";
		EntityManagerFactory entityManagerFactory = null;
		LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = null;
		LocalContainerEntityManagerFactoryStore containerStore = cache.get(key);

		logger.info(prefix + key);

		// validate
		if (containerStore != null) {
			try {
				localContainerEntityManagerFactoryBean = (LocalContainerEntityManagerFactoryBean) containerStore
						.getContainerFactory();
				entityManagerFactory = localContainerEntityManagerFactoryBean.getObject();
				if (entityManagerFactory == null || (entityManagerFactory != null && !entityManagerFactory.isOpen())) {
					localContainerEntityManagerFactoryBean = null;
				}
				
			} catch (Exception exc) {
				localContainerEntityManagerFactoryBean = null;
				logger.error(prefix + exc.getMessage());
				
			} finally {
				if (localContainerEntityManagerFactoryBean == null) {
					remove(key);
				}
			}
		}

		// when void, generate connection
		if (localContainerEntityManagerFactoryBean == null) {
			localContainerEntityManagerFactoryBean = configureLocalContainerEntityManagerFactoryBean(datasourceConfiguration);
		} 
		
		if (localContainerEntityManagerFactoryBean != null) {
			containerStore = new LocalContainerEntityManagerFactoryStore();
			containerStore.setDate(new Date());
			containerStore.setContainerFactory(localContainerEntityManagerFactoryBean);
			cache.set(key, containerStore);
			entityManagerFactory = localContainerEntityManagerFactoryBean.getObject();
			
		} else {
			remove(key);
		}
		
		return entityManagerFactory;
	}
	
	/**
	 * Configure local container entity manager factory
	 * @param datasourceConfiguration
	 */
	private LocalContainerEntityManagerFactoryBean configureLocalContainerEntityManagerFactoryBean(
			DatasourceConfiguration datasourceConfiguration) {
		String prefix = "configureLocalContainerEntityManagerFactoryBean() ";
		LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = null;
		if (datasourceConfiguration != null) {
			try {
				String source = null;
				String[] token = null;
				List<String> sources = new ArrayList<String>();
				int count = 0;
				if (datasourceConfiguration.getType().equals(Configuration.MySQLType.value())) {
					token = myPackages.split(",");
					for (count = 0; count < token.length; count++) {
						source = entityBasePackage + "." + token[count];
						logger.info(prefix + " source:" + source);
						sources.add(source);
					}
				} else {
					token = pgPackages.split(",");
					for (count = 0; count < token.length; count++) {
						source = entityBasePackage + "." + token[count];
						logger.info(prefix + " source:" + source);
						sources.add(source);
					}
				}
				
				String[] entities = new String[sources.size()];
				count = 0;
				for (String item : sources) {
					entities[count] = item;
					count++;
				}
				localContainerEntityManagerFactoryBean = getLocalContainerEntityManagerFactoryBean(datasourceConfiguration, entities);
			} catch (Exception exc) {
				logger.error(prefix + exc.getMessage());
				// close the connection when having issues on entities.
				try {
					if (localContainerEntityManagerFactoryBean != null) {
						((HikariDataSource) localContainerEntityManagerFactoryBean.getDataSource()).close();
						localContainerEntityManagerFactoryBean.getObject().close();
						localContainerEntityManagerFactoryBean.destroy();
						localContainerEntityManagerFactoryBean = null;
						logger.warn(prefix + " terminate localContainerEntityManagerFactoryBean");
					}

				} catch (Exception exc2) {
					logger.error(prefix + exc.getMessage());
					localContainerEntityManagerFactoryBean = null;
				}
			}
		}
		return localContainerEntityManagerFactoryBean;
	}
	
	/**
	 * Get local container entity manager factory bean
	 * @param datasourceConfiguration
	 * @param entities
	 * @return
	 */
	private LocalContainerEntityManagerFactoryBean getLocalContainerEntityManagerFactoryBean(DatasourceConfiguration datasourceConfiguration,
			String[] entities) {
		String prefix = "getLocalContainerEntityManagerFactoryBean() ";

		LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		localContainerEntityManagerFactoryBean.setPackagesToScan(entities);
		localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		localContainerEntityManagerFactoryBean
				.setPersistenceUnitName("persistenceUnitName" + System.currentTimeMillis());
		Properties jpaProperties = new Properties();
		
		//TODO: make datasource and dbhost more multi-tenant e.g. fetch DB host from somewhere based on datasource
		//      for now just make datasource == dbhost
		//temporary code:
		String finalJdbcUrl = datasourceConfiguration.getJdbcUrl();
		
		if (datasourceConfiguration.getDbHost() != null && !datasourceConfiguration.getDbHost().isEmpty()) {
			finalJdbcUrl = datasourceConfiguration.getJdbcUrl().replace("{MYSQL_DATABASE}", datasourceConfiguration.getDbHost());
		}
		
		// generic settings
		jpaProperties.put("hibernate.hbm2ddl.auto", cfgHbm2ddAuto);
		jpaProperties.put("hibernate.show_sql", cfgShowSql);
		jpaProperties.put("hibernate.current_session_context_class", cfgContextClass);
		jpaProperties.put("hibernate.default_batch_fetch_size", cfgBatchFetchSize);
		jpaProperties.put("hibernate.generate_statistics", cfgGenerateStatistics);
		jpaProperties.put("hibernate.batch_strategy", cfgGenerateStatistics);
		jpaProperties.put("hibernate.user_new_id_genrator_mappings", false);
	
		String driver = null;
		try {
			if (datasourceConfiguration.getType().equalsIgnoreCase(Configuration.MySQLType.value())) {
				jpaProperties.put(Configuration.HibernateDialect.value, Configuration.MySQLHibernateDialect.value);
				driver = Configuration.MySQLDriver.value();
			} else if (datasourceConfiguration.getType().equalsIgnoreCase(Configuration.PostgresType.value())) {
				jpaProperties.put(Configuration.HibernateDialect.value, Configuration.PostgresHibernateDialect.value);
				driver = Configuration.PostgresDriver.value();
			}
			logger.info(prefix + "-----------------------------------------------------------------------");
			//logger.info(prefix + "{datasource}:" + datasourceConfiguration.getJdbcUrl());
			logger.info(prefix + "{datasource}:" + finalJdbcUrl);
			logger.info(prefix + "-----------------------------------------------------------------------");

			HikariConfig cpConfig = new HikariConfig();
			//cpConfig.setJdbcUrl(datasourceConfiguration.getJdbcUrl());
			cpConfig.setJdbcUrl(finalJdbcUrl);
			cpConfig.setUsername(datasourceConfiguration.getUser());
			cpConfig.setPassword(datasourceConfiguration.getPassword());
			cpConfig.setMaximumPoolSize(15);
			cpConfig.setMinimumIdle(5);
			cpConfig.setConnectionTestQuery("SELECT 1");
			cpConfig.setConnectionTimeout(60000);
			cpConfig.setIdleTimeout(28740000);
			cpConfig.setDriverClassName(driver);
			HikariDataSource cpDatasource = new HikariDataSource(cpConfig);
			localContainerEntityManagerFactoryBean.setDataSource(cpDatasource);
			localContainerEntityManagerFactoryBean.setJpaProperties(jpaProperties);
			localContainerEntityManagerFactoryBean.afterPropertiesSet();
		} catch (Exception exc) {
			exc.printStackTrace();
			localContainerEntityManagerFactoryBean = null;
		}
		return localContainerEntityManagerFactoryBean;
	}
}





