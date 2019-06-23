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
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * JPA for employees table
 *
 */
@Getter
@Setter
@Entity
@Table(name = "employees")
@SuppressWarnings("serial")
public class Employee implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "row_id")
	private Integer rowId;
	
	@Column(name = "employee_id")
	private String employeeId;
	
	@Column(name = "firstname")
	private String firstName;
	
	@Column(name = "middlename")
	private String middleName;
	
	@Column(name = "lastname")
	private String lastname;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "department_id")
	private String emailAddress;
	
	@Column(name = "created_at", columnDefinition = "DATETIME")
	private Date createdAt;
	
	@Column(name = "updated_at", columnDefinition = "TIMESTAMP")
	private Date updatedAt;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false, nullable = false)
    User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id", referencedColumnName = "department_id", insertable = false, updatable = false, nullable = false)
	@JsonIgnore
	private Department department;
	
	@JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
	List<Item> items;
	
}

