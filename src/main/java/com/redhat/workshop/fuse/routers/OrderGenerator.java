package com.redhat.workshop.fuse.routers;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import com.redhat.workshop.fuse.service.OrderService;

@Component
public class OrderGenerator extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("timer:generate?repeatCount=3&period=1000")
            .log("Generating Order ...")
            .bean(OrderService.class, "generateOrder")
            .log("Order ${body.item} generated")
        .to("direct:book-to-file");

        from("direct:book-to-file")
        .choice()
            .when(simple("${body.item} == 'Camel'"))
                .log("Processing a camel book")
                .marshal().json()
                .to("file:/tmp/fuse-workshop/camel?fileName=camel-${date:now:yyyy-MM-dd-HHmmssSSS}.json")
            .otherwise()
                .log("Processing an activemq book")
                .process(new OrderProcessor())
                .marshal().jacksonxml()
                .to("file:/tmp/fuse-workshop/activemq?fileName=activemq-${date:now:yyyy-MM-dd-HHmmssSSS}.xml");
            
    }

}
