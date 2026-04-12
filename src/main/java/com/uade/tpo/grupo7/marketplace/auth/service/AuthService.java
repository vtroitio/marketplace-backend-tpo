package com.uade.tpo.grupo7.marketplace.auth.service;

import com.uade.tpo.grupo7.marketplace.auth.dto.AuthResponse;
import com.uade.tpo.grupo7.marketplace.auth.dto.LoginRequest;
import com.uade.tpo.grupo7.marketplace.auth.dto.RegisterRequest;

public interface AuthService {

    /**
     * Registers a new user with the provided registration details.
     *
     * @param dto the {@link RegisterRequest} containing user registration information
     * @return {@link AuthResponse} the authentication response for the newly registered user
     */
    AuthResponse register(RegisterRequest dto);

    /**
     * Authenticates a user with the provided login credentials.
     *
     * @param dto the {@link LoginRequest} containing user authentication details
     * @return {@link AuthResponse} the authentication response for the authenticated user
     */
    AuthResponse login(LoginRequest dto);

}
