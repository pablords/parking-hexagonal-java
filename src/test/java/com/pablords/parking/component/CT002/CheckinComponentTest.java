package com.pablords.parking.component.CT002;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/java/com/pablords/parking/resources/features/checkin",
    glue = {"com.pablords.parking.component"},
    plugin = {"pretty", "json:target/cucumber-reports/component/report-CT002.json", "html:target/cucumber-reports/component/report-CT002.html"},
    monochrome = true
)
public class CheckinComponentTest {

}
