package com.uade.tpo.grupo7.marketplace.users.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.tpo.grupo7.marketplace.users.dto.CreateAdminRequest;
import com.uade.tpo.grupo7.marketplace.users.dto.UpdateUserRequest;
import com.uade.tpo.grupo7.marketplace.users.dto.UserResponse;

public interface UserService {
    
    public Page<UserResponse> getAllUsers(Pageable pageable);
    
    public UserResponse getUserById(Long userId);

    public UserResponse updateUserById(Long userId, UpdateUserRequest dto);

    public void deleteUserById(Long userId);

    public UserResponse createAdminUser(CreateAdminRequest dto);

    public UserResponse promoteUserToSeller(Long userId);

    public void activateUserById(Long userId);

    public void deactivateUserById(Long userId);

}
