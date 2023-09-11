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
            .to(this.selectAll)
    
        .get("/{id}").description("Get orders by id")
            .to(this.selectById)
            
        .post("/").type(Order.class).description("Create a new order")
            .to(this.insertOrder);
    }

    // Query support
    private String ds = "?dataSource=dataSource";
    private String selectAll = "sql:select * from orders" + ds;
    private String selectById = "sql:select * from orders where id = :#${header.id}"  + ds;
    private String insertOrder = "sql:insert into orders (item, amount, description, processed) values " +
    	                		"(:#${body.item}, :#${body.amount}, :#${body.description}, false)"+ ds;

}
