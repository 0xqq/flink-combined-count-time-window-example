package de.twiechert.flink.windowing

import java.text.SimpleDateFormat
import java.util.Calendar

object Params {


  val UberData = "./../flinkcombinedcounttimewindow/sample_data/uber-raw-data-jul14.csv"
  val OutputPath = "./../flinkcombinedcounttimewindow/out"
  val DateOutputFormat = new SimpleDateFormat("HH_mm_ss")

  def getOutputfilePath() = {
    val fileName = DateOutputFormat.format(Calendar.getInstance().getTime())
    s"$OutputPath/$fileName"


  }
}

