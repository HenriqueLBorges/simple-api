package adapters

import com.twitter.finagle
import com.twitter.finagle.Http
import com.twitter.finagle.http.{Method, Request, Response}
import com.twitter.util.{Await, Future}
import io.circe.Json
import io.circe.parser._
import ports.HttpPort

/** An Http requester.
 *
 */
object HttpAdapter extends HttpPort {

  /** Makes a GET request.
   *
   * @return The HTTP reponse in JSON format.
   */
  def get(): Json = {
    val responseBody: String = request()
    parseResponse(responseBody)
  }

  /** Makes a GET request.
   *
   * @return The HTTP reponse in String format.
   */
  private def request(): String = {
    val host: String = scala.util.Properties.envOrElse("HostPath", "jsonplaceholder.typicode.com")
    val path: String = scala.util.Properties.envOrElse("UsersPath", "/users")
    val client: finagle.Service[Request, Response] = Http.client.withTls(host).newService(host + ":443")
    val request = Request(Method.Get, path)
    val response: Future[Response] = client(request)

    Await.result(response.onSuccess { rep: Response =>
    }).contentString
  }

  /** Parse a JSON String to JSON.
   *
   * @param responseBody The String that will be parsed.
   * @return The JSON data.
   */
  private def parseResponse(responseBody: String): Json = {
    parse(responseBody).getOrElse(Json.Null)
  }
}
