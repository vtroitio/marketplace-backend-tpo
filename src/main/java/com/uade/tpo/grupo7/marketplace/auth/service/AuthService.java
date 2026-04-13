package com.uade.tpo.grupo7.marketplace.auth.service;

import java.util.Optional;

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

    /**
     * Refreshes the authentication tokens using the provided refresh token.
     *
     * @param refreshToken the refresh token used to obtain new authentication tokens
     * @return an {@code Optional} containing the new {@link AuthTokens} if the refresh was successful,
     *         or an empty {@code Optional} if the refresh token is invalid or expired
     */
    Optional<AuthTokens> refresh(String refreshToken);

    /**
     * Logs out a user by invalidating their refresh token.
     * 
     * This method terminates the user session by invalidating the provided refresh token,
     * preventing further authentication requests using that token.
     * 
     * @param refreshToken the refresh token to be invalidated for logout
     */
    void logout(String refreshToken);

}
