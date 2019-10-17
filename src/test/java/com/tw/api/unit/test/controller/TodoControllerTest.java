package com.tw.api.unit.test.controller;

import com.tw.api.unit.test.controller.dto.ResourceWithUrl;
import com.tw.api.unit.test.domain.todo.Todo;
import com.tw.api.unit.test.domain.todo.TodoRepository;
import com.tw.api.unit.test.services.ShowService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TodoController.class)
@ActiveProfiles(profiles = "test")
public class TodoControllerTest {

    @Autowired
    private TodoController todoController;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TodoRepository todoRepository;

    @Test
    void shouldReturnListOfResourceUrl_usingMockMVC() throws Exception {
        //given
        when(todoRepository.getAll()).thenReturn(Arrays.asList(new Todo("item1", false)));

        //when
        ResultActions result = mvc.perform(get("/todos"));
        //then
        MvcResult requestResult = result.andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        assertEquals(requestResult.getResponse().getContentAsString(), "[{\"id\":0,\"title\":\"item1\",\"completed\":false,\"order\":0,\"url\":\"\"}]");
    }

    @Test
    void shouldReturnListOfResourceUrl() {
        //given
        given(todoRepository.getAll()).willReturn(Arrays.asList(new Todo("item1", false)));
        //when
        HttpEntity<Collection<ResourceWithUrl>> httpEntity = todoController.getAll();

        //then
        assertEquals(((ResponseEntity) httpEntity).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shouldReturnResourceWithUrlGivenTodo() throws Exception
    {
        Todo todo = new Todo(1, "title", false, 1);
        when(todoRepository.findById(1L)).thenReturn(java.util.Optional.of(todo));

        ResultActions result = mvc.perform(get("/todos/{id}", 1));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.completed", is(false)));

    }

}
