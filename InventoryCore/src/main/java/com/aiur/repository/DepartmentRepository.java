package com.aiur.repository;

import java.util.List;

import com.aiur.entity.Department;

public interface DepartmentRepository {
	List<Department> getAll() throws Exception;
	Department getDepartment(String departmentId) throws Exception;
	Department getDepartmentByName(String departmentName) throws Exception;
	
	boolean insert(List<Department> departmentList) throws Exception;
	boolean insert(Department department) throws Exception;
	
	boolean update(List<Department> departmentList) throws Exception;
	boolean update(Department department) throws Exception;
	
	boolean delete(List<Department> departmentList) throws Exception;
	boolean delete(Department department) throws Exception;
}

