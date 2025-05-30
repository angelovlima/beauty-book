package br.com.beauty_book.customer_management.bdd;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("br/com/beauty_book/customer_management/bdd")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "br.com.beauty_book.customer_management.bdd")
public class CucumberTest {
}
