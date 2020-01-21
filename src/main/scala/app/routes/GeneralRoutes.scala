package app.routes

import app.controllers.{AuthController, IGeneralController}
import io.finch.Endpoint
import io.finch.syntax.get

/** The general routes.
 *
 * @param controller The controller to the general routes
 * @param authController The authentication controller to the general routes
 */
case class GeneralRoutes (controller: IGeneralController, authController: AuthController) {

  /** A route to test the Application.
   *
   *  @return Represents the HTTP endpoint output.
   */
  def healthCheck: Endpoint[String] = get("health-check" :: authController.authenticate) { resp: String =>
    this.controller.healthCheck()
  }
}
