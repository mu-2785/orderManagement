# Architecture

The application is intentionally a modular monolith.

Current logical modules:
- customer
- product
- inventory
- order
- payment
- shipping
- notification

The application is one deployment unit and currently uses one MySQL database.

Layering:
- api: HTTP/controllers/request-response concerns
- application: use-case orchestration
- domain: business rules/domain model
- infrastructure: persistence and external integrations

Rules:
1. Controllers do not contain business logic.
2. Entities are not returned directly from controllers.
3. Flyway owns schema changes.
4. Hibernate uses `ddl-auto=validate`.
5. Business changes require automated tests.
6. Production secrets come from environment/configuration, never source control.

Future architecture may extract selected modules into microservices.
