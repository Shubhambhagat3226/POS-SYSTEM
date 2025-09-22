package com.shu.domain;

/**
 *
 * USER ROLE ENUM:
 * Defines the roles a user can have in the POS system.
 * Each role determines the permissions and access level of the user.
 *
 * Roles:
 * - ROLE_USER          : Regular user with standard access.
 * - ROLE_ADMIN         : System administrator with full access.
 * - ROLE_CASHIER       : Can process sales and manage transactions.
 * - ROLE_BRANCH_MANAGER: Manages branch-level operations.
 * - ROLE_STORE_MANAGER : Manages overall store operations and inventory.
 */
public enum UserRole {

    ROLE_USER,
    ROLE_ADMIN,
    ROLE_CASHIER,
    ROLE_BRANCH_MANAGER,
    ROLE_STORE_MANAGER
}
