package com.aiur.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * JPA items table
 *
 */
@Getter
@Setter
@Entity
@Table(name = "items")
@SuppressWarnings("serial")
public class Item implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "row_id")
	private Integer rowId;
	
	@Column(name = "itemcode")
	private String itemCode;
	
	@Column(name = "parent_itemcode")
	private String parentItemCode;
	
	@Column(name = "serialnumber")
	private String serialNumber;
	
	@Column(name = "brand")
	private String brand;
	
	@Column(name = "model")
	private String model;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "is_kit_item", columnDefinition = "TINYINT", length = 1)
	private Boolean isKitItem;
	
	@Column(name = "price")
	private BigDecimal price;
	
	@Column(name = "employee_id")
	private String owner;
	
	@Column(name = "created_at", columnDefinition = "DATETIME")
	private Date createdAt;
	
	@Column(name = "updated_at", columnDefinition = "TIMESTAMP")
	private Date updatedAt;
	
	@JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentItem")
	List<Item> items;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_itemcode", referencedColumnName = "itemcode", insertable = false, updatable = false, nullable = false)
	@JsonIgnore
	private Item parentItem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id", referencedColumnName = "employee_id", insertable = false, updatable = false, nullable = false)
	@JsonIgnore
	private Employee employee;
}

