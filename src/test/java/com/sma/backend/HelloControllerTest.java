/*
 * Copyright (c) 2022.
 */

package com.sma.backend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Sophea Mak
 * @since June-2022
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class HelloControllerTest {

  private MockMvc mockMvc;

  @Autowired private WebApplicationContext webApplicationContext;

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @AfterEach
  void tearDown() {}

  @Test
  void hello() throws Exception {
    String name = "testing";
    mockMvc
        .perform(
            get("/hello")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", name))
        .andExpect(status().isOk())
        //
        // .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        .andExpect(result -> result.toString().equals(String.format("Hello %s!", name)));
  }

  @Test
  void getMyBalance() throws Exception {
    mockMvc
        .perform(get("/api/myaccount/balance"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("cif").value("000000000"));
  }

  @Test
  void updateMyBalanace() throws Exception {
    mockMvc
        .perform(
            post("/api/myaccount/balance/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("cif", "00000000")
                .param("account", "000000000")
                .param("amount", "100.33")
                .param("ref", "ref-000000000"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("cif").value("00000000"));
  }
}
