package domain.value_objects

import com.twitter.util.Duration

/** A log message.
 *
 *  @constructor creates a new log message with the following params.
 *  @param endpoint The endpoint that the user called.
 *  @param ip The user's ip.
 *  @param replyTime The request duration in milliseconds.
 *  @param reply The request response if available.
 *  @param exception The request exception if available.
 */
case class Log (endpoint: String, method: String, ip: String, replyTime: Duration,  reply: Option[String] = None,  exception: Option[String] = None)
