import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(tags ="not @Networked_sp",  features="src/main/test/resources")
public class CucumberTest {
}
