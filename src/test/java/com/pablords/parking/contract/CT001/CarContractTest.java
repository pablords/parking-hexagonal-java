package com.pablords.parking.contract.CT001;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/java/com/pablords/parking/contract/CT001/features",
    glue = {"com.pablords.parking.contract"},
    plugin = {"pretty", "json:target/cucumber-report-CT001.json", "html:target/cucumber-html-report-CT001.html"},
    monochrome = true
)
public class CarContractTest {
   
}
