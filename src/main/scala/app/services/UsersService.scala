package app.services

import io.circe.Json
import io.circe.syntax._
import scala.collection.mutable.ListBuffer

/** The user service.
 *
 */
object UsersService extends IUsersService {

  /** Searches for a key and value pair inside JSON data.
   *
   *  @param data The data in JSON format.
   *  @param key The property that will be searched in data.
   *  @param value The value expected inside the JSON property.
   *  @return A boolean representing the present of the key value pair inside the JSON data.
   */
  private def searchJson(data: Json, key: String, value: String): Boolean = {
    var evaluation = false
    val allValues = if (value == "*" || value == "") true else false
    data.mapObject(dataObj => {
        evaluation = dataObj.values.exists(jsonValue => {
          val foundKey = dataObj.keys.exists(objKey => objKey == key)
          var foundValue: Boolean = false
            var found: Boolean = false
          if(jsonValue.isNumber){
            val convertedValue = jsonValue.asNumber.getOrElse(0)
            if(allValues) foundValue = true
            else if(convertedValue == value) foundValue = true
            found = foundValue && foundKey
          } else if(jsonValue.isString){
            val convertedValue = jsonValue.asString.getOrElse("")
            if(allValues) foundValue = true
            else if(convertedValue == value) foundValue = true
            found = foundValue && foundKey
          } else if(jsonValue.isObject){
            found = this.searchJson(jsonValue, key, value)
          }
            found
      })
      dataObj
    })
    evaluation
  }

  /** Searches for specifics JSON values inside determined keys.
   *
   *  @param data The data in JSON format.
   *  @param keys The properties that will be searched in data.
   *  @return The JSON values
   */
  private def getJson(data: Json, keys: List[String]): List[Json] = {
    var result: ListBuffer[Json] = ListBuffer[Json]()

    data.mapObject(dataObj => {
      val foundKeys: List[String] =  keys.filter(key => dataObj.keys.exists(objKey => objKey == key)).distinct
      val remainingKeys: List[String] = keys.filter(key => dataObj.keys.filter(objKey => objKey == key).isEmpty)
      val nestedKeys = keys.filter((key) => key.contains("."))

      dataObj.toMap.foreach((json: (String, Json)) => {
        val jsonKey = json._1
        val jsonProperty = json._2
        val foundKey = foundKeys.exists((key) => key == jsonKey)

        if ((jsonProperty.isString || jsonProperty.isNumber || jsonProperty.isArray || jsonProperty.isBoolean) && foundKey) {
          result += jsonProperty
        } else if (!nestedKeys.isEmpty  && jsonProperty.isObject) {
          val nestedKey = nestedKeys.head.split("\\.").toList
          val firstPart = nestedKey(0)
          val rest = nestedKey.filter((key) => key != firstPart).mkString(".")
          val foundNestedProperty = (firstPart == jsonKey)

          if(foundNestedProperty) {
            result = result ++ this.getJson(jsonProperty, List(rest)).to[ListBuffer]
          }
        } else if(!remainingKeys.isEmpty && jsonProperty.isObject){
          result = result ++ this.getJson(jsonProperty, remainingKeys).to[ListBuffer]
        }
      })
      dataObj
    })
    result.toList
  }

  /** Filters JSON data based on a key and value pair.
   *
   *  @param key The key that will be searched inside the Json data.
   *  @param value The value that will be used to filter the Json data.
   *  @param response The data in JSON format.
   *  @return The JSON values
   */
  def search(key: String, value: String, response: Json): List[Json] = {
    val Right(data) = response.as[List[Json]]
    data.filter(user => this.searchJson(user, key, value))
  }

  /** Filters JSON properties based on a key and value pair.
   *
   *  @param keys The key that will be used to filter JSON properties.
   *  @param response The data in JSON format.
   *  @return The JSON values.
   */
  def allUsers(keys: List[String], response: Json): List[Json] = {
    val Right(data) = response.as[List[Json]]
    val strippedData = data.map(user => {
      val result = this.getJson(user, keys)
      if(result.isEmpty)
        Json.Null
      else {
        var response = scala.collection.mutable.Map[String, Json]()
        keys.zip(result).toMap.foreach((item: (String, Json)) => {
          response += (item._1 -> item._2)
        })
        response.asJson
      }
    })

    strippedData sortBy (_.asObject.get(keys(0)).getOrElse(Json.Null).toString())
  }

  /** Creates a list of values based on a determined JSON property.
   *
   *  @param key The key that will be used to filter JSON properties.
   *  @param response The data in JSON format.
   *  @return The JSON values.
   */
  def singleProperty(key: String, response: Json): List[Json] = {
    val Right(data) = response.as[List[Json]]
    data.map(user => {
      val result = this.getJson(user, List(key))
      if(result.isEmpty)
        Json.Null
      else result.head
    })
  }
}
