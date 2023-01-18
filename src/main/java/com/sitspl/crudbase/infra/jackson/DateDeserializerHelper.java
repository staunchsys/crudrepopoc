package com.sitspl.crudbase.infra.jackson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.sitspl.crudbase.infra.exception.DeserializerException;

public class DateDeserializerHelper extends JsonDeserializer<LocalDate> {

	private SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");
	
	@Override
	public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		String dateString = p.getText().trim();
		
		try {
			dateFormat.parse(dateString);
		} catch (ParseException e) {
			throw new DeserializerException(p, p.currentName(), e);
		}
		
		return LocalDate.parse(dateString);
	}

}
