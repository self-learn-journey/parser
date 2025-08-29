package com.learn.parser.controller

import com.fasterxml.jackson.databind.JsonNode
import com.learn.parser.service.ParserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.{GetMapping, PostMapping, RequestBody, RequestHeader, RequestMapping, RestController}
import io.micrometer.core.annotation.Timed

import java.util
import scala.reflect.internal.util.NoSourceFile.path

@RestController
class RootController(parserService: ParserService) {
  val appname = "Parser Service application"

  @GetMapping(path = Array("/", "/health.html"))
  @Timed
  def healthCheck(): ResponseEntity[String] = {
    ResponseEntity.ok.body(appname + " is up and running!")
  }

  @PostMapping(path = Array("/parse-service"))
  @Timed
  def parse(@RequestBody payload: String, @RequestHeader headers: util.Map[String, String]): ResponseEntity[JsonNode] = {
    this.parserService.parse(payload, headers)
  }
}
