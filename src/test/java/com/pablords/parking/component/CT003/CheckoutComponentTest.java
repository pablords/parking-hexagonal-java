package com.pablords.parking.component.CT003;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features/checkout",
    glue = {"com.pablords.parking.component"},
    plugin = {"pretty", "json:target/cucumber-reports/component/report-CT003.json", "html:target/cucumber-reports/component/report-CT003.html"},
    monochrome = true
)
public class CheckoutComponentTest {

}
