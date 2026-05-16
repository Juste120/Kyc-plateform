package com.edgar.kyc.customer;

import com.edgar.kyc.customer.internal.CustomerService;
import com.edgar.kyc.customer.internal.request.CustomerUpdateRequest;
import com.edgar.kyc.customer.internal.response.CustomerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

/**
 * Contrôleur public pour la gestion du profil client
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Récupérer le profil du client connecté
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CustomerResponse> getMyProfile(Principal principal) {
        CustomerResponse customer = customerService.getCustomerByEmail(principal.getName());
        return ResponseEntity.ok(customer);
    }

    /**
     * Mettre à jour le profil du client connecté
     */
    @PutMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CustomerResponse> updateMyProfile(
            Principal principal,
            @Valid @RequestBody CustomerUpdateRequest request) {
        CustomerResponse updatedCustomer = customerService.updateCustomer(principal.getName(), request);
        return ResponseEntity.ok(updatedCustomer);
    }
}