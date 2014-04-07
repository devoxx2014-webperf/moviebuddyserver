package com.jeffmaury.moviebuddy.server;

import static java.lang.Integer.compare;

import com.jeffmaury.moviebuddy.server.JsonStream.JsonItem;

class Movie implements Comparable<Movie>{
  final int _id;
  final String title;
  final String actors;
  final String genre;
  private String json;
  
  Movie(int _id, String title, String actors, String genre, String json) {
    this._id = _id;
    this.title = title;
    this.actors = actors;
    this.genre = genre;
    this.json = json;
  }
  
  @Override
  public int compareTo(Movie movie) {
    return compare(_id, movie._id);
  }
 
  /**
   * {@inheritedDoc}
   */
  @Override
  public String toString() {
    return json;
  }

  static Movie parse(JsonItem item) {
    return new Movie(
        item.getInt("_id"),
        item.getString("Title").toLowerCase(),
        item.getString("Actors").toLowerCase(),
        item.getString("Genre").toLowerCase(),
        item.toJSON());
  }
  
}