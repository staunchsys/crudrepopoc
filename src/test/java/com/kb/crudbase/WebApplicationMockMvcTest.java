package com.kb.crudbase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class WebApplicationMockMvcTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void shouldGetResultForGetPersonById() throws Exception {
		this.mockMvc.perform(get(URI.create("/api/person/ff80818180c233560180c23374860000")))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	public void shouldGetFilterOptionsForPerson() throws Exception {
		this.mockMvc.perform(get(URI.create("/api/person/listFilterOptions")))
		.andDo(print())
		.andExpect(status().isOk());	
	}
	
	@Test
	public void shouldGetResultForGetPersonsListWithoutFilter() throws Exception {
		StringBuilder listByFilterParams = new StringBuilder("?");
		listByFilterParams.append("currentPage=0&");
		listByFilterParams.append("resultsPerPage=10&");
		this.mockMvc.perform(get(URI.create("/api/person/listByFilter"+listByFilterParams.toString())))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	public void shouldGetResultForGetPersonsListWithFilter() throws Exception {
		StringBuilder listByFilterParams = new StringBuilder("?");
		listByFilterParams.append("currentPage=0&");
		listByFilterParams.append("resultsPerPage=10&");
		listByFilterParams.append("firstName=(starts_with)Disha&");
		this.mockMvc.perform(get(URI.create("/api/person/listByFilter"+listByFilterParams.toString())))
			.andDo(print())
			.andExpect(status().isOk());
	}
}
