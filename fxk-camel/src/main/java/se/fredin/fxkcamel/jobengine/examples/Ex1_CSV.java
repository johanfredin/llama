package se.fredin.fxkcamel.jobengine.examples;

import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.springframework.stereotype.Component;
import se.fredin.fxkcamel.jobengine.JobUtils;
import se.fredin.fxkcamel.jobengine.JobengineJob;
import se.fredin.fxkcamel.jobengine.bean.CsvUser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Doing ex.1 where input/output is csv
 */
@Component
public class Ex1_CSV extends JobengineJob {

    @Override
    public void configure() throws Exception {

        BindyCsvDataFormat bindyCsvDataFormat = new BindyCsvDataFormat(CsvUser.class);

        from(JobUtils.file(getSettingsComponent().getInputDirectory(), "foo.csv"))
                .unmarshal(bindyCsvDataFormat)
                .process(e -> processUsers(e))
                .marshal(bindyCsvDataFormat)
                .to(JobUtils.file(getSettingsComponent().getOutputDirectory(), "foo_fixed.csv"))
                .end();

    }

    private void processUsers(Exchange exchange) throws InvalidPayloadException {
        List<CsvUser> users = JobUtils.<CsvUser>asList(exchange)
                .stream()                                                       // Iterate users
                .filter(user -> user.getAge() > 0 && user.getAge() < 100)       // Filter out invalid age
                .sorted(Comparator.comparing(user -> user.getCountry()))        // Sort on country
                .peek(user -> user.setGender(user.getGender().toUpperCase()))   // Set gender to be uppercase
                .collect(Collectors.toList());                                  // Collect the update

        // Update the body
        exchange.getIn().setBody(users);
    }


}
