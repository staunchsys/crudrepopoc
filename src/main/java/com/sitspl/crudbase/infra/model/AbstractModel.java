package com.sitspl.crudbase.infra.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sitspl.crudbase.infra.annotations.UXFilterable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@ToString
public abstract class AbstractModel {

	// Entity variables
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(length = 36)
	private String id;

	@JsonIgnoreProperties(allowGetters=true, allowSetters=false)
	@Getter
	@Setter
    private String createdBy;

	@JsonIgnoreProperties(allowGetters=true, allowSetters=false)
	@Getter
	@Setter
	private String updatedBy;

    @Column(nullable = false)
    @CreationTimestamp
    @JsonIgnoreProperties(allowGetters=true, allowSetters=false)
    @Getter
	@Setter    
    private LocalDateTime createdOn;

    @UpdateTimestamp
    @JsonIgnoreProperties(allowGetters=true, allowSetters=false)
    @Getter
	@Setter
    private LocalDateTime updatedOn;
	
    @JsonIgnoreProperties(allowGetters=true, allowSetters=false)
    @UXFilterable
    @Getter
	@Setter
    private LocalDateTime deletedOn;
    
    @Version
    @JsonIgnore
    private Integer version;
    
	// Transient variables
	
	// this is used for preSave & postSave method communications!
	@Transient
    @JsonIgnore
    @Getter
    @Setter
    private Boolean creatingNewObject = false; 

	public AbstractModel(String id) {
		this.id = id;
	}

	public AbstractModel() {
	}

	// helper methods

	@Override
	public boolean equals(Object obj) {
		if (getId() != null)
			return getId().equals(((AbstractModel) obj).getId());
		return false;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (id != null && id.length() > 0)
			this.id = id;
		else
			this.id = null;
	}
}
