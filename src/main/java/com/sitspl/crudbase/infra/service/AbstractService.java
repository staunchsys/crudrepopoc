package com.sitspl.crudbase.infra.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sitspl.crudbase.infra.annotations.SoftDeleteEnabled;
import com.sitspl.crudbase.infra.dto.FilterTypeDto;
import com.sitspl.crudbase.infra.model.AbstractModel;
import com.sitspl.crudbase.infra.repository.AbstractRepository;

@Service
public abstract class AbstractService<T extends AbstractModel> {

	@Autowired
	protected AbstractRepository<T> repository;
	
	public T retrieve(String id) {
		return repository.findById(id).get();
	}

	private void setCreatingNewObject(T model) {
        if (model instanceof AbstractModel) {
            ((AbstractModel) model).setCreatingNewObject(((AbstractModel) model).getId() == null);
        }
    }
	
	@Transactional
	public T saveOrUpdate(T model) {
		model = preSave(model);
		model = repository.save(model);
		model = postSave(model);
		return model;
	}
	
	/**
	 * Override and update it to perform the Post save task. 
	 * This will be a transactional task.
	 * @param model
	 * @return
	 */
	protected T postSave(T model) {
		return model;
	}

	/**
	 * Override and update it to perform the Pre save task. 
	 * This will be a transactional task.
	 * @param model
	 * @return
	 */
	protected T preSave(T model) {
		setCreatingNewObject(model);
		
		return model;
	}

	@Transactional
	public Optional<T> delete(T model) {
		
		model = preDelete(model);
		
		Boolean isSoftDeleteEnabled = model.getClass().isAnnotationPresent(SoftDeleteEnabled.class);
		
		if (isSoftDeleteEnabled) {
			model.setDeletedOn(LocalDateTime.now());
			model = repository.save(model);
			model = postSoftDelete(model);
		} else {
			repository.delete(model);
			postHardDelete();
		}
		
		return Optional.of(model);
	}

	protected T preDelete(T model) {
		return model;
	}

	protected T postSoftDelete(T model) {
		return model;
	}

	private void postHardDelete() {
		// No implementation. Can override and implement the logic for required entities.
	}
	
	public List<T> retrieveAll() {
		return repository.findAll();
	}
	
	public Page<T> retrieveAll(int currentPage, int resultsPerPage, List<FilterTypeDto> filterOptions) {
		Pageable page = Pageable.ofSize(resultsPerPage).withPage(currentPage);
		AbstractSpecification<T> specifications = new AbstractSpecification<>(filterOptions);
		return repository.findAll(specifications, page);
	}
}
