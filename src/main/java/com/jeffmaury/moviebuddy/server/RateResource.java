
package com.jeffmaury.moviebuddy.server;

import java.io.IOException;
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

	private static MovieService movieService = InMemoryMovieService.INSTANCE;
	
  private static UserService userService = InMemoryUserService.INSTANCE;
  

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
        return Response.status(301).header("Location", "/rates/" + user._id).build();
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

}
