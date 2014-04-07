
package com.jeffmaury.moviebuddy.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
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
@Path("/movies")
public class MovieResource {

	private MovieService movieService = InMemoryMovieService.INSTANCE;
	
    /** Method processing HTTP GET requests, producing "text/plain" MIME media
     * type.
     * @throws IOException 
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public void getMovies(@Context HttpServletResponse response) throws IOException {
      PrintWriter writer = response.getWriter();
      writer.write("[");
      for(Movie movie : movieService.listMovies()) {
        writer.write(movie.toString());
        writer.write(",");
      }
      writer.write("]");
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovie(@PathParam("id") String id) {
      Movie movie = movieService.findMovie(Integer.parseInt(id));
      if (movie == null) {
        return Response.status(404).build();
      } else {
        return Response.ok(movie.toString()).build();
      }
    }
    
    @GET
    @Path("search/title/{title}/{limit}")
    @Produces(MediaType.APPLICATION_JSON)
    public void searchByTitle(@Context HttpServletResponse response, @PathParam("title") String title, @PathParam("limit") String limit) throws IOException {
      PrintWriter writer = response.getWriter();
      writer.write("[");
      for(Movie movie : movieService.findByTitle(title, Integer.parseInt(limit))) {
        writer.write(movie.toString());
      }
      writer.write("]");
      writer.flush();
    }

    @GET
    @Path("search/actors/{actors}/{limit}")
    @Produces(MediaType.APPLICATION_JSON)
    public void searchByActors(@Context HttpServletResponse response, @PathParam("actors") String actors, @PathParam("limit") String limit) throws IOException {
      PrintWriter writer = response.getWriter();
      writer.write("[");
      for(Movie movie : movieService.findByActors(actors, Integer.parseInt(limit))) {
        writer.write(movie.toString());
      }
      writer.write("]");
      writer.flush();
    }

    @GET
    @Path("search/genre/{genre}/{limit}")
    @Produces(MediaType.APPLICATION_JSON)
    public void searchByGenre(@Context HttpServletResponse response, @PathParam("genre") String genre, @PathParam("limit") String limit) throws IOException {
      PrintWriter writer = response.getWriter();
      writer.write("[");
      for(Movie movie : movieService.findByGenre(genre, Integer.parseInt(limit))) {
        writer.write(movie.toString());
      }
      writer.write("]");
      writer.flush();
    }
}
