package com.edgar.kyc.auth.internal;

import com.edgar.kyc.auth.internal.request.AuthLoginRequest;
import com.edgar.kyc.auth.internal.request.AuthRegisterRequest;

/**
 * Interface du service d'authentification
 */
public interface AuthService {
    void register(AuthRegisterRequest request);
    String login(AuthLoginRequest request);
}