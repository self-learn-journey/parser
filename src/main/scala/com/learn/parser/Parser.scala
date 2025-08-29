package com.learn.parser

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration


@SpringBootApplication(
  exclude = Array(classOf[DataSourceAutoConfiguration], classOf[HibernateJpaAutoConfiguration])
)
class Parser

object Parser extends App {
  SpringApplication.run(classOf[Parser])
}



