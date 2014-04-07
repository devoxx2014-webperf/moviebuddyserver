/**
 * 
 */
package com.jeffmaury.moviebuddy.server;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.limit;
import static java.util.Collections.binarySearch;

import java.io.IOException;
import java.util.List;

import com.google.common.base.Predicate;
import com.jeffmaury.moviebuddy.server.JsonStream.ItemFactory;
import com.jeffmaury.moviebuddy.server.JsonStream.JsonItem;

/**
 * @author Syspertec Java Dev Team
 *
 */
public class InMemoryUserService implements UserService {

  public static final UserService INSTANCE = new InMemoryUserService();
  
  private List<User> users;

  private InMemoryUserService() {
    try {
      users = JsonStream.asStream(InMemoryMovieService.class.getResourceAsStream("/users.json"), new ItemFactory<User>() {

        @Override
        public User build(JsonItem item) {
          return User.parse(item);
        }
      });
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
    return find(new Predicate<User>() {
      @Override
      public boolean apply(User user) {
        return user.name.contains(name);
      }
    }, limit);
  }

}
