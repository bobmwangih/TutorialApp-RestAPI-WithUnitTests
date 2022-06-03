package com.bob.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.bob.model.Tutorial;
import com.bob.repository.TutorialRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TutorialController.class)
public class TutorialControllerTest {

	@MockBean
	private TutorialRepository tutorialRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldCreateTutorial() throws Exception {
		Tutorial tutorial = new Tutorial(1, "mufasa", "description", false);
		mockMvc.perform(post("/api/tutorials").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(tutorial))).andExpect(status().isCreated()).andDo(print());
	}

	@Test
	void shouldReturnTutorial() throws Exception {
		long id = 1L;
		Tutorial tutorial = new Tutorial(id, "mufasa", "description", false);
		when(tutorialRepository.findById(id)).thenReturn(Optional.of(tutorial));
		mockMvc.perform(get("/api/tutorials/{id}", id)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id))
				.andExpect(jsonPath("$.title").value(tutorial.getTitle()))
				.andExpect(jsonPath("$.description").value(tutorial.getDescription()))
				.andExpect(jsonPath("$.published").value(tutorial.isPublished())).andDo(print());
	}

	@Test
	void shouldReturnNotFoundTutorial() throws Exception {
		long id = 1L;
		when(tutorialRepository.findById(id)).thenReturn(Optional.empty());
		mockMvc.perform(get("/api/tutorials/{id}", id)).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	void shouldReturnListOfTutorials() throws Exception {
		List<Tutorial> tutorials = new ArrayList<>(
				Arrays.asList(new Tutorial(1, "book 1", "desc 1", true), new Tutorial(2, "book 2", "desc 2", false)));
		when(tutorialRepository.findAll()).thenReturn(tutorials);
		mockMvc.perform(get("/api/tutorials")).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(tutorials.size())).andDo(print());

	}

	@Test
	void shouldReturnListOfTutorialsWithFilters() throws Exception {
		List<Tutorial> tutorials = new ArrayList<>(
				Arrays.asList(new Tutorial(1, "Spring Boot @WebMvcTest", "Description 1", true),
						new Tutorial(3, "Spring Boot Web MVC", "Description 3", true)));
		String title = "Boot";
		MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
		paramsMap.add("title", title);
		when(tutorialRepository.findByTitleContaining(title)).thenReturn(tutorials);
		mockMvc.perform(get("/api/tutorials").params(paramsMap)).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(tutorials.size())).andDo(print());
	}

	@Test
	void shouldReturnNoContentWhenTitleDontExist() throws Exception {
		String title = "mzito";
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
		paramMap.add("title", title);

		List<Tutorial> tutorials = Collections.emptyList();
		when(tutorialRepository.findByTitleContaining(title)).thenReturn(tutorials);
		mockMvc.perform(get("/api/tutorials").params(paramMap)).andExpect(status().isNoContent()).andDo(print());
	}

	@Test
	void shouldUpdateTutorial() throws Exception {
		long id = 1L;
		Tutorial tutorial = new Tutorial(1, "book 1", "desc 1", false);
		Tutorial updatedTutorial = new Tutorial(1, "book 1", "desc 1", true);
		when(tutorialRepository.findById(id)).thenReturn(Optional.of(tutorial));
		when(tutorialRepository.save(any(Tutorial.class))).thenReturn(updatedTutorial);

		mockMvc.perform(put("/api/tutorials/{id}", id).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedTutorial))).andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value(updatedTutorial.getTitle()))
				.andExpect(jsonPath("$.description").value(updatedTutorial.getDescription()))
				.andExpect(jsonPath("$.published").value(updatedTutorial.isPublished())).andDo(print());
	}

	@Test
	void shouldDeleteTutorial() throws Exception {
		long id = 1L;
		doNothing().when(tutorialRepository).deleteById(id);
		mockMvc.perform(delete("/api/tutorials/{id}", id)).andExpect(status().isAccepted()).andDo(print());
	}

	@Test
	void shouldDeleteAllTutorials() throws Exception {
		doNothing().when(tutorialRepository).deleteAll();
		mockMvc.perform(delete("/api/tutorials")).andExpect(status().isAccepted()).andDo(print());
	}
}
