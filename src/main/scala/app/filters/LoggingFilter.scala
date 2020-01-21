package app.filters

import com.twitter.finagle.{http, Service, SimpleFilter}
import com.twitter.util._
import domain.value_objects.Log
import ports.ApacheKafkaPort

/** A logging filter to a finagle service.
 *
 */
class LoggingFilter extends SimpleFilter[http.Request, http.Response] {
  private var logger: ApacheKafkaPort[http.Response] = _

  /** Creates a new logging filter.
   *
   *  @param request The HTTP request.
   *  @param service The service that will be filtered.
   *  @return The HTTP response.
   */
  def apply(request: http.Request, service: Service[http.Request, http.Response]): Future[http.Response] = {
    val elapsed = Stopwatch.start()
    val future = service(request)
    this.logger = logger;
    future respond {
      case Return(reply) =>
        this.log(elapsed(), request, reply)
      case Throw(throwable) =>
        logException(elapsed(), request, throwable)
    }
    future
  }

  /** Sets a new Logger.
   *
   *  @param logger The Apache Kafka logger.
   */
  def setLogger(logger: ApacheKafkaPort[http.Response]): Unit = {
    this.logger = logger;
  }

  /** Logs a new message.
   *
   *  @param replyTime The request duration in milliseconds.
   *  @param request The HTTP request.
   *  @param reply The HTTP response
   */
  protected def log(replyTime: Duration, request: http.Request, reply: http.Response): Unit = {
    val log: Log = new Log(request.path, request.method.toString(), request.remoteAddress.getHostAddress(), replyTime, Some(reply.toString()), None)
    logger.log(log)
  }

  /** A route to search specific users.
   *
   *  @param replyTime The request duration in milliseconds.
   *  @param request  The HTTP request.
   *  @param throwable The service exception.
   *  @return Represents the HTTP endpoint and returns list of users.
   */
  protected def logException(replyTime: Duration, request: http.Request, throwable: Throwable): Unit = {
    val log: Log = new Log(request.path, request.method.toString(), request.remoteAddress.getHostAddress(), replyTime, None, Some(throwable.getMessage()))
    logger.log(log)
  }
}
