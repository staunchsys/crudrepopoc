package com.sitspl.crudbase.custom.client;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.sitspl.crudbase.infra.annotations.UXFilterable;
import com.sitspl.crudbase.infra.enums.UIElement;
import com.sitspl.crudbase.infra.model.AbstractModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "client")
@Getter @Setter
@NoArgsConstructor
public class Client extends AbstractModel {

	@UXFilterable(type = UIElement.TEXT, label = "Name")
	private String name;
	
	@UXFilterable(type = UIElement.TEXT, label = "Phone")
	private String phone;
	
	@UXFilterable(type = UIElement.TEXT, label = "Email")
	private String email;

	@UXFilterable(type = UIElement.TEXT, label = "Company")
	private String company;
	
	private String address;
	
}
