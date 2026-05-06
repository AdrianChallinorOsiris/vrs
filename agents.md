# Agent Guidelines

## Lombok Usage

Use Lombok for boilerplate reduction:

- **Getters and setters**: Use `@Getter` and `@Setter` on entity and model classes instead of writing manual accessors.
- **Logging**: Use `@Slf4j` for logger fields instead of manual `Logger` declarations.

## Entity Classes

All JPA entity classes must include these two methods:

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EntityName other)) return false;
    return id != null && id.equals(other.id);
}

@Override
public int hashCode() {
    return getClass().hashCode();
}
```

- Replace `EntityName` with the actual entity class name.
- For entities with a **composite primary key** (e.g. `UserRole`), use the composite key fields in `equals` instead of `id`:
  ```java
  return userId != null && roleId != null && userId.equals(other.userId) && roleId.equals(other.roleId);
  ```
## Each collection of domain related classes is to be in its own package

For example, all class to do with the Company should reside in the 
company package. This includes entity, repository, controller, service 

## Known Issues

### SecurityConfigTest failures

All tests in `SecurityConfigTest` currently fail. The failure is due to ApplicationContext loading: `@WebMvcTest` loads `DataWebAutoConfiguration` which requires `entityManagerFactory`, but the test slice does not provide JPA/DataSource configuration.

When investigating, request the failure log from the user.

