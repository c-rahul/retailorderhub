package com.training.retailorderhub;

import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

class RetailOrderHubApplicationTest {

    @Test
    void mainDelegatesToSpringApplication() {
        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            RetailOrderHubApplication.main(new String[] {"--test"});
            springApplication.verify(() -> SpringApplication.run(RetailOrderHubApplication.class,
                    new String[] {"--test"}));
        }
    }
}
