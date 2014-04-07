package com.jeffmaury.moviebuddy.server;

import static java.lang.Integer.compare;

import java.util.HashMap;

import com.jeffmaury.moviebuddy.server.JsonStream.JsonItem;


class User implements Comparable<User> {
  final int _id;
  final String name;
  final String json;
  HashMap<Movie, Integer> rates = new HashMap<Movie, Integer>();
  
  User(int _id, String name, String json) {
    this._id = _id;
    this.name = name;
    this.json = json;
  }
  
  @Override
  public int compareTo(User user) {
    return compare(_id, user._id);
  }
  
  @Override
  public String toString() {
    return json;
  }
  
  static User parse(JsonItem item) {
    return new User(
        item.getInt("_id"),
        item.getString("name").toLowerCase(),
        item.toJSON());
  }
  
}