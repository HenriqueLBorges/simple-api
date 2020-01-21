import adapters.ApacheKafkaAdapter
import adapters.HttpAdapter
import app.controllers.{AuthController, GeneralController, UsersController}
import app.routes.{GeneralRoutes, UsersRoutes}
import app.services.{IUsersService, UsersService}
import com.twitter.finagle.{Http, http}
import com.twitter.server.TwitterServer
import com.twitter.util.Await
import io.finch.{Application, InternalServerError}
import ports.{ApacheKafkaPort, HttpPort}
import io.finch.circe._
import app.filters.LoggingFilter

/** Server Object. */
object Server extends TwitterServer {
  private val port: String = scala.util.Properties.envOrElse("PORT", "8080")
  private val host: String = scala.util.Properties.envOrElse("HOST", "localhost")
  private val requester: HttpPort = HttpAdapter
  private val loggerAdapter: ApacheKafkaPort[http.Response] = new ApacheKafkaAdapter()
  private val usersService: IUsersService = UsersService
  private val authController: AuthController = new AuthController()
  private val usersController: UsersController = new UsersController(this.usersService, this.requester)
  private val usersRoutes: UsersRoutes = new UsersRoutes(this.usersController, this.authController)
  private val generalController: GeneralController = new GeneralController()
  private val generalRoutes: GeneralRoutes = new GeneralRoutes(this.generalController, this.authController)

  val api = (generalRoutes.healthCheck :+: usersRoutes.singleProperty :+: usersRoutes.allUsers :+: usersRoutes.search)

  val apiService = api.toServiceAs[Application.Json]

  val loggingFilter: LoggingFilter = new LoggingFilter();
  loggingFilter.setLogger(this.loggerAdapter)
  val filteredApiService = loggingFilter.andThen(apiService)

  /** Main Method.
   *
   */
  def main(): Unit = {
    println(s"Server is online and listening on ${host}:${port}")
    val server =
      Http.server
        .withStatsReceiver(statsReceiver)
        .serve(s"${this.host}:${this.port}", filteredApiService)
    closeOnExit(server)
    onExit { server.close() }
    Await.ready(adminHttpServer)
  }
}
