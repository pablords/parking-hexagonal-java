package com.pablords.parking.integration.CT003;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features/checkout",
    glue = {"com.pablords.parking.integration"},
    plugin = {"pretty", "json:target/cucumber-reports/integration/report-CT003.json", "html:target/cucumber-reports/integration/report-CT003.html"},
    monochrome = true
)
@ActiveProfiles("integration-test")
public class CheckoutIntegrationTest {

}
