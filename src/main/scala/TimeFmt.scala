class TimeFmt {
  // ミリ秒を時間に変換
  def fmt(time: Long): String ={
    s"${"%02d".format(time/1000/60/60)}:${"%02d".format(time/1000/60%60)}:${"%02d".format(time/1000%60%60)}"
  }
}
