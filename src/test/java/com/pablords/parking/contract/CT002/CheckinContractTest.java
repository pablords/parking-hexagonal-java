package com.pablords.parking.contract.CT002;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/java/com/pablords/parking/contract/CT002/features",
    glue = {"com.pablords.parking.contract"},
    plugin = {"pretty", "json:target/cucumber-reports/report-CT002.json", "html:target/cucumber-reports/report-CT002.html"},
    monochrome = true
)
public class CheckinContractTest {

}
