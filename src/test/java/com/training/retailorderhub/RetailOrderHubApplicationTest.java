package com.training.retailorderhub;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest(properties = {
        "spring.main.web-application-type=none",
        "spring.main.banner-mode=off",
        "spring.datasource.url=jdbc:h2:mem:applicationtest"
})
class RetailOrderHubApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void applicationContextStartsWithCoreServices() {
        assertNotNull(applicationContext);
        assertTrue(applicationContext.containsBean("orderService"));
        assertTrue(applicationContext.containsBean("productCatalogService"));
        assertTrue(applicationContext.containsBean("orderQueryService"));
    }

    @Test
    void mainDelegatesToSpringApplication() {
        String[] arguments = {"--test"};
        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            assertDoesNotThrow(() -> RetailOrderHubApplication.main(arguments));
            springApplication.verify(() -> SpringApplication.run(RetailOrderHubApplication.class, arguments));
        }
    }
}
