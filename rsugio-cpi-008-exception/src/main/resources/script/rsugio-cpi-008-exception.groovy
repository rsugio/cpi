import com.sap.gateway.ip.core.customdev.util.Message as CpiMsg
import org.apache.camel.component.ahc.AhcOperationFailedException

CpiMsg init(CpiMsg msg) {
	String body = msg.getBody(String)
	
	msg.properties.url = body ? "http://s.rsug.io/$body" as String : "http://s.rsug.io/503"
	msg.setBody("Empty payload")
	msg
}

CpiMsg checkException(CpiMsg msg) {
	def mpl = messageLogFactory.getMessageLog(msg)
	Exception e = msg.properties.CamelExceptionCaught
	if (e instanceof AhcOperationFailedException) {
		AhcOperationFailedException ahc = (AhcOperationFailedException)e
		String rb = ahc.responseBody
		String rl = ahc.redirectLocation
		String rt = ahc.statusText
		int rc = ahc.statusCode
		if (rc==500) {
			msg.properties.escalate = "true"
			msg.setBody("Process escalated for error code $rc" as String)
		} else if (rc==501) {
			msg.properties.error = "true"
			msg.setBody("Process failed for error code $rc" as String)
		} else {
		    msg.setBody("Error ignored for code $rc" as String)   
		}
		mpl.addAttachmentAsString("CamelExceptionCaught", e.toString(), "text/plain")
		msg.properties.remove("CamelExceptionCaught")
	} else {
		mpl.addAttachmentAsString("CamelExceptionCaught", e.toString(), "text/plain")
	}
//	mpl.addAttachmentAsString("properties", msg.properties.toString(), "text/plain")
//	mpl.addAttachmentAsString("headers", msg.headers.toString(), "text/plain")
//	msg.properties.remove("CamelFailureRouteId")
//	msg.properties.remove("CamelFailureEndpoint")
//	msg.properties.remove("SAP_ErrorModelStepID")
	msg.headers.CamelHttpResponseCode = 200
	msg.headers.CamelHttpResponseText = "OK"
	
	msg
}

CpiMsg log(CpiMsg msg) {
	def mpl = messageLogFactory.getMessageLog(msg)
	String s = """error: ${msg.properties.error}
escalated: ${msg.properties.escalated}
"""
	mpl.addAttachmentAsString("log", s, "text/plain")
	msg
}

CpiMsg whenNoError(CpiMsg msg) {
	def mpl = messageLogFactory.getMessageLog(msg)
	msg.setBody("No exception")
	msg
}