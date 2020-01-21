package app.controllers

import io.finch.{Endpoint, Output}

/** The authentication controller abstraction.
 *
 */
trait IAuthController {

  /** Validates the request.
   *
   *  @return Represents the HTTP endpoint.
   */
  val authenticate: Endpoint[String]
}
