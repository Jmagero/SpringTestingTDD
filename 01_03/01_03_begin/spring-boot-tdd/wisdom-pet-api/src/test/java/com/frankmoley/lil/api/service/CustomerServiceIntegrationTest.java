package com.frankmoley.lil.api.service;

import com.frankmoley.lil.api.data.entity.CustomerEntity;
import com.frankmoley.lil.api.web.error.ConflictException;
import com.frankmoley.lil.api.web.error.NotFoundException;
import com.frankmoley.lil.api.web.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CustomerServiceIntegrationTest {
    @Autowired
    CustomerService customerService;

    CustomerEntity customerEntity;
    Customer customer;
    @BeforeEach
    void setUp(){
        customerEntity = new CustomerEntity(UUID.randomUUID(),
                "firstName", "lastname", "email@test.com" , "555-515-1234", "1234 Mary Street");
        customer = new Customer(UUID.randomUUID().toString(), "joe"
                ,"doe", "joe@email.com", "555-515-1234","1234 Mary Street");
    }

    @Test
    void getAllCustomers(){
        List<Customer> customers = customerService.getAllCustomers();
        assertEquals(5, customers.size());
    }

    @Test
    void findByEmailAddress(){
        Customer customer = customerService.findByEmailAddress("penatibus.et@lectusa.com");
        assertEquals("Cally",customer.getFirstName());
    }

    @Test
    void addExistingCustomer(){
        Customer customer1 = customerService.findByEmailAddress("penatibus.et@lectusa.com");
        assertThrows(ConflictException.class, () -> customerService.addCustomer(customer1));
    }
    @Test
    void addNonExistingCustomer(){
        Customer customer2 = customerService.addCustomer(customer);
        assertTrue(StringUtils.isNotBlank(customer.getCustomerId()));
        assertEquals("joe", customer2.getFirstName());
    }
    @Test
    void getExistingCustomer(){
        Optional<Customer> optionalEntity = Optional.ofNullable(customerService.getCustomer("38124691-9643-4f10-90a0-d980bca0b27d"));
        assertEquals("Nolan", optionalEntity.get().getFirstName());
    }

    @Test
    void getNonExistingCustomer(){
        UUID id = UUID.randomUUID();
        assertThrows(NotFoundException.class, () -> customerService.getCustomer(id.toString()));
    }

    @Test
    void updateCustomer(){
        customerService.updateCustomer(customer);
    }
    @Test
    void deleteCustomer(){
        customerService.deleteCustomer(customer.getCustomerId().toString());
    }

}
