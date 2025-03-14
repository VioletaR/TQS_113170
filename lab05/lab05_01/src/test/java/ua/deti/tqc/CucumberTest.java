package ua.deti.tqc;


import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("ua/deti/tqc")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "ua.deti.tqc")
public class CucumberTest {


}
