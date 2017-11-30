package org.foo.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class Timer extends RouteBuilder {

    private static final String ROUTE_ID = "org.foo.timer";

    @Override
    public void configure() throws Exception {

        from("timer://foo?fixedRate=true&period={{timer.default.period.msec}}")
            .routeId(ROUTE_ID)
            .setBody(simple("Timer triggered: ${date:now:yyyyMMdd-HHmmss} "))
            .to("log:"+ROUTE_ID+"?level=INFO&showBody=true")
        ;
    }
}
