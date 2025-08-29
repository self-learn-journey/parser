package com.learn.parser.service

import com.fasterxml.jackson.databind.JsonNode

trait BaseMapper {
  val id: String
  def getKeyOutput(message: JsonNode): JsonNode

}
