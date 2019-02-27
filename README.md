## About this template

This is a template to get started with a Gauge project that uses Selenium as the driver to interact with a web browser.

## Installing this template

    gauge --install java_maven_selenium

## Building on top of this template

## to install gauge

brew install gauge
Java plugin : gauge --install java
html reports : gauge --install html-report

## IDE plugin - gauge

Search for gauge plugin, install it and restart intelliJ

Archetype : gauge-archetype-selenium

### Look into

* /env/default/browser.properties to configure setup related properties
* /resources/devices_specs.yaml

### Defining Specifications

* This template includes a sample specification which opens up a browser and navigates to `Get Started` page of Gauge.
* Add more specifications on top of sample specification.

Read more about [Specifications](http://getgauge.io/documentation/user/current/specifications/README.html)

### Writing the implementations

This is where the java implementation of the steps would be implemented. Since this is a Selenium based project, the java implementation would invoke Selenium APIs as required.

_We recommend considering modelling your tests using the [Page Object](https://github.com/SeleniumHQ/selenium/wiki/PageObjects) pattern, and the [Webdriver support](https://github.com/SeleniumHQ/selenium/wiki/PageFactory) for creating them._

- Note that every Gauge step implementation is annotated with a `Step` attribute that takes the Step text pattern as a parameter.
Read more about [Step implementations in Java](http://getgauge.io/documentation/user/current/test_code/java/java.html)

### Execution

* You can execute the specification as:

```
mvn test
```

or

```
mvn clean -U install gauge:execute
```

To run the specs parallel
```
mvn clean -U install gauge:execute -DinParallel=true -DspecsDir=specs -Dnodes=2
