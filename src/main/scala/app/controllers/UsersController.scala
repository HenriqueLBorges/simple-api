package app.controllers

import app.services.IUsersService
import io.finch.Output
import io.finch.Ok
import ports.HttpPort
import io.circe.Json

/** The user controller.
 *
 * @param usersService The user service abstraction.
 * @param http The HTTP requester abstraction.
 */
case class UsersController (usersService: IUsersService, http: HttpPort) extends IUsersController{

  /** Searches for a specific user based on a key and value pair inside JSON data.
   *
   *  @param property The property that will be searched in data.
   *  @param value The value expected inside the JSON property.
   *  @return The endpoint output containing a JSON list.
   */
  def search(property: Option[String], value: Option[String]): Output[List[Json]] = {
    val key = property.getOrElse("suite")
    val keyValue = value.getOrElse("*")
    val response = http.get()
    Ok(this.usersService.search(key, keyValue, response))
  }

  /** Filters JSON properties based on a key and value pair.
   *
   *  @param properties Used to filter JSON properties.
   *  @return The endpoint output containing a JSON list.
   */
  def allUsers(properties: Option[String]): Output[List[Json]] = {
    val keysParam = properties.getOrElse("name,email,company.name")
    val keys  = keysParam.split(",").toList
    val response = http.get()
    Ok(this.usersService.allUsers(keys, response))
  }

  /** Creates a list of values based on a determined JSON property.
   *
   *  @param property Used to filter JSON properties.
   *  @return The endpoint output containing a JSON list.
   */
  def singleProperty(property: Option[String]): Output[List[Json]] = {
    val key = property.getOrElse("website")
    val response = http.get()
    Ok(this.usersService.singleProperty(key, response))
  }

}
