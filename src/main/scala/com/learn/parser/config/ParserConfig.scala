package com.learn.parser.config

import java.util
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.{Bean, Configuration}
import scala.beans.BeanProperty


case class Condition(key:String, location:String, value:String)
case class PipelineDetail(mapperIdentifier: String, conditions: util.List[Condition])

@Configuration
@ConfigurationProperties(prefix = "pipelines")
class ParserConfig{
  @BeanProperty
  var parserConfig: util.Map[String, PipelineDetail] = _
}
object ParserConfig{}


