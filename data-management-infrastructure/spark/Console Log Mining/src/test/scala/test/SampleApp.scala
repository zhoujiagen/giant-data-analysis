package test

/**
  * @author zhoujiagen
  */
object SampleApp extends App {
	val line = "2016-02-01 19:17:54.880 [http-bio-8081-exec-35] ERROR   c.y.b.s.admin.UserAccessServiceImpl:620 - Somethine wrong in get user appTree!"

	val parts = line.split("\\s")

	for (part <- parts)
		if (part.contains("c.y.b")) {
			println(part)
		}

}