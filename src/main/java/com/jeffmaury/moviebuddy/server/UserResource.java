
package com.jeffmaury.moviebuddy.server;

import static java.lang.Integer.parseInt;
import static java.lang.Math.sqrt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

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
    
    @GET
    @Path("share/{userId1}/{userId2}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getShare(@PathParam("userId1") String userId1, @PathParam("userId2") String userId2) {
      User user1 = userService.findUser(parseInt(userId1));
      User user2 = userService.findUser(parseInt(userId2));
      if ((user1 != null) && (user2 != null)) {
        StringBuilder builder = new StringBuilder("[");
        Set<Movie> commons = new HashSet<Movie>();
        commons.addAll(user1.rates.keySet());
        commons.retainAll(user2.rates.keySet());
        for(Movie movie : commons) {
          builder.append(movie.toString()).append(',');
        }
        builder.append(']');
        return Response.ok(builder.toString()).build();
      } else {
        return Response.status(404).build();
      }
    }

    @GET
    @Path("distance/{userId1}/{userId2}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDistance(@PathParam("userId1") String userId1, @PathParam("userId2") String userId2) {
      User user1 = userService.findUser(parseInt(userId1));
      User user2 = userService.findUser(parseInt(userId2));
      if ((user1 != null) && (user2 != null)) {
        StringBuilder builder = new StringBuilder("{");
        double sum = 0.0;
        for(Movie movie : user1.rates.keySet()) {
          int rate1 = user1.rates.get(movie);
          Integer rate2 = user2.rates.get(movie);
          if (rate2 != null) {
            double diff = rate1 - rate2;
            sum += (diff * diff);
          }
        }
        sum = 1.0 / (1.0 + sqrt(sum));
        builder.append("\"distance\":").append(sum);
        builder.append('}');
        return Response.ok(builder.toString()).build();
      } else {
        return Response.status(404).build();
      }
    }

}
