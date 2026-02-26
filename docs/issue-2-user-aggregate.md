# Issue #2 – User Aggregate

## Overview

This issue implements the core User Aggregate following Clean Architecture and privacy-by-design principles.

Scope includes:

- Domain aggregate
- Value objects
- Persistence port
- JPA adapter
- Password hashing
- Unit tests

Authentication flows are intentionally excluded.

---

## Domain Model

Aggregate Root:

User:
- id
- email
- passwordHash
- createdAt

Value Objects:
- UserId
- Email
- PasswordHash

Domain layer contains no Spring or JPA annotations.

---

## Persistence

A port defines the persistence contract:

UserRepositoryPort

Infrastructure provides:
- JPA Entity
- Spring Data repository
- Adapter implementation

---

## Password Security

Password hashing handled through:

PasswordHasher interface

Implementation uses BCrypt (default).

No plain-text passwords are stored.

---

## Testing

Unit tests validate:
- Email format
- Password constraints
- Aggregate invariants

---

## Out of Scope

- JWT
- Login endpoints
- OAuth2
- Role management

These will be implemented in later issues.

---

## Completion Criteria

- User aggregate compiles without framework dependencies
- All invariants enforced
- Persistence adapter implemented
- Tests passing
