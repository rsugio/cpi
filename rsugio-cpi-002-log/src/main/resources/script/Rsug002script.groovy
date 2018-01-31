package io.rsug.cpi

import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.SftpException

import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class Rsug002log {
    private boolean connectedSftp = false, useTempFiles = false
    private com.jcraft.jsch.JSch jsch = null
    private com.jcraft.jsch.Session session = null
    private com.jcraft.jsch.ChannelSftp sftp = null
    private Map<String, Path> tempFiles = [:]

    public void connectSftp(String host_sftp, Object cpiCred, boolean ignoreSSL = false) throws IOException {
        assert cpiCred != null
        assert cpiCred.getClass().getCanonicalName().startsWith('com.sap.it.api.')
        String uname = cpiCred.getUsername()
        char[] password = cpiCred.getPassword()
        byte[] pb = String.valueOf(password).getBytes()
        connectSftp(host_sftp, uname, pb, ignoreSSL)
    }

    public void connectSftp(String host_sftp, String uname, byte[] pwd, boolean ignoreSSL = false) throws IOException {
        jsch = new com.jcraft.jsch.JSch()
        session = jsch.getSession(uname, host_sftp)
        session.setPassword(pwd)
        session.setConfig("StrictHostKeyChecking", ignoreSSL ? "yes" : "no")
        session.connect()
        sftp = session.openChannel("sftp")
        sftp.connect()
        connectedSftp = true
    }

    public boolean isConnectedSftp() {
        return connectedSftp
    }

    public void disconnectSftp() {
        if (connectedSftp) {
            sftp.disconnect()
            session.disconnect()
        }
    }

    public void mkdirCdSftp(String dir) throws RuntimeException, SftpException {
        assert connectedSftp
        if (!connectedSftp) throw new RuntimeException("Not connected to SFTP")
        try {
            sftp.cd(dir)
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                sftp.mkdir(dir)
                sftp.cd(dir)
            }
        }
    }

    public void putSftp(String name, String content, boolean append = true) throws SftpException {
        putSftp(name, content.bytes, append)
    }

    public void putSftp(String name, byte[] content, boolean append = true) throws SftpException {
        assert connectedSftp
        if (!connectedSftp) throw new RuntimeException("Not connected to SFTP")
        OutputStream w = sftp.put(name, append ? ChannelSftp.APPEND : ChannelSftp.OVERWRITE)
        w.write(content)
        w.flush()
        w.close()
    }

    public Path createTempFile(String fn) throws IOException {
        String[] p = fn.split("\\.")
        Path tmp = Files.createTempFile(p[0] + "_", "." + (p.length > 0 ? p[p.length-1] : ""))
        tempFiles[fn] = tmp
    }

    public void tempToArchive(String archive) throws IOException {
        assert connectedSftp
        if (!connectedSftp) throw new RuntimeException("Not connected to SFTP")
        OutputStream w = sftp.put(archive, ChannelSftp.OVERWRITE)
        ZipOutputStream z = new ZipOutputStream(w)
        Iterator<Map.Entry<String,Path>> zz = tempFiles.iterator()
        while(zz.hasNext()) {
            Map.Entry<String,Path> x = zz.next()
            assert Files.isRegularFile(x.value)
            ZipEntry ze = new ZipEntry(x.key)
            z.putNextEntry(ze)
            org.apache.commons.io.IOUtils.copy(Files.newInputStream(x.value), z)
            Files.delete(x.value)
            z.closeEntry()
            z.flush()
        }
        z.close()
        w.close()
    }
}

Object r002sftp(Object msg) {
    assert msg && msg.getClass() == Class.forName("com.sap.gateway.ip.core.customdev.util.Message")
    String host_sftp = msg.properties.Host_Sftp
    String uc_sftp = msg.properties.Credential_Sftp
    if (!host_sftp)
        throw new RuntimeException("Property Host_Sftp is not set")
    if (!uc_sftp)
        throw new RuntimeException("Property Credential_Sftp is not set")
    Class itApiFactory = Class.forName("com.sap.it.api.ITApiFactory")
    Class secureStoreService = Class.forName("com.sap.it.api.securestore.SecureStoreService")
    Class keystoreService = Class.forName("com.sap.it.api.keystore.KeystoreService")
    Class userCredential = Class.forName("com.sap.it.api.securestore.UserCredential")
    def sss = itApiFactory.getApi(secureStoreService, null)
    def uc = sss.getUserCredential(uc_sftp)

    org.apache.camel.CamelContext camelCtx = msg.exchange.context
    String messageId = msg.properties.SAP_MessageProcessingLogID
    String iflowId = camelCtx.name
    String ccts = msg.properties.CamelCreatedTimestamp.format("yyyy-MM-dd'T'HH.mm.ss.SSSXXX")
    String tenantName = System.properties['com.sap.it.node.tenant.name']

    Rsug002log r2d2 = new Rsug002log()
    r2d2.connectSftp(host_sftp, uc, false)
    String dir_sftp = msg.properties.Directory_Sftp
    String dir = "/outgoing/$tenantName/$iflowId"
    r2d2.mkdirCdSftp(dir)
    msg.properties.log002 = r2d2
    msg.properties.log002name = "${ccts}_${messageId}.zip" as String
    msg
}

Object r002finish(Object msg) {
    assert msg && msg.getClass() == Class.forName("com.sap.gateway.ip.core.customdev.util.Message")
    String name = msg.properties.log002name
    Rsug002log r2d2 = msg.properties.log002
    if (!r2d2)
        throw new RuntimeException("No property `log002` set")
    else {
        if (r2d2.connectedSftp) {
            r2d2.tempToArchive(name)
            r2d2.disconnectSftp()
        }
    }
    msg
}
