package io.rsug.cpi

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class Rsug002log {
    private boolean connectedSftp = false, useTempFiles = false
    private com.jcraft.jsch.JSch jsch = null
    private com.jcraft.jsch.Session session = null
    private com.jcraft.jsch.ChannelSftp sftp = null

    void connectSftp(String host_sftp, String uname, byte[] pwd, boolean ignoreSSL = false) throws IOException {
        jsch = new com.jcraft.jsch.JSch()
        session = jsch.getSession(uname, host_sftp)
        session.setPassword(pwd)
        session.setConfig("StrictHostKeyChecking", ignoreSSL ? "yes" : "no")
        session.connect()

        sftp = session.openChannel("sftp")
        sftp.connect()
        connectedSftp = true
    }

    void disconnect() {
//        String fn = msg.getHeader("CamelFileName", String)
//        assert fn : "CamelFileName has to be set before"
//        sftp.put(msg.getBody(InputStream), fn)
        if (connectedSftp) {
            sftp.disconnect()
            session.disconnect()
        }
    }

    void addAttachment(String name, Object content, String mimeType) {

    }

    void addStringProperty(String name, String property) {

    }


}
