package com.edgar.kyc.verification.internal;

import com.edgar.kyc.verification.internal.response.VerificationResponse;
import org.mapstruct.Mapper;

/**
 * Mapper pour convertir les entités Verification en DTOs
 */
@Mapper(componentModel = "spring")
public interface VerificationMapper {

    VerificationResponse toVerificationResponse(Verification verification);
}