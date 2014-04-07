/**
 * 
 */
package com.jeffmaury.moviebuddy.server;

import java.util.List;

/**
 * @author Syspertec Java Dev Team
 *
 */
public interface UserService {

  public List<User> listUsers();
  
  /**
   * @param id
   * @return
   */
  public User findUser(int id);
  
  /**
   * @param name
   * @param limit
   * @return
   */
  public Iterable<User> findByName(String name, int limit);


}
