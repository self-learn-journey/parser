package com.learn.parser.service

import com.fasterxml.jackson.databind.JsonNode

trait BaseMapper {
  val id: String
  def getKeyOutput(message: JsonNode): JsonNode
  def getValueOutput(message: JsonNode): JsonNode
  private val log = org.slf4j.LoggerFactory.getLogger(classOf[BaseMapper])



}
