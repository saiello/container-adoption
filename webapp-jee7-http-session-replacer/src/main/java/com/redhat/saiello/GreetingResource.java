package com.redhat.saiello;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

import static java.lang.String.format;

@Consumes("application/json")
@Produces("application/json")
@Path("/greetings")
public class GreetingResource {

    @POST
    @Path("/meet/{name}")
    public void hello(@PathParam ("name") String name, @Context HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("name", name);
    }


    @GET
    @Path("/")
    public String hello(@Context HttpServletRequest request){
        HttpSession session = request.getSession();
        String name = (String)session.getAttribute("name");

        return format("Hello, %s!", name);
    }
}
