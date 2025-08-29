package com.learn.parser.service

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.learn.parser.config.{ParserConfig, PipelineDetail}
import org.springframework.stereotype.Service
import org.springframework.http.{HttpStatus, ResponseEntity}

import java.time.LocalDateTime
import java.util
import java.util.Optional

@Service
class ParserService(baseMappers: util.List[BaseMapper], parserConfig: ParserConfig) {
  case class MapperResponse(body: JsonNode, statusCode: Int)

  private val log = org.slf4j.LoggerFactory.getLogger(classOf[ParserService])
  log.info("Available mappers at the startup")
  baseMappers.forEach(mapper => log.info(s"Mapper id: ${mapper.id}, class: ${mapper.getClass.getSimpleName}"))

  val mappers: util.HashMap[String, BaseMapper] = new util.HashMap[String, BaseMapper]()
  val om = new ObjectMapper()

  log.info(s"Intializaing ParserService with ${baseMappers.size()} mappers" )
  baseMappers.forEach(mp => {
    log.info(s"Registering mapper with id: ${mp.id}")
    mappers.put(mp.id, mp)
  })


  def parse(data: String, headers: util.Map[String, String]): ResponseEntity[JsonNode] = {
    val startTime = LocalDateTime.now()

    val message = om.readTree(data)

    val pipelineDetail: PipelineDetail = this.identifyMapper(headers).orElse(PipelineDetail("default", new util.ArrayList()))
    log.info(s"Pipeline identified: $pipelineDetail")

    //get the mapper and process the message
    val mapperId = pipelineDetail.mapperIdentifier
    log.info(s"Looking for mapper with id: $mapperId" )

    val mapper = mappers.get(mapperId)
    if(mapper == null){
      log.error(s"Mapper with id: $mapperId not found, available mappers: ${mappers.keySet()}")
      val errorResponse = om.createObjectNode()
      errorResponse.put("error", s"Mapper with id: $mapperId not found")
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }

    val mapperResult = mapper.getValueOutput(message)
    if (mapperResult == null) {
      log.error(s"getValueOutput returned null result")
      val errorResponse = om.createObjectNode()
      errorResponse.put("error", s"Mapper with id: $mapperId returned null result")
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }

    val mapperResponse = MapperResponse(
      body = mapperResult,
      statusCode = 200
    )

    val result = mapperResponse.body.asInstanceOf[JsonNode]

    ResponseEntity
      .status(mapperResponse.statusCode)
      .body(result)

  }

  def identifyMapper(headers: util.Map[String, String]): Optional[PipelineDetail] = {
    log.info(s"Identifying mapper from headers: $headers")
    val pipeline = parserConfig.parserConfig.values().parallelStream()
      .filter(pipeline => pipeline.conditions.parallelStream().allMatch(condition => condition.location match {
        case "HEADER" =>
          val headerValue = headers.get(condition.key.toLowerCase)
          headerValue != null && headerValue.toLowerCase.equals(condition.value)
        case _ => false
      })).findFirst()
    log.info(s"found Pipeline: $pipeline")
    pipeline
  }
}
