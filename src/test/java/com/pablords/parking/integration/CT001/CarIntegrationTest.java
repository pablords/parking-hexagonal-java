package com.pablords.parking.integration.CT001;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;


@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/java/com/pablords/parking/resources/features",
    glue = {"com.pablords.parking.integration"},
    plugin = {"pretty", "json:target/cucumber-reports/integration/report-CT001.json", "html:target/cucumber-reports/integration/report-CT001.html"},
    monochrome = true
)
@ActiveProfiles("integration-test")
public class CarIntegrationTest {
   
}
