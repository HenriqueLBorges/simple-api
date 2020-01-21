package app.controllers

import io.finch.Output
import io.circe.Json

/** The user controller abstraction.
 *
 */
trait IUsersController {

  /** Searches for a specific user base on a key and value pair inside JSON data.
   *
   *  @param property The property that will be searched in data.
   *  @param value The value expected inside the JSON property.
   *  @return The endpoint output containing a JSON list.
   */
  def search(property: Option[String], value: Option[String]): Output[List[Json]]

  /** Filters JSON properties based on a key and value pair.
   *
   *  @param properties Used to filter JSON properties.
   *  @return The endpoint output containing a JSON list.
   */
  def allUsers(properties: Option[String]): Output[List[Json]]

  /** Creates a list of values based on a determined JSON property.
   *
   *  @param property Used to filter JSON properties.
   *  @return The endpoint output containing a JSON list.
   */
  def singleProperty(property: Option[String]): Output[List[Json]]
}
