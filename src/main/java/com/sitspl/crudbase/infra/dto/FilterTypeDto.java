package com.sitspl.crudbase.infra.dto;

import java.util.Arrays;
import java.util.List;

import com.sitspl.crudbase.infra.enums.FilterOperators;
import com.sitspl.crudbase.infra.enums.UIElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
public class FilterTypeDto {

	@Getter @Setter
    private String label;

	@Getter @Setter
    private String name;

	@Getter @Setter
    private Object value;

	@Getter @Setter
    private UIElement uiElement;

    private List<FilterOperators> availableFilterOperators;

    @Getter @Setter
    private Object[] selectedValue;

    @Getter @Setter
    private FilterOperators selectedFilterOperator;
    
    @Getter @Setter
    private Boolean useValue = false;

    @Getter @Setter
    private String autoCompleteUrl;

    public FilterTypeDto() {
    }

    public FilterTypeDto(String label, String name, Object value, UIElement uiElement) {
        super();
        this.label = label;
        this.name = name;
        this.value = value;
        this.uiElement = uiElement;
    }

    public FilterTypeDto(String label, String name, Object value, UIElement uiElement, String url) {
        super();
        this.label = label;
        this.name = name;
        this.value = value;
        this.uiElement = uiElement;
        this.autoCompleteUrl = url;
    }

    public List<FilterOperators> getAvailableFilterOperators() {
		return Arrays.asList(uiElement.getAvailableFilterOperators());
	}
    
	/*
	 * @Override public String toString() { StringBuilder builder = new
	 * StringBuilder();
	 * builder.append("FilterOptionDto [label=").append(label).append(", name=").
	 * append(name).append(", value=").append(value).append(", type=").append(
	 * uiElement).append(", useValue=").append(useValue).append("]"); return
	 * builder.toString(); }
	 */

}
