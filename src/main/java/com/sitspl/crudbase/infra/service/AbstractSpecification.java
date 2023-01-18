package com.sitspl.crudbase.infra.service;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sitspl.crudbase.infra.dto.FilterTypeDto;
import com.sitspl.crudbase.infra.model.AbstractModel;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class AbstractSpecification<T extends AbstractModel> implements Specification<T> {

	private static final long serialVersionUID = -3699922870540166716L;
	
	private List<FilterTypeDto> filterOptions;
	
	public AbstractSpecification(List<FilterTypeDto> filterOptions) {
		this.filterOptions = filterOptions;
	}
	
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		Predicate mainPredicate = null;
		
		for(FilterTypeDto f: getFilterOptions()) {
			if (f.getUseValue()) {
				Predicate predicate = null;
				switch(f.getSelectedFilterOperator()) {
				case EQ:
					predicate = criteriaBuilder.equal(root.get(f.getName()), f.getSelectedValue()[0]);	
					break;
				case NEQ:
					predicate = criteriaBuilder.notEqual(root.get(f.getName()), f.getSelectedValue()[0]);	
					break;
				case LE: {
					// TODO: convert to appropreat type.
					BigDecimal number = (BigDecimal)f.getSelectedValue()[0];
					predicate = criteriaBuilder.le(root.get(f.getName()), number);	
				}
				break;
				case GE: {
					// TODO: convert to appropreat type.
					BigDecimal number = (BigDecimal)f.getSelectedValue()[0];
					predicate = criteriaBuilder.ge(root.get(f.getName()), number);	
				}
				case STARTS_WITH: 
					predicate = criteriaBuilder.like(root.get(f.getName()), f.getSelectedValue()[0] + "%");	
					break;
				case ENDS_WITH: 
					predicate = criteriaBuilder.like(root.get(f.getName()), "%" + f.getSelectedValue()[0]);	
					break;
				case CONTAINS: 
					predicate = criteriaBuilder.like(root.get(f.getName()), "%" + f.getSelectedValue()[0] + "%");	
					break;
				case NULL: 
					predicate = criteriaBuilder.isNull(root.get(f.getName()));	
					break;
				case NOT_NULL: 
					predicate = criteriaBuilder.isNotNull(root.get(f.getName()));
					break;
				default:
					break;
				}
				
				if (predicate != null) {
					predicate = criteriaBuilder.and(predicate);
					mainPredicate = criteriaBuilder.and(predicate);
				}
			}
		}
		
		return mainPredicate;
	}

}
