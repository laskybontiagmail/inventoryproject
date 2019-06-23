package com.aiur.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * JPA for departments table
 *
 */
@Getter
@Setter
@Entity
@Table(name = "departments")
@SuppressWarnings("serial")
public class Department implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "row_id")
	private Integer rowId;
		
	@Column(name = "department_id")
	private String departmentId;
	
	@Column(name = "department_name")
	private String departmentName;
	
	@Column(name = "created_at", columnDefinition = "DATETIME")
	private Date createdAt;
	
	@Column(name = "updated_at", columnDefinition = "TIMESTAMP")
	private Date updatedAt;
	
	@JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department")
	List<Employee> employees;
}


