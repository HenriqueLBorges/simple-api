package ports
import io.circe.Json

/** An Http requester abstraction.
 *
 */
trait HttpPort {

  /** Makes a GET request.
   *
   * @return The HTTP reponse in JSON format.
   */
  def get(): Json
}
