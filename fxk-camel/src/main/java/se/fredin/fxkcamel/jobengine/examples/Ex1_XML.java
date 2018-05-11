package se.fredin.fxkcamel.jobengine.examples;

import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.model.dataformat.JacksonXMLDataFormat;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.springframework.stereotype.Component;
import se.fredin.fxkcamel.jaxb.Users;
import se.fredin.fxkcamel.jobengine.JobUtils;
import se.fredin.fxkcamel.jobengine.JobengineJob;

import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Doing ex.1 where input/output is xml
 */
@Component
public class Ex1_XML extends JobengineJob {

    @Override
    public void configure() throws Exception {


        from(JobUtils.file(getSettingsComponent().getInputDirectory(), "foo.xml"))
        .convertBodyTo(Users.class)
        .process(e -> processUsers(e))
        .marshal().jaxb()
        .to(JobUtils.file(getSettingsComponent().getOutputDirectory(), "foo_fixed.xml"))
        .end();
    }

    private void processUsers(Exchange exchange) throws InvalidPayloadException {
        Users users = exchange.getIn().getBody(Users.class);
        users.getUser()
                .stream()                                                       // Iterate users
                .filter(user -> user.getAge() > 0 && user.getAge() < 100)       // Filter out invalid age
                .sorted(Comparator.comparing(user -> user.getCountry()))        // Sort on country
                .peek(user -> user.setGender(user.getGender().toUpperCase()))   // Set gender to be uppercase
                .collect(Collectors.toList());                                  // Collect the update

        // Update the body
        exchange.getIn().setBody(users);
    }

}
