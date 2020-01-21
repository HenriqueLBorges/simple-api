package adapters

import ports.ApacheKafkaPort
import java.util.Properties

import com.twitter.finagle.http.Response
import io.circe.syntax._
import io.circe.{Encoder, Json}
import domain.value_objects.Log
import org.apache.kafka.clients.producer._

/** An Apache Kafka Logger.
 *
 *  @constructor Creates a new Apache Kafka Logger.
 */
case class ApacheKafkaAdapter() extends ApacheKafkaPort[Response] {
  private val bootstrapServers: String = scala.util.Properties.envOrElse("KafkaBootstrapServers", "localhost:9092")
  private val keySerializer: String = scala.util.Properties.envOrElse("KafkaKeySerializer", "org.apache.kafka.common.serialization.StringSerializer")
  private val valueSerializer: String = scala.util.Properties.envOrElse("KafkaValueSerializer", "org.apache.kafka.common.serialization.ByteArraySerializer")
  private var producer: KafkaProducer[String, Array[Byte]] = _
  private val topic: String = scala.util.Properties.envOrElse("KafkaTopic", "requisitions")

  val props = new Properties()
  props.put("bootstrap.servers", this.bootstrapServers)
  props.put("key.serializer", this.keySerializer)
  props.put("value.serializer", this.valueSerializer)
  this.producer = new KafkaProducer[String, Array[Byte]](props)

  /** Logs a message.
   *
   *  @param message The message that will be logged.
   */
  def log(message: Log): Unit = {
    this.newRecord(message)
  }

  /** Writes a message into Apache Kafka.
   *
   *  @param message The message that will be logged.
   */
  private def newRecord(message: Log): Unit = {
    implicit val encoderLog = new Encoder[Log]() {
      final def apply(obj: Log): Json = Json.obj(
        ("endpoint", Json.fromString(obj.endpoint)),
        ("ip", Json.fromString(obj.ip)),
        ("method", Json.fromString(obj.method)),
        ("reply_time", Json.fromBigDecimal(obj.replyTime.inMilliseconds)),
        ("response", Some(obj.reply).asJson),
        ("error", Some(obj.exception).asJson)
      )
    }
    val record = new ProducerRecord[String, Array[Byte]](this.topic, "key", message.asJson.noSpaces.getBytes())
    this.producer.send(record)
    this.producer.close()
  }
};
