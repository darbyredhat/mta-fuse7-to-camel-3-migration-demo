package com.redhat.workshop.fuse.routers;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.redhat.workshop.fuse.model.Order;

@Component
public class RestRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        rest("/orders")
        .get("/").description("Get all orders")
            .route().routeId("all-orders")
            .log("Getting all order entries from database")
            .to(this.selectAll)
        .endRest()
    
        .get("/{id}").description("Get orders by id")
            .route().routeId("find-by-id")
            .log("Getting order with id ${header.id} entry from database")
            .to(this.selectById)
        .endRest()
            
        .post("/").type(Order.class).description("Create a new order")
            .route().routeId("create order")
            .log("Order received")
            .to(this.insertOrder)
        .endRest();
    }

    // Query support
    private String ds = "?dataSource=dataSource";
    private String selectAll = "sql:select * from orders" + ds;
    private String selectById = "sql:select * from orders where id = :#${header.id}"  + ds;
    private String insertOrder = "sql:insert into orders (item, amount, description, processed) values " +
    	                		"(:#${body.item}, :#${body.amount}, :#${body.description}, false)"+ ds;

}
