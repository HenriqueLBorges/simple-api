package app.controllers

import io.finch.{Ok, Output}

/** The general controller.
 *
 */
case class GeneralController () extends IGeneralController {

  /** Tests the Application.
   *
   *  @constructor Creates a general controller.
   *  @return Represents the HTTP endpoint output.
   */
  def healthCheck(): Output[String] = {
    Ok("Ok!")
  }
}
