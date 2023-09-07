package com.frankmoley.lil.api.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frankmoley.lil.api.data.entity.CustomerEntity;
import com.frankmoley.lil.api.web.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CustomerControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    CustomerEntity customerEntity;
    Customer customer;
    Customer customer2;

    @BeforeEach
    void setUp(){
        customerEntity = new CustomerEntity(UUID.randomUUID(),
                "firstName", "lastname", "email@test.com" , "555-515-1234", "1234 Mary Street");
        customer = new Customer("38124691-9643-4f10-90a0-d980bca0b27d", "joe"
                ,"doe", "joe@email.com", "555-515-1234","1234 Mary Street");
        customer2 = new Customer("9ac775c3-a1d3-4a0e-a2df-3e4ee8b3a49a", "John"
                ,"Smith", "jsmith@email.com", "555-515-1234","1234 Mary Street");
    }

    @Test
    void getCustomers() throws Exception {
        this.mockMvc.perform(get("/customers")).andExpect(status().isOk())
                .andExpect(content().string(containsString("c04ca077-8c40-4437-b77a-41f510f3f185")))
                .andExpect(content().string(containsString("3b6c3ecc-fad7-49db-a14a-f396ed866e50")));
    }


    @Test
    void getCustomer() throws Exception {
       this.mockMvc.perform(get("/customers/054b145c-ddbc-4136-a2bd-7bf45ed1bef7"))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("Cally")));
    }

    @Test
    void getNonExistingCustomer() throws Exception{
        this.mockMvc.perform(get("/customers/38124691-9643-4f10-90a0-d980bca0b27e"))
                .andExpect(status().isNotFound());
    }
    @Test
    void addCustomer() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(customer);
        this.mockMvc.perform(post("/customers")
                .content(jsonString)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("joe@email.com")));
    }

    @Test
    void updateCustomer() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(customer2);
        this.mockMvc
                .perform(put("/customers/9ac775c3-a1d3-4a0e-a2df-3e4ee8b3a49a")
                        .content(jsonString)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("jsmith@email.com")));
    }
    @Test
    void updateNonCustomer() throws Exception{
        Customer updatedCustomer = new Customer("9ac775c3-a1d3-4a0e-a2df-3e4ee8b3a477", "Andrew"
                ,"Smitho", "andrewsmith@email.com", "555-515-1234","1234 Mary Street");
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(updatedCustomer);
        this.mockMvc
                .perform(put("/customers/c04ca077-8c40-4437-b77a-41f510f3f188")
                        .content(jsonString)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCustomer() throws Exception{
        this.mockMvc.perform(delete("/customers/38124691-9643-4f10-90a0-d980bca0b27d"))
                .andExpect(status().isResetContent());
    }
}
