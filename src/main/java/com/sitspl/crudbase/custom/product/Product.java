package com.sitspl.crudbase.custom.product;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.sitspl.crudbase.infra.annotations.SoftDeleteEnabled;
import com.sitspl.crudbase.infra.annotations.UXFilterable;
import com.sitspl.crudbase.infra.enums.UIElement;
import com.sitspl.crudbase.infra.model.AbstractModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product")
@Getter @Setter
@NoArgsConstructor
@SoftDeleteEnabled
public class Product extends AbstractModel {

	@UXFilterable(type = UIElement.TEXT, label = "Name")
	@NotNull
	private String name;
	
	@UXFilterable(type = UIElement.TEXT, label = "Code")
	@NotNull
	@Size(min = 6, max = 6, message = "code should be exactly 3 characters")
	private String code;
	
	@UXFilterable(type = UIElement.TEXT, label = "Brand Name")
	@NotNull
	private String brandName;
	
	// @JsonDeserialize(using=DateDeserializerHelper.class)
	private LocalDate manufacturingDate;
	
	@Column(nullable = true, length = 6, scale = 2)
	private BigDecimal maxRetailPrice;
}
