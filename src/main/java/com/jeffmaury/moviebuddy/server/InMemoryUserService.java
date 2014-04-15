/**
 * 
 */
package com.jeffmaury.moviebuddy.server;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.limit;
import static java.util.Collections.binarySearch;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.base.Predicate;
import com.jeffmaury.moviebuddy.server.JsonStream.ItemFactory;
import com.jeffmaury.moviebuddy.server.JsonStream.JsonItem;

/**
 * @author Syspertec Java Dev Team
 *
 */
public class InMemoryUserService implements UserService {

  public static final UserService INSTANCE = new InMemoryUserService();
  
  private MovieService movieService = InMemoryMovieService.INSTANCE;

  private static int[][] INIT_STATE = {
  	/*userid      movieid       vote*/
  	{3022,        772,          2 },
  	{3022,        24 ,          10},
  	{3022,        482,          4 },
  	{3022,        302,          7 },
  	{3022,        680,          6 },
  	{9649,        772,          2 },
  	{9649,        24 ,          8 },
  	{9649,        482,          9 },
  	{9649,        302,          3 },
  	{9649,        556,          8 },
  	{2349,        453,          7 },
  	{2349,        461,          9 },
  	{2349,        258,          10},
  	{2349,        494,          9 },
  	{2349,        158,          4 },
  	{ 496,        682,          4 },
  	{ 496,        559,          7 },
  	{ 496,        537,          4 },
  	{ 496,        352,          3 },
  	{ 496,        005,          9	}
  };
  
  private List<User> users;

  private InMemoryUserService() {
    try {
      users = JsonStream.asStream(InMemoryMovieService.class.getResourceAsStream("/users.json"), new ItemFactory<User>() {

        @Override
        public User build(JsonItem item) {
          return User.parse(item);
        }
      });
    	for(int[] triple : INIT_STATE) {
    		User user = findUser(triple[0]);
    		Movie movie = movieService.findMovie(triple[1]);
    		user.rates.put(movie, triple[2]);
    	}
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * {@inheritedDoc}
   */
  @Override
  public List<User> listUsers() {
    return users;
  }

  /**
   * {@inheritedDoc}
   */
  @Override
  public User findUser(int id) {
    int index = binarySearch(users, new User(id, "", ""));
    return (index < 0)? null: users.get(index);
  }

  protected Iterable<User> find(Predicate<User> predicate, int limit) {
    return limit(filter(users, predicate), limit);
  }

  /**
   * {@inheritedDoc}
   */
  @Override
  public Iterable<User> findByName(final String name, int limit) {
  	final Pattern pattern = Pattern.compile(name);
    return find(new Predicate<User>() {
      @Override
      public boolean apply(User user) {
        return pattern.matcher(user.name).find();
      }
    }, limit);
  }

}
