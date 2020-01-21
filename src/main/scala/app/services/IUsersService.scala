package app.services

import io.circe.Json

/** The user service abstraction.
 *
 */
trait IUsersService {
  /** Filters JSON data based on a key and value pair.
   *
   *  @param key The key that will be searched inside the Json data.
   *  @param value The value that will be used to filter the Json data.
   *  @param response The data in JSON format.
   *  @return The JSON values
   */
  def search(key: String, value: String, response: Json): List[Json]

  /** Filters JSON properties based on a key and value pair.
   *
   *  @param keys The key that will be used to filter JSON properties.
   *  @param response The data in JSON format.
   *  @return The JSON values.
   */
  def allUsers(keys: List[String], response: Json): List[Json]

  /** Creates a list of values based on a determined JSON property.
   *
   *  @param key The key that will be used to filter JSON properties.
   *  @param response The data in JSON format.
   *  @return The JSON values.
   */
  def singleProperty(key: String, response: Json): List[Json]
}
