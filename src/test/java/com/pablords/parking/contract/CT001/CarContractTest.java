package com.pablords.parking.contract.CT001;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;


@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/java/com/pablords/parking/resources/features",
    glue = {"com.pablords.parking.contract"},
    plugin = {"pretty", "json:target/cucumber-reports/contract/report-CT001.json", "html:target/cucumber-reports/contract/report-CT001.html"},
    monochrome = true
)
@ActiveProfiles("contract-test")
public class CarContractTest {
   
}
