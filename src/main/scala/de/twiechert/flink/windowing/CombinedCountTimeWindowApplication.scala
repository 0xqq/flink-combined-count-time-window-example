package de.twiechert.flink.windowing

import java.text.SimpleDateFormat

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.slf4j.{Logger, LoggerFactory}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows
import org.apache.flink.streaming.api.windowing.evictors.TimeEvictor
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.triggers.CountTrigger
import org.apache.flink.streaming.api.TimeCharacteristic

object CombinedCountTimeWindowApplication {

  val logger: Logger = LoggerFactory.getLogger(CombinedCountTimeWindowApplication.getClass)


  def main(args: Array[String]): Unit = {
    println("Hello from main of class")
    // local / embedded execution environment
    logger.info("Starting Stream Application")

    val env = StreamExecutionEnvironment.createLocalEnvironment()
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)


    val uberPickups =
      env.readTextFile(Params.UberData)
        .filter(line => !line.startsWith("\"Date/Time\""))
        .map(line => {
          val lineElements = line.split(",")
          // not thread safe, thus create one per record
          val UberDateTimeFormat = new SimpleDateFormat("M/d/yyyy H:mm:s")
          val timestamp = UberDateTimeFormat.parse(trimString(lineElements(0))).getTime
          (timestamp, lineElements(1).toDouble, lineElements(2).toDouble, trimString(lineElements(3)), 1)
        })
        .assignAscendingTimestamps(_._1)
        // key by the taxi base station
        .keyBy(_._4)
        // now, for every record determine the count of Uber pick ups for the last 30 minutes.
        .window(GlobalWindows.create())
        .trigger(CountTrigger.of(10))
        .evictor(TimeEvictor.of(Time.minutes(30)))
        .sum(4)

     uberPickups.writeAsCsv(Params.getOutputfilePath())


    env.execute()
    logger.info("Stream Execution Ended")
  }

  def trimString(string: String): String = string.substring(1, string.length - 2)
}

