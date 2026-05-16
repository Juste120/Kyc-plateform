package com.edgar.kyc.customer.internal;

import com.edgar.kyc.customer.internal.request.CustomerUpdateRequest;
import com.edgar.kyc.customer.internal.response.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapper pour convertir les entités Customer en DTOs et vice versa
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponse toCustomerResponse(Customer customer);

    void updateCustomerFromRequest(CustomerUpdateRequest request, @MappingTarget Customer customer);
}