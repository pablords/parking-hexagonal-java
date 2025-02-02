package com.pablords.parking.component.CT001;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;



@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/car", glue = {
        "com.pablords.parking.component" }, plugin = { "pretty",
                "json:target/cucumber-reports/component/report-CT001.json",
                "html:target/cucumber-reports/component/report-CT001.html" }, monochrome = true)
public class CarComponentTest {

}
