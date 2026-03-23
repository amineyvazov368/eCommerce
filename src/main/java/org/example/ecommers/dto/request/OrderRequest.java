package org.example.ecommers.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record OrderRequest(
        @NotBlank
        List<OrderItemRequest> list
) {
}
