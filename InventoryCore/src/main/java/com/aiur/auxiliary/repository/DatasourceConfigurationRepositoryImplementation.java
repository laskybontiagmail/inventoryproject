package com.aiur.auxiliary.repository;

import java.util.List;

import javax.persistence.EntityManager;

import com.aiur.auxiliary.entity.DatasourceConfigurationEntity;
import com.aiur.auxiliary.entity.QDatasourceConfigurationEntity;
import com.aiur.repository.BaseRepository;
import com.mysema.query.types.Predicate;


public class DatasourceConfigurationRepositoryImplementation extends BaseRepository<DatasourceConfigurationEntity, Long>  implements DatasourceConfigurationRepository {
	/**
	 * Constructor.
	 * @param entityManager - entity manager
	 */
	public DatasourceConfigurationRepositoryImplementation(EntityManager entityManager) {
		super(entityManager, DatasourceConfigurationEntity.class);
	}
	
	@Override
	public List<DatasourceConfigurationEntity> getAll() throws Exception {
		return this.getRepository().findAll();
	}
	
	@Override
	public DatasourceConfigurationEntity getDatasourceConfiguration(String dataSourceName) throws Exception {
		QDatasourceConfigurationEntity qDatasourceConfiguration = QDatasourceConfigurationEntity.datasourceConfigurationEntity;
		Predicate predicate = qDatasourceConfiguration.datasourceName.eq(dataSourceName);
		return this.getRepository().findOne(predicate);
	}
}


