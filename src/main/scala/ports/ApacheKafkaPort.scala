package ports

import domain.value_objects.Log

/** An Apache Kafka abstraction
 *
 */
trait ApacheKafkaPort[Rep] {

  /** Logs a new message to the Apache Kafka.
   *
   *  @param log The message
   */
  def log(log: Log): Unit
}
