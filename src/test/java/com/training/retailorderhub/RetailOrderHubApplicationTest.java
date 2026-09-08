package com.training.retailorderhub;

import org.junit.jupiter.api.Test;

class RetailOrderHubApplicationTest {

    @Test
    void mainStartsApplicationWithoutWebServer() {
        RetailOrderHubApplication.main(new String[] {
                "--spring.main.web-application-type=none",
                "--spring.main.banner-mode=off",
                "--spring.datasource.url=jdbc:h2:mem:applicationtest"
        });
    }
}
