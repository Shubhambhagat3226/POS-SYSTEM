package com.shu.domain;

/**
 * Enum representing the operational status of a store in the system.
 *
 * <ul>
 *     <li>{@link #ACTIVE} - Store is approved and operational.</li>
 *     <li>{@link #PENDING} - Store is awaiting approval or activation.</li>
 *     <li>{@link #BLOCKED} - Store is blocked and cannot operate.</li>
 * </ul>
 *
 * Usage:
 * Used inside the Store entity to manage store lifecycle state.
 */
public enum StoreStatus {

    ACTIVE,
    PENDING,
    BLOCKED
}
