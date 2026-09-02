package com.rookies6.myspringboot4project.repository;

import com.rookies6.myspringboot4project.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@Transactional
class CustomerRepositoryTest {
    @Autowired
    CustomerRepository customerRepository;

    //1. Customer 등록
    @Test
    @Rollback(value = false) //등록된 데이터를 확인하려고 하니 Rollback 처리를 하지 마세요.
    void testCreate() {
        //Given(준비단계)
        Customer customer = new Customer();
        customer.setCustomerId("D002");
        customer.setCustomerName("길동3");
        //When(실행단계)
        Customer addCustomer = customerRepository.save(customer);
        //Then(검증단계)
        assertThat(addCustomer).isNotNull();
        assertThat(addCustomer.getCustomerName()).isEqualTo("길동3");
    }

    //2. Customer 조회
    @Test
    void testFindBy() {
        Optional<Customer> optionalCustomer = customerRepository.findById(1L);
        if(optionalCustomer.isPresent()){
            Customer existCustomer = optionalCustomer.get();
            assertThat(existCustomer.getId()).isEqualTo(1L);
        }
        //ifPresent(Consumer) Consumer의 추상메서드 void accept(T t)
        optionalCustomer.ifPresent(customer -> System.out.println(customer.getCustomerName()));
    }

    @Test
    void testFindByNotFound() {
        Customer notFoundCustomer = customerRepository.findByCustomerId("B001")
                .orElseGet(() -> new Customer());

        assertThat(notFoundCustomer.getCustomerId()).isNull();

        assertThrows(RuntimeException.class, () -> {
            customerRepository.findById(3L)
                    .orElseThrow(() -> new RuntimeException("Customer Not Found"));
        });
    }


    @Test
    //@Rollback(value = false)
    void testUpdate() {
        Customer customer = new Customer();
        customer.setCustomerId("A004");
        customer.setCustomerName("박둘리");

        Customer savedCustomer = customerRepository.save(customer);

        savedCustomer.setCustomerName("박둘리2");

        Customer updatedCustomer = customerRepository.save(savedCustomer);

        assertThat(updatedCustomer.getCustomerName()).isEqualTo("박둘리2");
    }
}