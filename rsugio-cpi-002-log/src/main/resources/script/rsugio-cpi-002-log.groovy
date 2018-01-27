/**
 * Some logging technics
 *
 * @author Iliya Kuznetsov <iliya.kuznetsov@gmail.com>
 * @version 1.0.1
 * @date 2018-01-27
 * @see https://github.com/rsugio/cpi/tree/master/rsugio-cpi-002-log
 */
import com.sap.gateway.ip.core.customdev.util.Message as CpiMsg
import com.jcraft.jsch.JSch

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

import com.sap.it.api.ITApiFactory
import com.sap.it.spi.ITApiHandler
import com.sap.it.api.securestore.SecureStoreService
import com.sap.it.api.securestore.UserCredential
import com.sap.it.api.securestore.exception.SecureStoreException
import com.sap.it.api.keystore.KeystoreService
import com.sap.it.api.keystore.exception.KeystoreException

class Rsugio002logger {
          
          
}


CpiMsg log002init(CpiMsg msg) {
	List any = ['1', '2', '3', '4', '5']
	String content = "1234"*20+"\n"*10+"..."
	
	msg.properties.logs = [:]
	any.each{
	    msg.properties.logs[it] = content
	}
	msg
}

CpiMsg p002makeZip(CpiMsg msg) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream()
    ZipOutputStream zos = new ZipOutputStream(baos)
    PrintWriter pw = new PrintWriter(zos)
    msg.properties.logs.each{String k, String v ->
        ZipEntry ze = new ZipEntry("${k}.txt")
        zos.putNextEntry(ze)
        pw.println(v)
        pw.flush()
        zos.closeEntry()
    }
    pw.close()
    zos.close()
    baos.close()
    msg.setBody(baos.toByteArray())
    
    String tn = System.properties['com.sap.it.node.tenant.name']
    org.apache.camel.Exchange camelExch = msg.exchange
    org.apache.camel.CamelContext camelCtx = camelExch.getContext()
    String iflowId = camelCtx.name
    String ts = msg.properties.CamelCreatedTimestamp.format("yyyy-MM-dd'T'HH_mm_ssZZZ", TimeZone.getTimeZone("Europe/Moscow"))
    String fn = "/outgoing/${tn}_${iflowId}_${ts}_.zip"
    msg.setHeader('CamelFileName', fn)
    msg
}

CpiMsg p002manual(CpiMsg msg) {
    def mpl = messageLogFactory.getMessageLog(msg)
    
    def service = ITApiFactory.getApi(SecureStoreService.class, null)
    com.sap.it.api.securestore.UserCredential cred = service.getUserCredential(msg.properties.Credential_Sftp)
    String host_sftp = msg.properties.Host_Sftp
    
    com.jcraft.jsch.JSch jsch=new com.jcraft.jsch.JSch()
    com.jcraft.jsch.Session session = jsch.getSession(cred.username, host_sftp)
    session.setPassword(new String(cred.password))
    session.setConfig("StrictHostKeyChecking", "no")
    session.connect()

    com.jcraft.jsch.Channel sftpc = session.openChannel("sftp")
    sftpc.connect()
    com.jcraft.jsch.ChannelSftp sftp = (com.jcraft.jsch.ChannelSftp)sftpc
    String fn = msg.getHeader("CamelFileName", String)
    assert fn : "CamelFileName has to be set before"
    sftp.put(msg.getBody(InputStream), fn)
    sftp.disconnect()
    session.disconnect()
	msg
}

 