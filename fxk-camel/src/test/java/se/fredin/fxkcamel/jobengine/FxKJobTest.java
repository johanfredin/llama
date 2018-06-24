package se.fredin.fxkcamel.jobengine;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import se.fredin.fxkcamel.externallinks.JobRoute;
import se.fredin.fxkcamel.jobengine.mock.EndpointToMockEndpoint;
import se.fredin.fxkcamel.jobengine.mock.MockRouteDefinition;
import se.fredin.fxkcamel.jobengine.utils.SettingsComponent;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public abstract class FxKJobTest extends CamelTestSupport {

    @Autowired
    protected SettingsComponent settingsComponent;

    @Override
    protected RoutesBuilder createRouteBuilder() {
        return new JobRoute();
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Before
    public void mockEndpoints() throws Exception {
        for (MockRouteDefinition mockRouteDefinition : getMockRoutes()) {
            AdviceWithRouteBuilder mockEndpoint = new AdviceWithRouteBuilder() {
                @Override
                public void configure() {
                    for (EndpointToMockEndpoint etme : mockRouteDefinition.getEnpointsTomock()) {
                        // Mock for testing
                        interceptSendToEndpoint(etme.getRealEndpoint()).skipSendToOriginalEndpoint().to(etme.getMockedEnpoint());
                    }
                }
            };
            context.getRouteDefinition(mockRouteDefinition.getRouteId()).adviceWith(context, mockEndpoint);
        }
    }

    public SettingsComponent getSettingsComponent() {
        return settingsComponent;
    }

    public void setSettingsComponent(SettingsComponent settingsComponent) {
        this.settingsComponent = settingsComponent;
    }

    protected abstract List<MockRouteDefinition> getMockRoutes();
}
