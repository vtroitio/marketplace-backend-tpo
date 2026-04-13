package com.uade.tpo.grupo7.marketplace.auth.service;

import com.uade.tpo.grupo7.marketplace.auth.domain.AuthTokens;
import com.uade.tpo.grupo7.marketplace.auth.dto.LoginRequest;
import com.uade.tpo.grupo7.marketplace.auth.dto.RegisterRequest;

public interface AuthService {

    /**
     * Registers a new user with the provided registration details.
     *
     * @param dto the {@link RegisterRequest} containing user registration information
     * @return {@link AuthTokens} the authentication tokens for the newly registered user
     */
    AuthTokens register(RegisterRequest dto);

    /**
     * Authenticates a user with the provided login credentials.
     *
     * @param dto the {@link LoginRequest} containing user authentication details
     * @return {@link AuthTokens} the authentication tokens for the authenticated user
     */
    AuthTokens login(LoginRequest dto);

}
