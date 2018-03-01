import com.sap.gateway.ip.core.customdev.util.Message as CpiMsg
import org.apache.camel.component.ahc.AhcOperationFailedException

CpiMsg init(CpiMsg msg) {
	String body = msg.getBody(String)
	msg.properties.url = body ?: "http://httpbin.org/azaza"
	msg
}

CpiMsg checkException(CpiMsg msg) {
	Exception e = msg.properties.CamelExceptionCaught
	if (e instanceof AhcOperationFailedException) {
		AhcOperationFailedException ahc = (AhcOperationFailedException)e
		String rb = ahc.responseBody
		String rl = ahc.redirectLocation
		String rt = ahc.statusText
		int rc = ahc.statusCode
		if (rc==404) {
			msg.properties.isescalated = "true"
		} else if (rc==302) {
			msg.properties.iserror = "true"
		}
//		msg.properties.remove("CamelExceptionCaught")
	}

	msg
}