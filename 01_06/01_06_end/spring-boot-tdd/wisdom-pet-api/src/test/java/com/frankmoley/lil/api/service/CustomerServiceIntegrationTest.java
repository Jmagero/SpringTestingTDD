package com.frankmoley.lil.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.frankmoley.lil.api.web.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase(replace= Replace.ANY)
public class CustomerServiceIntegrationTest {

  @Autowired
  CustomerService service;

  @Test
  void getAllCustomers(){
    List<Customer> customers = this.service.getAllCustomers();
    assertEquals(5, customers.size());
  }

}
