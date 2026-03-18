package org.example.ecommers.dto.request;

import jakarta.validation.constraints.NotNull;
import org.example.ecommers.entity.Role;

public class UserRoleRequest {
    @NotNull
    private Role role;

    // getter və setter
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
