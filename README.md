# RetailOrderHub

Monolithic full-stack Java application built for the 5-day System Design training
program. This is a deliberately simple, single-deployable Spring Boot app used
for system design training and SonarCloud demonstrations.

## Stack

- **Java 25**
- **Spring Boot 3.5** (Spring MVC + Thymeleaf server-rendered views — a true
  monolith, one JAR serves both UI and backend)
- **Spring Data JPA + H2** (in-memory, zero setup — resets on every restart)
- **Maven**

## Prerequisites

- JDK 25 or later
- Maven 3.9+ (or use your IDE's built-in Maven support)

## Running it

```bash
mvn spring-boot:run
```

Or build a JAR and run it directly:

```bash
mvn clean package
java -jar target/retailorderhub.jar
```

Then open:

- **http://localhost:8080/** — product catalog + order form
- **http://localhost:8080/orders** — list of placed orders
- **http://localhost:8080/h2-console** — H2 database console (JDBC URL:
  `jdbc:h2:mem:retailorderhub`, user `sa`, no password) — for poking at the data
  directly during training; note this is enabled here for teaching purposes only
  and should never be left on in a real deployment

## Placing a test order

On the home page, use one of the seeded product names exactly as shown in the
catalog (e.g. `Laptop, Mouse`), any customer ID, and any payment method. A
successful order will appear on the `/orders` page.

## Service design

Order processing follows the SOLID principles through focused collaborators:

- `OrderService` orchestrates the use case.
- `OrderValidator` handles customer and item validation.
- `InventoryService` handles stock checks and updates with parameterized queries.
- `OrderCreator` builds and persists orders.
- `PaymentStrategy` implementations isolate credit-card, PayPal, and gift-card behavior.
- `PaymentStrategyFactory` selects the payment strategy without changing the order flow.

## Running the SonarCloud scan against this project

```bash
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=<your-project-key> \
  -Dsonar.organization=<your-org> \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.login=$SONAR_TOKEN
```

Run `mvn clean verify` first to execute the tests and generate JaCoCo reports under
`target/site/jacoco/`.

## Project structure

```
retailorderhub/
├── pom.xml
├── README.md
└── src/main/
    ├── java/com/training/retailorderhub/
    │   ├── RetailOrderHubApplication.java
    │   ├── controller/OrderController.java
    │   ├── model/Product.java
    │   ├── model/Order.java
    │   ├── repository/ProductRepository.java
    │   ├── repository/OrderRepository.java
    │   └── service/
    │       ├── OrderService.java
    │       ├── OrderValidator.java
    │       ├── InventoryService.java
    │       ├── OrderCreator.java
    │       └── payment/
    │           ├── PaymentStrategy.java
    │           ├── PaymentStrategyFactory.java
    │           ├── CreditCardPaymentStrategy.java
    │           ├── PaypalPaymentStrategy.java
    │           └── GiftCardPaymentStrategy.java
    └── resources/
        ├── application.properties
        ├── data.sql
        ├── static/css/style.css
        └── templates/
            ├── index.html
            └── orders.html
```
