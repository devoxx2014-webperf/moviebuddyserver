
package com.jeffmaury.moviebuddy.server;

import java.io.IOException;
import static java.lang.Math.sqrt;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Integer.parseInt;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/** Example resource class hosted at the URI path "/myresource"
 */
@Path("/rates")
public class RateResource {

	private MovieService movieService = InMemoryMovieService.INSTANCE;
	
  private UserService userService = InMemoryUserService.INSTANCE;

  /** Method processing HTTP GET requests, producing "text/plain" MIME media
     * type.
     * @throws IOException 
     */
    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response rate(@Context HttpServletRequest request) throws IOException {
      JsonNode node = new ObjectMapper().readTree(request.getInputStream());
      User user = userService.findUser(node.path("userId").asInt());
      Movie movie = movieService.findMovie(node.path("movieId").asInt());
      if ((user != null) && (movie != null)) {
        user.rates.put(movie, node.path("rate").asInt());
        return Response.status(201).build();
      } else {
        return Response.status(404).build();
      }
    }
    
    @GET
    @Path("{userId1}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRate(@PathParam("userId1") String userId1) {
      User user = userService.findUser(parseInt(userId1));
      if (user != null) {
        StringBuilder builder = new StringBuilder("{");
        for(Movie movie : user.rates.keySet()) {
          builder.append('"').append(movie._id).append('"').append(':').append(user.rates.get(movie)).append(',');
        }
        builder.append('}');
        return Response.ok(builder.toString()).build();
      } else {
        return Response.status(404).build();
      }
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
