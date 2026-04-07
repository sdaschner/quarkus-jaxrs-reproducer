# RESTEasy Path Conflict Reproducer

*README written by AI, but checked for correctness*

Demonstrates a routing bug in Quarkus 3.29.0 with RESTEasy (`quarkus-resteasy-jsonb`) where two root resource classes with overlapping `@Path` annotations cause 404s.

## The bug

Two JAX-RS resources:

- `ItemsResource` with `@Path("items")` — has sub-resource methods `@Path("{id}")`, `@Path("{id}/details")`
- `ItemLogsResource` with `@Path("items/{id}")` — has sub-resource method `@Path("logs")`

Per the JAX-RS spec (Section 3.7.2), the matching algorithm should accumulate candidate methods
from **all** matching root resources. In practice, RESTEasy selects `ItemLogsResource`
(more specific class-level `@Path`) and only looks for methods within that class:

| Endpoint              | Expected | Actual  | Why                                            |
|-----------------------|----------|---------|------------------------------------------------|
| `GET /items`          | 200      | 200     | Only `ItemsResource` matches                   |
| `GET /items/1`        | 200      | **404** | `ItemLogsResource` selected, no root `@GET`    |
| `GET /items/1/details`| 200      | **404** | `ItemLogsResource` selected, no `details` path |
| `GET /items/1/logs`   | 200      | 200     | `ItemLogsResource` handles it                  |

## Reproduce

Build and run:

```bash
mvn clean package
java -jar target/quarkus-app/quarkus-run.jar
```

In another terminal:

```bash
# Works — list items (only ItemsResource matches)
curl -v http://localhost:8080/items

# Works — logs handled by ItemLogsResource
curl -v http://localhost:8080/items/1/logs

# 404 — should return "Alpha" but ItemLogsResource claims the prefix
curl -v http://localhost:8080/items/1

# 404 — should return "Details for: Alpha" but ItemLogsResource claims the prefix
curl -v http://localhost:8080/items/1/details
```
