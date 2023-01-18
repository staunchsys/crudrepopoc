package com.sitspl.crudbase.infra.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;

import com.sitspl.crudbase.infra.model.AbstractModel;

@NoRepositoryBean
public interface AbstractRepository<T extends AbstractModel> extends JpaRepositoryImplementation<T, String> {

}
