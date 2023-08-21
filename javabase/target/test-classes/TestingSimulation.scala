/**
  * mvn archetype:generate -DinteractiveMode=false -DarchetypeGroupId=io.gatling.highcharts -DarchetypeArtifactId=gatling-highcharts-maven-archetype -DgroupId=com.ezlippi  -DartifactId=gatling.test  -Dversion=1.0
  *
  * @Author:qishan
  * @Date: 2019-03-15
  * @Desc: 压力测试gatling脚本
  **/

import Feeders._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class TestingSimulation extends Simulation {

//  val httpProtocol = http.baseURL("http://11.161.236.65:7001") // Here is the root for all relative URLs
//    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
//    .doNotTrackHeader("1")
//    .acceptLanguageHeader("en-US,en;q=0.5")
//    .acceptEncodingHeader("gzip, deflate")
//    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
//    .contentTypeHeader("application/json")
//
//  val headers_10 = Map("Content-Type" -> "application/x-www-form-urlencoded") // Note the headers specific to a given request

  //  local function randomTime()
  //  math.randomseed(rand()*1000000000)
  //  --offset=math.random(1,7)*86400
  //  offset=math.random(120*5,86400*3.4)
  //  startTime=os.date("%Y-%m-%d %X",1551628800-offset)
  //  endTime=os.date("%Y-%m-%d %X",1551628800+offset)
  //
  //  xn=math.random(1,5)
  //  xm=math.random(1,4)*10
  //
  //  yn=math.random(1,3)
  //  ym=math.random(1,5)*10
  //  jd= json.decode(templateJson)
  //  for k, v in ipairs(jd) do
  //  v['x']=v['x']+(xn/xm)
  //  v['y']=v['y']+(yn/ym)
  //  end
  //  wrk.body=json.encode(jd)
  //  return {startTime,endTime}
  //  end
  //  templateJson= '[{"x":116.51577960019505,"y":39.9908948226566,"z":0.0},{"x":116.51519582879381,"y":39.99545139072592,"z":0.0},{"x":116.51346273234184,"y":39.999832852439695,"z":0.0},{"x":116.5106467201125,"y":40.00387083068141,"z":0.0},{"x":116.50685588102439,"y":40.007410148211754,"z":0.0},{"x":116.50223584951007,"y":40.010314791042916,"z":0.0},{"x":116.49696421581943,"y":40.01247313537984,"z":0.0},{"x":116.49124369449376,"y":40.01380223726007,"z":0.0},{"x":116.48529431457435,"y":40.01425102004371,"z":0.0},{"x":116.47934493465493,"y":40.01380223726007,"z":0.0},{"x":116.47362441332926,"y":40.01247313537984,"z":0.0},{"x":116.46835277963862,"y":40.010314791042916,"z":0.0},{"x":116.4637327481243,"y":40.007410148211754,"z":0.0},{"x":116.45994190903619,"y":40.00387083068141,"z":0.0},{"x":116.45712589680686,"y":39.999832852439695,"z":0.0},{"x":116.45539280035489,"y":39.99545139072592,"z":0.0},{"x":116.45480902895365,"y":39.9908948226566,"z":0.0},{"x":116.45539678952726,"y":39.98633825458728,"z":0.0},{"x":116.45713326783647,"y":39.981956792873504,"z":0.0},{"x":116.4599515397509,"y":39.97791881463179,"z":0.0},{"x":116.46374317233528,"y":39.974379497101445,"z":0.0},{"x":116.46836241035427,"y":39.971474854270284,"z":0.0},{"x":116.47363178436021,"y":39.96931650993336,"z":0.0},{"x":116.47934892382824,"y":39.967987408053126,"z":0.0},{"x":116.48529431457435,"y":39.96753862526949,"z":0.0},{"x":116.49123970532045,"y":39.967987408053126,"z":0.0},{"x":116.49695684478849,"y":39.96931650993336,"z":0.0},{"x":116.50222621879442,"y":39.971474854270284,"z":0.0},{"x":116.50684545681341,"y":39.974379497101445,"z":0.0},{"x":116.51063708939779,"y":39.97791881463179,"z":0.0},{"x":116.51345536131223,"y":39.981956792873504,"z":0.0},{"x":116.51519183962144,"y":39.98633825458728,"z":0.0},{"x":116.51577960019505,"y":39.9908948226566,"z":0.0}]'

  //  local returnRequest = "/history/queryParam?startTime="..urlEncode(se[1]).."&endTime="..urlEncode(se[2]).."&accountId=122718&pageSize=200"
//  val randomIdFeeder = Iterator.continually(Map("id" -> (scala.util.Random.nextInt(100))))
//  val brownse = feed(randomOffsetFeeder).exec("http://baidu.com")

//  forever(feed(randomIdFeeder).exec(http("Random id browse").get("/articles/${id}"))) //.check(status.is(200))
//  val scn = scenario("GisQuery") // A scenario is a chain of requests and pauses
//    // Here's an example of a POST request
//    .exec(
//    http("gis").post("/history/queryParam").headers(headers_10)
//      .formParam("startTime", "Beautiful Computer") // Note the triple double quotes: used in Scala for protecting a whole chain of characters (no need for backslash)
//      .formParam("endTime", "2012-05-30")
//      .formParam("accountId", "122718")
//      .formParam("pageSize", "200")
//  )
//  setUp(scn.inject(atOnceUsers(1)).protocols(httpProtocol))
}
