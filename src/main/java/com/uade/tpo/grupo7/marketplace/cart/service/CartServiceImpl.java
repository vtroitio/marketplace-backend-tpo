package com.uade.tpo.grupo7.marketplace.cart.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.cart.dto.AddToCartRequest;
import com.uade.tpo.grupo7.marketplace.cart.dto.CartItemResponse;
import com.uade.tpo.grupo7.marketplace.cart.dto.CartResponse;
import com.uade.tpo.grupo7.marketplace.cart.dto.CartValidationIssueResponse;
import com.uade.tpo.grupo7.marketplace.cart.dto.CartValidationResponse;
import com.uade.tpo.grupo7.marketplace.cart.dto.SyncCartItemResultResponse;
import com.uade.tpo.grupo7.marketplace.cart.dto.SyncCartRequest;
import com.uade.tpo.grupo7.marketplace.cart.dto.SyncCartResponse;
import com.uade.tpo.grupo7.marketplace.cart.entity.Cart;
import com.uade.tpo.grupo7.marketplace.cart.entity.CartItem;
import com.uade.tpo.grupo7.marketplace.cart.repository.CartRepository;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.entity.ProductVariant;
import com.uade.tpo.grupo7.marketplace.products.repository.ProductVariantRepository;
import com.uade.tpo.grupo7.marketplace.users.entity.User;
import com.uade.tpo.grupo7.marketplace.users.repository.UserRepository;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductVariantRepository productVariantRepository;
    private final UserRepository userRepository;

    public CartServiceImpl(CartRepository cartRepository,
            ProductVariantRepository productVariantRepository,
            UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productVariantRepository = productVariantRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public CartResponse getCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return mapToResponse(cart);
    }

    @Transactional
    @Override
    public CartResponse addItemToCart(Long userId, AddToCartRequest request) {
        Cart cart = getOrCreateCart(userId);

        ProductVariant variant = productVariantRepository.findById(request.productVariantId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "La variante ya no existe."));

        Product product = variant.getProduct();

        validateProductCanBeAdded(userId, product);

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductVariant().getId().equals(variant.getId()))
                .findFirst();

        int currentQuantity = existingItem
                .map(CartItem::getQuantity)
                .orElse(0);

        int requestedFinalQuantity = currentQuantity + request.quantity();

        validateStock(variant, requestedFinalQuantity);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(requestedFinalQuantity);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .productVariant(variant)
                    .quantity(request.quantity())
                    .build();

            cart.getItems().add(newItem);
        }

        recalculateTotal(cart);

        return mapToResponse(cartRepository.save(cart));
    }

    @Transactional
    @Override
    public CartResponse updateItemQuantity(Long userId, Long itemId, int quantity) {
        Cart cart = requireCart(userId);
        CartItem item = findItemOrThrow(cart, itemId);

        ProductVariant variant = item.getProductVariant();
        Product product = variant.getProduct();

        validateProductCanBeAdded(userId, product);
        validateStock(variant, quantity);

        item.setQuantity(quantity);

        recalculateTotal(cart);

        return mapToResponse(cartRepository.save(cart));
    }

    private void validateProductCanBeAdded(Long userId, Product product) {
        if (isOwnProduct(userId, product)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No podés agregar al carrito una publicación propia.");
        }

        if (!Boolean.TRUE.equals(product.isActive())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El producto ya no está disponible.");
        }
    }

    private void validateStock(ProductVariant variant, int requestedQuantity) {
        if (requestedQuantity <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La cantidad debe ser mayor a cero.");
        }

        if (variant.getStock() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No hay stock disponible.");
        }

        if (requestedQuantity > variant.getStock()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No hay más stock disponible para este producto.");
        }
    }

    @Transactional
    @Override
    public CartResponse removeItem(Long userId, Long itemId) {
        Cart cart = requireCart(userId);
        CartItem item = findItemOrThrow(cart, itemId);
        cart.getItems().remove(item);
        recalculateTotal(cart);
        return mapToResponse(cartRepository.save(cart));
    }

    @Transactional
    @Override
    public void clearCart(Long userId) {
        Cart cart = requireCart(userId);
        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByBuyerId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
                    return cartRepository.save(Cart.builder()
                            .buyer(user)
                            .totalAmount(BigDecimal.ZERO)
                            .build());
                });
    }

    private Cart requireCart(Long userId) {
        return cartRepository.findByBuyerId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));
    }

    private CartItem findItemOrThrow(Cart cart, Long itemId) {
        return cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found"));
    }

    private void recalculateTotal(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(item -> BigDecimal.valueOf(item.getProductVariant().getPrice())
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(total);
    }

    private CartResponse mapToResponse(Cart cart) {
        return new CartResponse(
                cart.getId(),
                cart.getTotalAmount(),
                cart.getItems().stream().map(this::mapItem).collect(Collectors.toList()));
    }

    private CartItemResponse mapItem(CartItem item) {
        BigDecimal unitPrice = BigDecimal.valueOf(item.getProductVariant().getPrice());
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
        return new CartItemResponse(
                item.getId(),
                item.getProductVariant().getId(),
                item.getProductVariant().getProduct().getName(),
                item.getQuantity(),
                unitPrice,
                subtotal);
    }

    @Transactional
    @Override
    public SyncCartResponse syncCart(Long userId, SyncCartRequest request) {
        Cart cart = getOrCreateCart(userId);

        List<SyncCartItemResultResponse> results = new ArrayList<>();

        results.addAll(removeOwnProductsFromCart(cart, userId));

        Map<Integer, Integer> requestedItems = request.items()
                .stream()
                .collect(Collectors.toMap(
                        item -> item.variantId(),
                        item -> item.quantity(),
                        Integer::sum));

        for (Map.Entry<Integer, Integer> entry : requestedItems.entrySet()) {
            Integer productVariantId = entry.getKey();
            Integer requestedQuantity = entry.getValue();

            ProductVariant variant = productVariantRepository.findById(productVariantId)
                    .orElse(null);

            if (variant == null) {
                results.add(new SyncCartItemResultResponse(
                        productVariantId,
                        requestedQuantity,
                        0,
                        "NOT_FOUND",
                        "La variante ya no existe."));
                continue;
            }

            Product product = variant.getProduct();

            if (isOwnProduct(userId, product)) {
                results.add(new SyncCartItemResultResponse(
                        productVariantId,
                        requestedQuantity,
                        0,
                        "OWN_PRODUCT",
                        "No podés agregar al carrito una publicación propia."));
                continue;
            }

            if (!Boolean.TRUE.equals(product.isActive())) {
                results.add(new SyncCartItemResultResponse(
                        productVariantId,
                        requestedQuantity,
                        0,
                        "PRODUCT_INACTIVE",
                        "El producto ya no está disponible."));
                continue;
            }

            if (variant.getStock() <= 0) {
                results.add(new SyncCartItemResultResponse(
                        productVariantId,
                        requestedQuantity,
                        0,
                        "NO_STOCK",
                        "No hay stock disponible."));
                continue;
            }

            CartItem existingItem = findItemByProductVariant(cart, productVariantId);

            int currentQuantity = existingItem != null ? existingItem.getQuantity() : 0;
            int availableToAdd = variant.getStock() - currentQuantity;

            if (availableToAdd <= 0) {
                results.add(new SyncCartItemResultResponse(
                        productVariantId,
                        requestedQuantity,
                        0,
                        "STOCK_LIMIT",
                        "Ya alcanzaste el máximo disponible para este producto."));
                continue;
            }

            int quantityToAdd = Math.min(requestedQuantity, availableToAdd);

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + quantityToAdd);
            } else {
                CartItem newItem = CartItem.builder()
                        .cart(cart)
                        .productVariant(variant)
                        .quantity(quantityToAdd)
                        .build();

                cart.getItems().add(newItem);
            }

            String status = quantityToAdd == requestedQuantity
                    ? "ADDED"
                    : "STOCK_ADJUSTED";

            String message = quantityToAdd == requestedQuantity
                    ? "Producto agregado al carrito."
                    : "Se agregó una cantidad menor por falta de stock.";

            results.add(new SyncCartItemResultResponse(
                    productVariantId,
                    requestedQuantity,
                    quantityToAdd,
                    status,
                    message));
        }

        recalculateTotal(cart);

        Cart savedCart = cartRepository.save(cart);

        return new SyncCartResponse(
                mapToResponse(savedCart),
                results);
    }

    @Transactional
    @Override
    public CartValidationResponse validateCartForCheckout(Long userId) {
        Cart cart = getOrCreateCart(userId);

        List<CartValidationIssueResponse> issues = new ArrayList<>();
        List<CartItem> itemsToRemove = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            ProductVariant variant = item.getProductVariant();
            Product product = variant.getProduct();
            Integer productVariantId = variant.getId();

            if (isOwnProduct(userId, product)) {
                itemsToRemove.add(item);

                issues.add(new CartValidationIssueResponse(
                        productVariantId,
                        "OWN_PRODUCT_REMOVED",
                        "Se eliminó un producto porque no podés comprar tu propia publicación."));

                continue;
            }

            if (!Boolean.TRUE.equals(product.isActive())) {
                itemsToRemove.add(item);

                issues.add(new CartValidationIssueResponse(
                        productVariantId,
                        "PRODUCT_INACTIVE_REMOVED",
                        "Se eliminó un producto porque ya no está disponible."));

                continue;
            }

            if (variant.getStock() <= 0) {
                itemsToRemove.add(item);

                issues.add(new CartValidationIssueResponse(
                        productVariantId,
                        "NO_STOCK_REMOVED",
                        "Se eliminó un producto porque no tiene stock disponible."));

                continue;
            }

            if (item.getQuantity() > variant.getStock()) {
                item.setQuantity(variant.getStock());

                issues.add(new CartValidationIssueResponse(
                        productVariantId,
                        "QUANTITY_ADJUSTED",
                        "Se ajustó la cantidad de un producto por stock insuficiente."));
            }
        }

        cart.getItems().removeAll(itemsToRemove);

        recalculateTotal(cart);

        Cart savedCart = cartRepository.save(cart);

        boolean valid = issues.isEmpty() && !savedCart.getItems().isEmpty();

        return new CartValidationResponse(
                valid,
                mapToResponse(savedCart),
                issues);
    }

    private CartItem findItemByProductVariant(Cart cart, Integer variantId) {
        return cart.getItems()
                .stream()
                .filter(item -> item.getProductVariant().getId().equals(variantId))
                .findFirst()
                .orElse(null);
    }

    private boolean isOwnProduct(Long userId, Product product) {
        return product.getSeller()
                .getId()
                .equals(userId);
    }

    private List<SyncCartItemResultResponse> removeOwnProductsFromCart(Cart cart, Long userId) {
        List<CartItem> ownItems = cart.getItems()
                .stream()
                .filter(item -> isOwnProduct(userId, item.getProductVariant().getProduct()))
                .toList();

        List<SyncCartItemResultResponse> results = ownItems
                .stream()
                .map(item -> new SyncCartItemResultResponse(
                        item.getProductVariant().getId(),
                        item.getQuantity(),
                        0,
                        "OWN_PRODUCT_REMOVED",
                        "Se eliminó del carrito porque no podés comprar tu propia publicación."))
                .toList();

        cart.getItems().removeAll(ownItems);

        return results;
    }
}