/**
 * Measure effective timeouts for CPI
 *
 * @author Iliya Kuznetsov <iliya.kuznetsov@gmail.com>
 * @version 1.0.1
 * @date 2018-02-08
 * @see https://github.com/rsugio/cpi/tree/master/rsugio-cpi-004-timeout
 * @see https://support.f5.com/csp/article/K7606
 */

import com.sap.gateway.ip.core.customdev.util.Message as CpiMsg

CpiMsg r004http(CpiMsg msg) {
	
	String body = msg.getBody(String)
	if (!body) body = "5"
	msg.properties.s = body as Long
	msg.properties.begin = Date.getMillisOf(msg.properties.CamelCreatedTimestamp)
	def log = "\u0474\u2697"*20<<"\nSeconds:\t$msg.properties.s\nBegin:\t$msg.properties.CamelCreatedTimestamp\n"
	msg.setBody(log as String)
	msg.properties.idx = 0
	msg.properties.sleep = 'true'
	msg
}

CpiMsg r004sleep(CpiMsg msg) {
	String body = msg.getBody(String)
	Thread.sleep(1000L)
	long d = Date.getMillisOf(new Date()) - msg.properties.begin
	msg.properties.idx = msg.properties.idx + 1
	String t = "$body\n$msg.properties.idx\t$d ms"
	if (d >= 1000*msg.properties.s) {
		msg.properties.sleep = 'false'
		t = "$t\n\nFinished! ${new Date()}"
	}
	msg.setBody(t)
	msg
}
