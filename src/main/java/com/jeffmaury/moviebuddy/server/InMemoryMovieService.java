package com.jeffmaury.moviebuddy.server;

import static java.util.Collections.binarySearch;

import java.io.IOException;
import java.util.List;

import com.google.common.base.Predicate;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.limit;

import com.jeffmaury.moviebuddy.server.JsonStream.ItemFactory;
import com.jeffmaury.moviebuddy.server.JsonStream.JsonItem;

public class InMemoryMovieService implements MovieService {

  public static final MovieService INSTANCE = new InMemoryMovieService();
  
	private List<Movie> movies;
	
	private InMemoryMovieService() {
		try {
	    movies = JsonStream.asStream(InMemoryMovieService.class.getResourceAsStream("/movies.json"), new ItemFactory<Movie>() {

				@Override
        public Movie build(JsonItem item) {
	        return Movie.parse(item);
        }
	    });
    }
		catch (IOException e) {
	    e.printStackTrace();
    }
	}

	@Override
  public List<Movie> listMovies() {
	  return movies;
  }

  /**
   * {@inheritedDoc}
   */
  @Override
  public Movie findMovie(int id) {
    int index = binarySearch(movies, new Movie(id, "", "", "", ""));
    return (index < 0)? null: movies.get(index);
  }

  protected Iterable<Movie> find(Predicate<Movie> predicate, int limit) {
    return limit(filter(movies, predicate), limit);
  }

  /**
   * {@inheritedDoc}
   */
  @Override
  public Iterable<Movie> findByTitle(final String title, int limit) {
    return find(new Predicate<Movie>() {
      @Override
      public boolean apply(Movie movie) {
        return movie.title.contains(title);
      }
    }, limit);
  }

  /**
   * {@inheritedDoc}
   */
  @Override
  public Iterable<Movie> findByActors(final String actors, int limit) {
    return find(new Predicate<Movie>() {
      @Override
      public boolean apply(Movie movie) {
        return movie.actors.contains(actors);
      }
    }, limit);
  }

  /**
   * {@inheritedDoc}
   */
  @Override
  public Iterable<Movie> findByGenre(final String genre, int limit) {
    return find(new Predicate<Movie>() {
      @Override
      public boolean apply(Movie movie) {
        return movie.genre.contains(genre);
      }
    }, limit);
  }

}
