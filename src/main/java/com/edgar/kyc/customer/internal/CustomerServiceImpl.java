package com.edgar.kyc.customer.internal;

import com.edgar.kyc.customer.CustomerLookup;
import com.edgar.kyc.customer.CustomerUpdatedEvent;
import com.edgar.kyc.customer.internal.request.CustomerUpdateRequest;
import com.edgar.kyc.customer.internal.response.CustomerResponse;
import com.edgar.kyc.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation du service de gestion des clients
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerServiceImpl implements CustomerService, CustomerLookup {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public CustomerResponse createCustomer(String email, String firstName, String lastName) {
        log.info("Creating customer with email: {}", email);

        Customer customer = Customer.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        log.info("Customer created successfully with ID: {}", savedCustomer.getId());
        return customerMapper.toCustomerResponse(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));
        return customerMapper.toCustomerResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerInfo> findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(c -> new CustomerInfo(c.getId(), c.getEmail(), c.getFirstName()));
    }

    @Override
    public CustomerResponse updateCustomer(String email, CustomerUpdateRequest request) {
        log.info("Updating customer with email: {}", email);

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));

        customerMapper.updateCustomerFromRequest(request, customer);
        Customer updatedCustomer = customerRepository.save(customer);

        // Publier l'événement de mise à jour
        CustomerUpdatedEvent event = new CustomerUpdatedEvent(
                updatedCustomer.getId(),
                updatedCustomer.getEmail(),
                updatedCustomer.getFirstName(),
                updatedCustomer.getLastName(),
                updatedCustomer.getPhone()
        );
        eventPublisher.publishEvent(event);

        log.info("Customer updated successfully with ID: {}", updatedCustomer.getId());
        return customerMapper.toCustomerResponse(updatedCustomer);
    }
}