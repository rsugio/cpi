import com.sap.gateway.ip.core.customdev.util.Message as CpiMsg
import com.jcraft.jsch.JSch

/**
 * Some logging technics
 *
 * @author Iliya Kuznetsov <iliya.kuznetsov@gmail.com>
 * @version 1.0.1
 * @date 2018-01-26
 */

/**
 * 
 * @param msg Incoming message (not used)
 * @return Outgoing message with plain text
 */
CpiMsg p002add(CpiMsg msg) {
	List any = ['1', '2', '3', '4', '5']
	String content = "1234"*20+"\n"*10+"..."
	
	msg.properties.logs = [:]
	any.each{
	    msg.properties.logs[it] = content
	}
	msg
}

CpiMsg p002mpl(CpiMsg msg) {
	def mpl = messageLogFactory.getMessageLog(msg)
	msg.properties.logs.each{String k, String v ->
	    mpl.addAttachmentAsString(k, v, 'text/plain')
	}
	msg
}

CpiMsg p002tgz(CpiMsg msg) {
	JSch jsch=new JSch()
	
	msg
}