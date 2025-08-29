package com.learn.parser.service

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.springframework.stereotype.Service
import lombok.extern.slf4j.Slf4j


@Slf4j
@Service
class VisaDebitJsonMapper() extends  BaseMapper {

  val id: String = "visa"
  private val om = new ObjectMapper()
  private val log = org.slf4j.LoggerFactory.getLogger(classOf[VisaDebitJsonMapper])

  def getKeyOutput(message: JsonNode): JsonNode = {
    message.get("key")
  }

    def getValueOutput(message: JsonNode): JsonNode = {
      val valueNode = om.createObjectNode()
      try {
        val transactionId = message.get("transactionId").asText()
        val amount = message.get("amount").asDouble()
        val currency = message.get("currency").asText()
        val timestamp = message.get("timestamp").asText()
        val merchant = message.get("merchant").asText()

        valueNode.put("transactionId", transactionId)
        valueNode.put("amount", amount)
        valueNode.put("currency", currency)
        valueNode.put("timestamp", timestamp)
        valueNode.put("merchant", merchant)

      } catch {
        case ex: Exception =>
          log.error(s"Error processing message: ${ex.getMessage}")
          valueNode.put("error", "Invalid message format")
      }
      valueNode
    }
}
