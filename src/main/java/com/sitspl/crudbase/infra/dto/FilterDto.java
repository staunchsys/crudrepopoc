package com.sitspl.crudbase.infra.dto;

public class FilterDto {

    private String field;
    private Operator operator;
    private Object[] value;
    private boolean isAndMode;

    public enum Operator {
        EQ,
        NE,
        LT,
        LE,
        GT,
        GE,
        LIKE,
        ILIKE,
        BETWEEN,
        IN,
        ISNULL,
        ISNOTNULL
    }

    public FilterDto() {
        this(null, Operator.EQ, null);
    }

    public FilterDto(String field, Object value[]) {
        this(field, Operator.EQ, value);
    }

    public FilterDto(String field, Operator op, Object value[]) {
        this(field, op, value , true);
    }
    
    public FilterDto(String field, Operator op, Object value[], boolean isAndMode) {
        this.field = field;
        this.operator = op;
        this.value = value;
        this.isAndMode = isAndMode;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOp(Operator op) {
        this.operator = op;
    }

    public Object[] getValue() {
        return value;
    }

    public void setValue(Object[] value) {
        this.value = value;
    }
    
    public boolean isAndMode() {
        return isAndMode;
    }

    public void setAndMode(boolean isAndMode) {
        this.isAndMode = isAndMode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FilterDto [field=").append(field).append(", op=").append(operator).append(", value=").append(value).append("]");
        return builder.toString();
    }

}
