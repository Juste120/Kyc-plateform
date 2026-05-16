package com.edgar.kyc.customer.internal;

import com.edgar.kyc.customer.internal.request.CustomerUpdateRequest;
import com.edgar.kyc.customer.internal.response.CustomerResponse;

/**
 * Interface du service de gestion des clients
 */
public interface CustomerService {
    CustomerResponse createCustomer(String email, String firstName, String lastName);
    CustomerResponse getCustomerByEmail(String email);
    CustomerResponse updateCustomer(String email, CustomerUpdateRequest request);
}