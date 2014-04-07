/**
 * 
 */
package com.jeffmaury.moviebuddy.server;

import java.util.List;

/**
 * @author jeffmaury
 *
 */
public interface MovieService {

	public List<Movie> listMovies();

  /**
   * @param id
   * @return
   */
  public Movie findMovie(int id);

  /**
   * @param title
   * @param limit
   * @return
   */
  public Iterable<Movie> findByTitle(String title, int limit);

  /**
   * @param actors
   * @param limit
   * @return
   */
  public Iterable<Movie> findByActors(String actors, int limit);

  /**
   * @param genre
   * @param limit
   * @return
   */
  public Iterable<Movie> findByGenre(String genre, int limit);
}
