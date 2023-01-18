package com.sitspl.crudbase.infra.controller;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sitspl.crudbase.infra.annotations.UXFilterable;
import com.sitspl.crudbase.infra.dto.FilterDto;
import com.sitspl.crudbase.infra.dto.FilterTypeDto;
import com.sitspl.crudbase.infra.dto.FormOptionDto;
import com.sitspl.crudbase.infra.enums.ByteEnum;
import com.sitspl.crudbase.infra.enums.FilterOperators;
import com.sitspl.crudbase.infra.enums.IntegerEnum;
import com.sitspl.crudbase.infra.enums.StringEnum;
import com.sitspl.crudbase.infra.enums.UIElement;
import com.sitspl.crudbase.infra.exception.SearchFilterException;
import com.sitspl.crudbase.infra.model.AbstractModel;
import com.sitspl.crudbase.infra.service.AbstractService;
import com.sitspl.crudbase.infra.utils.ClassUtils;
import com.sitspl.crudbase.infra.utils.DateUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
public abstract class CrudController<T extends AbstractModel, S extends AbstractService<T>> extends AbstractController {

	protected static final String CURRENT_PAGE_KEY = "currentPage";
	protected static final int DEFAULT_CURRENT_PAGE = 0;

	protected static final String RESULTS_PER_PAGE_KEY = "resultsPerPage";
	protected static final int DEFAULT_RESULTS_PER_PAGE = 10;

	@Autowired
	private S service;

	@Getter
	private Class<T> modelClass;

	public CrudController() {
		this(null, null);
	}

