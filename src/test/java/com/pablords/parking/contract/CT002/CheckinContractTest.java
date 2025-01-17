package com.pablords.parking.contract.CT002;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/java/com/pablords/parking/resources/features",
    glue = {"com.pablords.parking.contract"},
    plugin = {"pretty", "json:target/cucumber-reports/contract/report-CT002.json", "html:target/cucumber-reports/contract/report-CT002.html"},
    monochrome = true
)
@ActiveProfiles("contract-test")
public class CheckinContractTest {

}
