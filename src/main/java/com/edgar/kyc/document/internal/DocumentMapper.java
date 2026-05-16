package com.edgar.kyc.document.internal;

import com.edgar.kyc.document.internal.response.DocumentResponse;
import org.mapstruct.Mapper;

/**
 * Mapper pour convertir les entités Document en DTOs
 */
@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentResponse toDocumentResponse(Document document);
}