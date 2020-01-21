package app.controllers

import io.finch._

/** The authentication controller.
 *
 */
case class AuthController () extends IAuthController {
  private val key: String = scala.util.Properties.envOrElse("apiKey", "Bearer 123")

  /** Validates the request.
   *
   *  @constructor Creates a authentication controller.
   *  @return Represents the HTTP endpoint.
   */
  val authenticate: Endpoint[String] = header("Authorization").mapOutput(key =>
    if (key == this.key) Ok("Authenticated")
    else Unauthorized(new Exception("Unauthorized"))
  ).handle {
    case e: Error.NotPresent => Unauthorized(e)
  }
}
