package com.frankmoley.lil.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.frankmoley.lil.api.data.entity.CustomerEntity;
import com.frankmoley.lil.api.data.repository.CustomerRepository;
import com.frankmoley.lil.api.web.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

  @InjectMocks
  CustomerService customerService;

  @Mock
  CustomerRepository customerRepository;

  @Test
  void getAllCustomers(){
    Mockito.doReturn(getMockCustomers(2)).when(customerRepository).findAll();
    List<Customer> customers = this.customerService.getAllCustomers();
    assertEquals(2, customers.size());
  }

  private Iterable<CustomerEntity> getMockCustomers(int size){
    List<CustomerEntity> customers = new ArrayList<>(size);
    for(int i=0;i<size;i++){
      customers.add(new CustomerEntity(UUID.randomUUID(), "firstName" + i, "lastName" + i,
          "email"+i+"@test.com", "555-515-1234", "1234 Main Street"));
    }
    return customers;
  }

}
