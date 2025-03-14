package ua.deti.tqs.salary;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("ua/deti/tqs/salary")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "ua.deti.tqs.salary")
public class SalaryTest {


}
