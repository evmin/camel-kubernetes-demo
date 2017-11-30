package org.foo.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.google.bigquery.GoogleBigQueryConstants;
import org.springframework.stereotype.Component;

@Component
public class EventDemo extends RouteBuilder {

    private static final String ROUTE_ID = "org.foo.demo";

    @Override
    public void configure() throws Exception {

        from("google-pubsub:{{gcp_project_id}}:demo.event.bigquery")
            .routeId(ROUTE_ID)
            .to("jolt:transforms/demo_event.json?inputType=JsonString")
            .setHeader(GoogleBigQueryConstants.PARTITION_DECORATOR,simple("${date:now:yyyyMMdd}"))
            .log("Demo Event processed : ${body}")
            .to("google-bigquery:{{gcp_project_id}}:demo:demo_event")
        ;
    }
}
