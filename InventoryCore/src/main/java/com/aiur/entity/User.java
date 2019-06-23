package com.aiur.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.Getter;
import lombok.Setter;

/**
 * JPA for users
 *
 */
@Getter
@Setter
@Entity
@Table(name = "users")
@SuppressWarnings("serial")
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "row_id")
	private Integer rowId;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "email_address")
	private String emailAddress;
	
	@Column(name = "created_at", columnDefinition = "DATETIME")
	private Date createdAt;
	
	@Column(name = "updated_at", columnDefinition = "DATETIME")
	private Date updatedAt;
	
}


