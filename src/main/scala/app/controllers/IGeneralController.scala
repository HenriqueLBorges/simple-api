package app.controllers

import io.finch.Output

/** The general controller abstraction.
 *
 */
trait IGeneralController {

  /** Tests the Application.
   *
   *  @return Represents the HTTP endpoint output.
   */
  def healthCheck(): Output[String]
}
