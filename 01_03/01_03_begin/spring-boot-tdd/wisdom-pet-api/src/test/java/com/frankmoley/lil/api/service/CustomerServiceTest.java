package com.frankmoley.lil.api.service;

import com.frankmoley.lil.api.data.entity.CustomerEntity;
import com.frankmoley.lil.api.data.repository.CustomerRepository;
import com.frankmoley.lil.api.web.error.ConflictException;
import com.frankmoley.lil.api.web.error.NotFoundException;
import com.frankmoley.lil.api.web.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @InjectMocks
    CustomerService customerService;

    @Mock
    CustomerRepository customerRepository;


    CustomerEntity customerEntity;
    Customer customer;
    @BeforeEach void setUp(){
        customerEntity = new CustomerEntity(UUID.randomUUID(),
                "firstName", "lastname", "email@test.com" , "555-515-1234", "1234 Mary Street");
        customer = new Customer(UUID.randomUUID().toString(), "joe"
                ,"doe", "joe@email.com", "555-515-1234","1234 Mary Street");
    }


    @Test
    void getAllCustomers(){
        Mockito.doReturn(getMockCustomers(2)).when(customerRepository).findAll();
        List<Customer> customers = customerService.getAllCustomers();
        assertEquals(2,customers.size());
    }

    private Iterable<CustomerEntity> getMockCustomers(int size){
        List<CustomerEntity> customers = new ArrayList<>();
        for(int i = 0; i < size; i++){
            customers.add( new CustomerEntity(UUID.randomUUID(),
                    "firstName"+ i, "lastname"+ i, "email" + i + "@test.com" , "555-515-1234", "1234 Mary Street"));
        }
        return customers;
    }

    @Test
    void getExistingCustomer(){
        Optional<CustomerEntity> optional = Optional.of(customerEntity);
        when(customerRepository.findById(any())).thenReturn(optional);
        Customer customer1 = customerService.getCustomer(customerEntity.getCustomerId().toString());
        assertNotNull(customer1);
        assertEquals("firstName", customer1.getFirstName());
    }

    @Test
    void getNonExistingCustomer(){
        Optional<CustomerEntity> optional = Optional.empty();
        when(customerRepository.findById(any())).thenReturn(optional);
        assertThrows(NotFoundException.class, () -> customerService.
                getCustomer(customerEntity.getCustomerId().toString()),"exception not throw as expected");
    }

    @Test
    void findByEmailAddress(){
       when(customerRepository.findByEmailAddress("email@test.com")).thenReturn(getMockCustomer("email@test.com"));
        Customer customer1 = customerService.findByEmailAddress("email@test.com");
        assertNotNull(customer1);
        assertEquals("email@test.com", customer1.getEmailAddress());
    }

    private CustomerEntity getMockCustomer(String emailAddress){
        if(emailAddress.equals("email@test.com")){
            return customerEntity;
        }
        return null;
    }

    @Test
    void addExistingCustomer(){
        when(customerRepository.findByEmailAddress(customer.getEmailAddress())).thenReturn(customerEntity);
        assertThrows(ConflictException.class, () -> customerService.addCustomer(customer));
    }

    @Test
    void addNonExistingCustomer(){
        when(customerRepository.findByEmailAddress(any())).thenReturn(null);
        when(customerRepository.save(any())).thenReturn(customerEntity);
        Customer customer1 = customerService.addCustomer(customer);
        assertEquals("email@test.com",customer1.getEmailAddress());
    }

    @Test
    void updateCustomer(){
        when(customerRepository.save(any())).thenReturn(customerEntity);
        Customer customer1 = customerService.updateCustomer(customer);
        assertNotNull(customer1);
        assertEquals("email@test.com",customer1.getEmailAddress());
    }

    @Test
    void deleteCustomer(){
        UUID id = UUID.randomUUID();
        customerRepository.deleteById(id);
        customerService.deleteCustomer(id.toString());
    }
}
