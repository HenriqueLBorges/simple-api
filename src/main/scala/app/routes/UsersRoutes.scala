package app.routes

import app.controllers.{AuthController, IUsersController}
import io.circe.Json
import io.finch.Endpoint
import io.finch.paramOption
import io.finch.syntax.get

/** The user routes.
 *
 * @param controller The controller to the user's routes
 * @param authController The authentication controller to the user's routes
 */
case class UsersRoutes (controller: IUsersController, authController: AuthController) {

  /** A route to search specific users.
   *
   *  @return Represents the HTTP endpoint and returns list of users.
   */
  def search: Endpoint[List[Json]] = get("search" :: paramOption ("property") :: paramOption ("value")) { (property: Option[String], value: Option[String])  =>
    this.controller.search(property, value);
  }

  /** A route to filter specific properties from users.
   *
   *  @return Represents the HTTP endpoint and returns list of users.
   */
  def allUsers: Endpoint[List[Json]] = get("allUsers" :: paramOption ("property")) { property: Option[String] =>
    this.controller.allUsers(property)
  }

  /** A route to get all values from a specific property from all users.
   *
   *  @return Represents the HTTP endpoint and returns list of users.
   */
  def singleProperty: Endpoint[List[Json]] = get("singleProperty" :: paramOption ("property")) { property: Option[String] =>
    this.controller.singleProperty(property)
  }
}
