package com.uade.tpo.grupo7.marketplace.users.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.common.enums.RoleCode;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.repository.ProductRepository;
import com.uade.tpo.grupo7.marketplace.users.dto.CreateAdminRequest;
import com.uade.tpo.grupo7.marketplace.users.dto.UpdateUserRequest;
import com.uade.tpo.grupo7.marketplace.users.dto.UserResponse;
import com.uade.tpo.grupo7.marketplace.users.entity.Role;
import com.uade.tpo.grupo7.marketplace.users.entity.User;
import com.uade.tpo.grupo7.marketplace.users.mapper.UserMapper;
import com.uade.tpo.grupo7.marketplace.users.repository.RoleRepository;
import com.uade.tpo.grupo7.marketplace.users.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> users = this.userRepository.findAll(pageable);
        return users.map(UserMapper::toResponse);
    }

    @Override
    public UserResponse getUserById(Long userId) {
        return this.userRepository.findById(userId)
            .map(UserMapper::toResponse)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
            ));
    }

    @Override
    public UserResponse updateUserById(
        Long userId,
        UpdateUserRequest dto
    ) {
        User user = this.userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
            ));

        if (dto.username() != null) {
            this.userRepository.findByUsername(dto.username())
                .filter(existingUser -> !existingUser.getId().equals(userId))
                .ifPresent(existingUser -> {
                    throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Username already taken"
                    );
            });
            user.setUsername(dto.username());
        }

        if (dto.name() != null) {
            user.setName(dto.name());
        }

        if (dto.surname() != null) {
            user.setSurname(dto.surname());
        }

        if (dto.email() != null) {
            this.userRepository.findByEmail(dto.email())
                .filter(existingUser -> !existingUser.getId().equals(userId))
                .ifPresent(existingUser -> {
                    throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Email already taken"
                    );
            });
            user.setEmail(dto.email());
        }

        return UserMapper.toResponse(this.userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
            ));

        List<Product> products = productRepository.findBySeller(user)
            .orElse(List.of());

        for (Product p : products) {
            p.softDelete();
            p.setSeller(null);
        }

        userRepository.delete(user);
    }

    @Override
    public UserResponse createAdminUser(CreateAdminRequest dto) {
        if (this.userRepository.existsByUsername(dto.username())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Username already taken"
            );
        }

        if (this.userRepository.existsByEmail(dto.email())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Email already taken"
            );
        }

        String passwordHash = this.passwordEncoder.encode(dto.password());
        Role adminRole = this.roleRepository.findByCode(RoleCode.ADMIN)
            .orElseThrow(() -> new RuntimeException("Admin role not found"));

        User user = User.builder()
            .username(dto.username())
            .name(dto.name())
            .surname(dto.surname())
            .email(dto.email())
            .passwordHash(passwordHash)
            .role(adminRole)
            .build();

        return UserMapper.toResponse(this.userRepository.save(user));
    }

    @Override
    public UserResponse promoteUserToSeller(Long userId) {
        User user = this.userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
            ));

        Role sellerRole = this.roleRepository.findByCode(RoleCode.SELLER)
            .orElseThrow(() -> new RuntimeException("Seller role not found"));

        if (user.getRole() == sellerRole) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "User is already a seller"
            );
        }

        user.setRole(sellerRole);
        return UserMapper.toResponse(this.userRepository.save(user));
    }

    @Override
    public void activateUserById(Long userId) {
        User user = this.userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
            ));
        user.setDeletedAt(null);
        this.userRepository.save(user);
    }

    @Override
    public void deactivateUserById(Long userId) {
        User user = this.userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
            ));
        user.softDelete();
        this.userRepository.save(user);
    }

}
