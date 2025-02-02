package com.pablords.parking.integration.CT002;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features/checkin",
    glue = {"com.pablords.parking.integration"},
    plugin = {"pretty", "json:target/cucumber-reports/integration/report-CT002.json", "html:target/cucumber-reports/integration/report-CT002.html"},
    monochrome = true
)
@ActiveProfiles("integration-test")
public class CheckinIntegrationTest {

}
