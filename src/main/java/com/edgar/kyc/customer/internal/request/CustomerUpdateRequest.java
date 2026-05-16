package com.edgar.kyc.customer.internal.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO pour la mise à jour du profil client
 */
@Data
public class CustomerUpdateRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String phone;
}