	public CrudController(Class<T> modelClass, S service) {
		this.modelClass = modelClass;
		this.service = service;
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<T> getModel(@PathVariable(value = "id") String id) {
		preGet(id);
		T model = service.retrieve(id);
		model = postGet(model);
		return ResponseEntity.of(Optional.of(model));
	}

	protected T postGet(T model) {
		return model;
	}

	protected void preGet(String id) {}

	@PostMapping(value = "")
	public ResponseEntity<T> saveModel(@Valid @RequestBody T model) {
		model = prePostNonTransactional(model);
		model = service.saveOrUpdate(model);
		model = postPostNonTransactional(model);
		return ResponseEntity.of(Optional.of(model));
	}

	protected T postPostNonTransactional(T model) {
		return model;
	}

	protected T prePostNonTransactional(T model) {
		return model;
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<T> updateModel(@PathVariable(value = "id") String id, @RequestBody @Validated T model) {
		model.setId(id);
		model = prePutNonTransactional(model);
		model = service.saveOrUpdate(model);
		model = postPutNonTransactional(model);
		return ResponseEntity.of(Optional.of(model));
	}

	protected T postPutNonTransactional(T model) {
		return model;
	}

	protected T prePutNonTransactional(T model) {
		return model;
	}

	@DeleteMapping(value = "/{id}")
	public void deleteModel(@PathVariable(value = "id") String id) {
		T model = service.retrieve(id);
		model = preDeleteNonTransactional(model);
		Optional<T> optionalModel = (Optional<T>) service.delete(model);
		if (optionalModel.isPresent()) {
			model = optionalModel.get();
			model = postDeleteNonTransactional(model);
		} else {
			postDeleteNonTransactional();
		}
	}

	protected void postDeleteNonTransactional() {
		// Nothing implemented. Override and implement for business logic.
	}

	protected T preDeleteNonTransactional(T model) {
		return model;
	}

	protected T postDeleteNonTransactional(T model) {
		return model;
	}

	/**
	 * To get the getByAll crud for any of the entity, please override this method with super call and provide Get Mapping.
	 * @return
	 */
	protected ResponseEntity<List<T>> getByAll() {
		return ResponseEntity.of(Optional.of(service.retrieveAll()));
	}
	
	@GetMapping(value = "/listFilterOptions")
	public ResponseEntity<List<FilterTypeDto>> getListFilterOptions() {
		List<FilterTypeDto> filterOptions = getFilterOptions(new ArrayList<FilterTypeDto>());
		return ResponseEntity.of(Optional.of(filterOptions));
	}
	
	@GetMapping(value = "/listByFilter")
	@ApiImplicitParams({
		@ApiImplicitParam(name = CURRENT_PAGE_KEY, paramType = "query", dataTypeClass =  Integer.class, value = "Current Page to retrieve"),
		@ApiImplicitParam(name = RESULTS_PER_PAGE_KEY, paramType = "query", dataTypeClass =  Integer.class, value = "Results Per Page to retrieve")
	})
	public ResponseEntity<Page<T>> getByFilter(@ApiParam(hidden = true) @RequestParam(required = false) MultiValueMap<String, String> filters) throws SearchFilterException {
		List<FilterTypeDto> filterOptions = getUpdatedFilterOptionsByFilter(filters);
		int currentPage = DEFAULT_CURRENT_PAGE;
		if (filters.containsKey(CURRENT_PAGE_KEY)) {
			currentPage = Integer.parseInt(filters.get(CURRENT_PAGE_KEY).get(0));
		}
		int resultsPerPage = DEFAULT_RESULTS_PER_PAGE;
		if (filters.containsKey(RESULTS_PER_PAGE_KEY)) {
			resultsPerPage = Integer.parseInt(filters.get(RESULTS_PER_PAGE_KEY).get(0));
		}
		
		Page<T> page = service.retrieveAll(currentPage, resultsPerPage, filterOptions);
		page = postRetrieveData(page);
		
		return ResponseEntity.of(Optional.of(page));
	}

	protected Page<T> postRetrieveData(Page<T> page) {
		return page;
	}

	public List<FilterTypeDto> getUpdatedFilterOptionsByFilter(MultiValueMap<String, String> filters) throws SearchFilterException{
		List<FilterTypeDto> filterOptions = getFilterOptions(new ArrayList<FilterTypeDto>());
		
		for (FilterTypeDto filterTypeDto : filterOptions) {
			for (String filterKey: filters.keySet()) {
				List<String> filterValue = filters.get(filterKey);
				
				if (filterTypeDto.getName().equals(filterKey)) {
					filterTypeDto.setUseValue(Boolean.TRUE);
					// Pattern to retrieve filter options
					// (\(\w+\))([\w-]+)*
					Pattern p = Pattern.compile("(\\(\\w+\\))([\\w-\\.@\\s]+)*");
					StringBuilder sb = new StringBuilder();
					for (String fv : filterValue) {
						sb.append(fv).append("-");
					}
					sb.deleteCharAt(sb.length() - 1);
					Matcher m = p.matcher(sb.toString());
					if(m.matches()) {
						StringBuilder sbOperator = new StringBuilder(m.group(1));
						FilterOperators filterOperatorPassed = FilterOperators.valueOf(sbOperator.deleteCharAt(0).deleteCharAt(sbOperator.length() - 1).toString().toUpperCase()); 
						if (!(FilterOperators.NULL.equals(filterOperatorPassed) || FilterOperators.NOT_NULL.equals(filterOperatorPassed))) {
							filterTypeDto.setSelectedValue(new String[] {m.group(2).toString()});
						}
					    filterTypeDto.setSelectedFilterOperator(filterOperatorPassed);
					} else {
						throw new SearchFilterException();
					}
				}
			}
		}
		
		return filterOptions;
	}

	protected List<FilterTypeDto> getFilterOptions(List<FilterTypeDto> filterTypeDtos) {
		
		Field[] declaredFields = ClassUtils.getAnnotatedDeclaredFields(this.modelClass, UXFilterable.class, true);
		for (int declaredFieldCount = 0; declaredFieldCount < declaredFields.length; declaredFieldCount++) {
			Field field = declaredFields[declaredFieldCount];
			UXFilterable filterable = field.getDeclaredAnnotation(UXFilterable.class);
			addFilterType(filterTypeDtos, field, getFilterLabel(filterable, field.getName()), field.getName(),
					filterable.autoCompleteUrl(), filterable.type());
		}

		return filterTypeDtos;
	}

	private String getFilterLabel(UXFilterable filterable, String fieldName) {
		String label = filterable.label();
		return StringUtils.hasText(label) ? label : fieldName;
	}

	private void addFilterType(List<FilterTypeDto> filterTypeDtos, Field field, String label, String name,
			String autoCompleteUrl, UIElement element) {
		if (element == UIElement.NONE) {

			if (String.class.isAssignableFrom(field.getType())) {
				filterTypeDtos.add(new FilterTypeDto(label, name, "", UIElement.TEXT));
			} else if (Integer.class.isAssignableFrom(field.getType())
					|| BigInteger.class.isAssignableFrom(field.getType())) {
				filterTypeDtos.add(new FilterTypeDto(label, name, "", UIElement.NUMBER));
			} else if (Enum.class.isAssignableFrom(field.getType()) && field.getType().isEnum()) {
				List<FormOptionDto> enumValueList = new ArrayList<>();
				if (ByteEnum.class.isAssignableFrom(field.getType())) {
					ByteEnum[] byteEnums = (ByteEnum[]) field.getType().getEnumConstants();
					for (ByteEnum byteEnum : byteEnums) {
						enumValueList.add(new FormOptionDto(((Enum) byteEnum).name(), byteEnum.getLabel()));
					}
				} else if (StringEnum.class.isAssignableFrom(field.getType())) {
					StringEnum[] StringEnums = (StringEnum[]) field.getType().getEnumConstants();
					for (StringEnum stringEnum : StringEnums) {
						enumValueList.add(new FormOptionDto(((Enum) stringEnum).name(), stringEnum.getLabel()));
					}
				} else if (IntegerEnum.class.isAssignableFrom(field.getType())) {
					IntegerEnum[] integerEnums = (IntegerEnum[]) field.getType().getEnumConstants();
					for (IntegerEnum integerEnum : integerEnums) {
						enumValueList.add(new FormOptionDto(((Enum) integerEnum).name(), integerEnum.getLabel()));
					}
				} else {
					Object[] enumObjects = field.getType().getEnumConstants();
					for (Object enumObject : enumObjects) {
						enumValueList.add(new FormOptionDto(((Enum) enumObject).name(), enumObject.toString()));
					}
				}
				filterTypeDtos.add(new FilterTypeDto(label, name, enumValueList, UIElement.MULTI));
			} else if (LocalDate.class.isAssignableFrom(field.getType())) {
				filterTypeDtos.add(new FilterTypeDto(label, name, "", UIElement.DATERANGE));
			} else if (LocalDateTime.class.isAssignableFrom(field.getType())) {
				filterTypeDtos.add(new FilterTypeDto(label, name, "", UIElement.DATETIMERANGE));
			} else if (LocalTime.class.isAssignableFrom(field.getType())) {
				filterTypeDtos.add(new FilterTypeDto(label, name, "", UIElement.TIMERANGE));
			} else if (Boolean.class.isAssignableFrom(field.getType())) {
				filterTypeDtos.add(new FilterTypeDto(label, name, "", UIElement.CHECKBOX));
			}
		} else {
			filterTypeDtos.add(new FilterTypeDto(label, name, "", element, autoCompleteUrl));
		}
	}

	protected void setFilterValues(List<FilterDto> filters, List<FilterTypeDto> filterOptions) {
		for (FilterTypeDto filterOptionDto : filterOptions) {
			for (FilterDto filterDto : filters) {
				if (filterDto.getField().equalsIgnoreCase(filterOptionDto.getName())) {
					if (UIElement.TEXT == filterOptionDto.getUiElement()
							|| UIElement.NUMBER == filterOptionDto.getUiElement()) {
						filterOptionDto.setValue(filterDto.getValue()[0]);
					} else if (UIElement.COMBO == filterOptionDto.getUiElement()
							|| UIElement.MULTI == filterOptionDto.getUiElement()) {
						filterOptionDto.setSelectedValue(filterDto.getValue());
					} else if (UIElement.DATERANGE == filterOptionDto.getUiElement()
							|| UIElement.TIMERANGE == filterOptionDto.getUiElement()
							|| UIElement.DATETIMERANGE == filterOptionDto.getUiElement()) {
						Object[] fromToDate = new Object[2];
						fromToDate[0] = filterDto.getValue()[0];
						if (filterDto.getValue().length == 2) {
							fromToDate[1] = filterDto.getValue()[1];
						}
						filterOptionDto.setSelectedValue(fromToDate);
					} else if (UIElement.CHECKBOX == filterOptionDto.getUiElement()) {
						filterOptionDto.setValue("on");
					} else if (UIElement.SINGLEDATE == filterOptionDto.getUiElement()) {
						filterOptionDto.setValue(filterDto.getValue()[0]);
					} else if (UIElement.AUTOCOMPLETETEXT == filterOptionDto.getUiElement()) {
						filterOptionDto.setSelectedValue(filterDto.getValue());
					}
				}
			}
		}

	}

	private List<FilterDto> buildFilters(Map<String, String[]> params, boolean includeFilter) {
		Object[] timeArray = new Object[2];
		String timeColumnName = "";
		List<FilterDto> filters = new ArrayList<>();
		for (String key : params.keySet()) {
			String[] values = params.get(key);
			List<String> valuesList = new ArrayList<>();
			for (int i = 0; i < values.length; i++) {
				if (StringUtils.hasText(values[i])) {
					valuesList.add(String.valueOf(values[i]));
				}
			}
			if (valuesList.isEmpty()) {
				continue;
			}
			String[] valueArr = valuesList.toArray(new String[valuesList.size()]);

			if (key.startsWith(FILTER_PREFIX_KEY)) {
				boolean isDateTime = false;
				boolean isILike = false;
				boolean isCheckbox = false;
				boolean isTime = false;
				boolean isDate = false;
				if (key.startsWith(PREFIX_DATETIME)) {
					isDateTime = true;
				} else if (key.startsWith(PREFIX_DATE)) {
					isDate = true;
				} else if (key.startsWith(PREFIX_ILIKE)) {
					isILike = true;
				} else if (key.startsWith(PREFIX_CHECKBOX)) {
					isCheckbox = true;
				} else if (key.startsWith(PREFIX_TIME_RANGE)) {
					isTime = true;
				}

				if (isDateTime) {
					for (String value : valueArr) {
						String dates[] = value.split("\\|");
						String column = key.substring(PREFIX_DATETIME.length(), key.length());
						if (dates.length == 2) {
							try {
								LocalDateTime from = LocalDateTime.parse(dates[0].trim(),
										DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_TO_BE_PARSED_FROM_FILTER));
								LocalDateTime to = LocalDateTime.parse(dates[1].trim(),
										DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_TO_BE_PARSED_FROM_FILTER));
								Object localDateTimes[] = { from, to };
								filters.add(new FilterDto(column, FilterDto.Operator.BETWEEN, localDateTimes));
							} catch (DateTimeParseException e) {
								log.error(e.getMessage(), e);
							}

						} else {
							try {
								LocalDateTime date = LocalDateTime.parse(value.trim(),
										DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_TO_BE_PARSED_FROM_FILTER));
								Object localDate[] = { date, date.toLocalDate().atTime(DateUtils.MAX_TIME) };
								filters.add(new FilterDto(column, FilterDto.Operator.BETWEEN, localDate));
							} catch (DateTimeParseException e) {
								log.error(e.getMessage(), e);
							}

						}
					}
				} else if (isDate) {
					for (String value : valueArr) {
						String dates[] = value.split("-");
						String column = key.substring(PREFIX_DATE.length(), key.length());
						if (dates.length == 2) {
							try {
								LocalDate from = LocalDate.parse(dates[0].trim(),
										DateTimeFormatter.ofPattern(DATE_FORMAT_TO_BE_PARSED_FROM_FILTER));
								LocalDate to = LocalDate.parse(dates[1].trim(),
										DateTimeFormatter.ofPattern(DATE_FORMAT_TO_BE_PARSED_FROM_FILTER));
								Object localDate[] = { from, to };
								filters.add(new FilterDto(column, FilterDto.Operator.BETWEEN, localDate));
							} catch (DateTimeParseException e) {
								log.error(e.getMessage(), e);
							}

						} else {
							try {
								LocalDate date = LocalDate.parse(value.trim(),
										DateTimeFormatter.ofPattern(DATE_FORMAT_TO_BE_PARSED_FROM_FILTER));
								Object localDate[] = { date };
								filters.add(new FilterDto(column, localDate));
							} catch (DateTimeParseException e) {
								log.error(e.getMessage(), e);
							}
						}
					}
				} else if (isILike) {
					filters.add(new FilterDto(key.substring(PREFIX_ILIKE.length(), key.length()),
							FilterDto.Operator.ILIKE, valueArr));
				} else if (isCheckbox) {
					Boolean checkBoxVal = "on".equalsIgnoreCase(valueArr[0]) ? true : false;
					filters.add(new FilterDto(key.substring(PREFIX_CHECKBOX.length(), key.length()),
							new Object[] { checkBoxVal }));
				} else if (isTime) {
					String column = key.substring(PREFIX_TIME_RANGE.length(), key.length() - 1);
					timeColumnName = column;

					try {
						if (key.equals(PREFIX_TIME_RANGE + column + "1") && timeArray[0] == null) {
							timeArray[0] = LocalTime.parse(valueArr[0].trim(),
									DateTimeFormatter.ofPattern(CUSTOM_TIME_FORMAT));
						}

						if (key.equals(PREFIX_TIME_RANGE + column + "2") && timeArray != null && timeArray[0] != null) {
							timeArray[1] = LocalTime.parse(valueArr[0].trim(),
									DateTimeFormatter.ofPattern(CUSTOM_TIME_FORMAT));
						}
					} catch (DateTimeParseException e) {
						log.error(e.getMessage(), e);
					}

				}
			} else if (!(key.equalsIgnoreCase(CURRENT_PAGE_KEY) || key.equalsIgnoreCase("contentOnly")
					|| key.equalsIgnoreCase(RESULTS_PER_PAGE_KEY) || key.equalsIgnoreCase("_")
					|| key.equalsIgnoreCase("editable") || key.equalsIgnoreCase("filterId")
					|| key.equalsIgnoreCase("sort") || key.equalsIgnoreCase("fieldName")
					|| key.equalsIgnoreCase("fieldValue") || key.equalsIgnoreCase("searchType"))) {
				filters.add(new FilterDto(key, valueArr));
			}
		}
		if (timeArray[0] != null && timeArray[1] != null && !timeColumnName.isEmpty()) {
			filters.add(new FilterDto(timeColumnName, FilterDto.Operator.BETWEEN, timeArray));
		}
		return filters;

	}
}
