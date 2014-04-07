
package com.jeffmaury.moviebuddy.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/** Example resource class hosted at the URI path "/myresource"
 */
@Path("/users")
public class UserResource {
 
	private UserService userService = InMemoryUserService.INSTANCE;
	
    /** Method processing HTTP GET requests, producing "text/plain" MIME media
     * type.
     * @throws IOException 
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public void getUsers(@Context HttpServletResponse response) throws IOException {
      PrintWriter writer = response.getWriter();
      writer.write("[");
      for(User user : userService.listUsers()) {
        writer.write(user.toString());
        writer.write(",");
      }
      writer.write("]");
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") String id) {
      User user = userService.findUser(Integer.parseInt(id));
      if (user == null) {
        return Response.status(404).build();
      } else {
        return Response.ok(user.toString()).build();
      }
    }

    @GET
    @Path("search/{name}/{limit}")
    @Produces(MediaType.APPLICATION_JSON)
    public void searchByName(@Context HttpServletResponse response, @PathParam("name") String name, @PathParam("limit") String limit) throws IOException {
      PrintWriter writer = response.getWriter();
      writer.write("[");
      for(User user : userService.findByName(name, Integer.parseInt(limit))) {
        writer.write(user.toString());
      }
      writer.write("]");
      writer.flush();
    }
    
}
