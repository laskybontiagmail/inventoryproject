package com.aiur.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.aiur.entity.Department;
import com.aiur.entity.QDepartment;
import com.mysema.query.types.Predicate;

public class DepartmentRepositoryImplementation extends BaseRepository<Department, Long> implements DepartmentRepository {
	/**
	 * Constructor.
	 * @param entityManager - entity manager
	 */
	public DepartmentRepositoryImplementation(EntityManager entityManager) {
		super(entityManager, Department.class);
	}
	
	@Override
	public List<Department> getAll() throws Exception{
		return this.getRepository().findAll();
	}

	@Override
	public Department getDepartment(String departmentId) throws Exception {
		QDepartment qDepartment = QDepartment.department;
		Predicate predicate = qDepartment.departmentId.eq(departmentId);
		return this.getRepository().findOne(predicate);
	}

	@Override
	public Department getDepartmentByName(String departmentName) throws Exception {
		QDepartment qDepartment = QDepartment.department;
		Predicate predicate = qDepartment.departmentName.eq(departmentName);
		return this.getRepository().findOne(predicate);
	}
	
	@Override
	public boolean insert(List<Department> departmentList) throws Exception {
		boolean inserted = false;
		
		if (departmentList != null && !departmentList.isEmpty()) {
			getEntityManager().getTransaction().begin();
			Department department;
			for (int count = 0; count < departmentList.size(); count++) {
				department = departmentList.get(count);
				department.setCreatedAt(new Date());
				department.setUpdatedAt(new Date());
				this.getEntityManager().persist(department);
				this.makeBatchSize(count);
			}

			inserted = true;
			this.getEntityManager().getTransaction().commit();
		}
		return inserted;
	}
	
	@Override
	public boolean insert(Department department) throws Exception {
		boolean inserted = false;
		
		this.getEntityManager().getTransaction().begin();
		department.setCreatedAt(new Date());
		department.setUpdatedAt(new Date());
		this.getEntityManager().persist(department);
		inserted = true;
		this.getEntityManager().getTransaction().commit();
		
		return inserted;
	}
	
	@Override
	public boolean update(List<Department> departmentList) throws Exception {
		boolean updated = false;
		
		if (departmentList != null && !departmentList.isEmpty()) {
			this.getEntityManager().getTransaction().begin();
			Department department;
			for (int count = 0; count < departmentList.size(); count++) {
				department = departmentList.get(count);
				department.setUpdatedAt(new Date());
				this.getEntityManager().merge(department);
				this.makeBatchSize(count);
			}

			updated = true;
			this.getEntityManager().getTransaction().commit();
		}
		
		return updated;
	}
	
	@Override
	public boolean update(Department department) throws Exception {
		boolean updated = false;
		
		this.getEntityManager().getTransaction().begin();
		if (department != null) {
			department.setUpdatedAt(new Date());
			this.getEntityManager().merge(department);
			updated = true;
		}
		this.getEntityManager().getTransaction().commit();
		
		return updated;
	}
	
	@Override
	public boolean delete(List<Department> departmentList) throws Exception {
		boolean deleted = false;
		
		if (departmentList != null && !departmentList.isEmpty()) {
			this.getEntityManager().getTransaction().begin();
			Department department;
			for (int count = 0; count < departmentList.size(); count++) {
				department = departmentList.get(count);
				this.getEntityManager().remove(department);
				this.makeBatchSize(count);
			}

			deleted = true;
			this.getEntityManager().getTransaction().commit();
		}
		
		return deleted;
	}
	
	@Override
	public boolean delete(Department department) throws Exception {
		boolean deleted = false;
		
		this.getEntityManager().getTransaction().begin();
		if (department != null) {
			department.setUpdatedAt(new Date());
			this.getEntityManager().remove(department);
			deleted = true;
		}
		this.getEntityManager().getTransaction().commit();
		
		return deleted;
	}
}


