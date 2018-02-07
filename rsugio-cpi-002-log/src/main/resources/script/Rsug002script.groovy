package io.rsug.cpi

import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.SftpException
import com.sap.gateway.ip.core.customdev.util.Message as CpiMsg

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class Rsug002log {
    private boolean connectedSftp = false
    private boolean logHttp = false
    private com.jcraft.jsch.JSch jsch = null
    private com.jcraft.jsch.Session session = null
    private com.jcraft.jsch.ChannelSftp sftp = null
    synchronized int httpRespCount = 1
    private Map<String, Path> tempFiles = [:]
    private List<Path> sftpFiles = []
    private String cwd = null

    Rsug002log() {
        logHttp = false
    }

    Rsug002log(CpiMsg msg) {
        def mpc = msg.properties.SAP_MessageProcessingLogConfiguration
        logHttp = mpc.logLevel.toString() in ['DEBUG', 'TRACE']
    }

    public String mpl(CpiMsg msg) {
        def log = "\u0474\u2697"*20<<""
        def Map<String,Object> ps = msg.properties
        org.apache.camel.Exchange camelExch = msg.exchange
        org.apache.camel.CamelContext camelCtx = camelExch.context
        def osgiCtx = org.osgi.framework.FrameworkUtil.getBundle(msg.class).bundleContext
        String ccts = ps.CamelCreatedTimestamp.format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        def mpc = ps.SAP_MessageProcessingLogConfiguration
        String iflmapNode = camelExch.exchangeId.split("-")[1]

        log << "\nJava version:\t${System.properties['java.version']}"
        log << "\nGroovy version:\t${GroovySystem.version}"
        log << "\nCamel version:\t$camelCtx.version"
        log << "\nCamel uptime:\t$camelCtx.uptime"
        log << "\nIFLMAP node:\t$iflmapNode"
        log << "\ncom.sap.it.node.tenant.name:\t${System.properties['com.sap.it.node.tenant.name']}"
        log << "\ncom.sap.it.node.tenant.id:\t${System.properties['com.sap.it.node.tenant.id']}"
        log << "\ncom.sap.it.node.vmsize:\t${System.properties['com.sap.it.node.vmsize']}"

        log << "\n\nIFlow id:\t$camelCtx.name"
        log << "\nCamelExchange pattern:\t$camelExch.pattern"
        log << "\nCamelCreatedTimestamp:\t$ccts"
        log << "\nSAP_MessageProcessingLogID:\t$ps.SAP_MessageProcessingLogID\t\t//use it for navigation on MPLs. Somewhere it's MessageGuid"

        //JFY
        log << "\n\nMPLactive:\t${ps.SAP_MessageProcessingLogConfiguration.mplActive}"
        log << "\nMPLlevel:\t${ps.SAP_MessageProcessingLogConfiguration.logLevel}"

        log << "\n\n"<<"-"*100
        ps.each{String k, Object v ->
            log << "\nCpiMsgProperty\t$k = $v"
        }
//	log << "\n$ps.SAP_MessageProcessingLogConfiguration"
        log << "\n\n\n\nSystemEnvironment\t${System.getenv()}"
        log << "\n\n\n\nSystemProperties\t${System.properties}"
        log << "\n\n\n\norg.apache.camel.Exchange\t$camelExch"
        camelExch.properties.each{String p, Object r -> "\nCamelExchangeProperty $p = $r" }

        log << "\n\n\n\norg.apache.camel.CamelContext\t$camelCtx"
        log << "\nCamelContext.Status = ${camelCtx.getStatus()}"
        log << "\nCamelContext.DataFormats = ${camelCtx.getDataFormats()}"
        log << "\nCamelContext.DataFormatResolver = ${camelCtx.getDataFormatResolver()}"
        log << "\nCamelContext.ManagementName = ${camelCtx.getManagementName()}"
        log << "\nCamelContext.LanguageNames = ${camelCtx.getLanguageNames()}"
        log << "\nCamelContext.InflightRepository = ${camelCtx.getInflightRepository()}"
        log << "\nCamelContext.ClassLoader = ${camelCtx.getApplicationContextClassLoader()}"
        log << "\nCamelContext.ComponentNames = ${camelCtx.getComponentNames()}"
        log << "\nCamelContext.Endpoints = ${camelCtx.getEndpoints()}"
        log << "\nCamelContext.EndpointMap = ${camelCtx.getEndpointMap()}"
        log << "\nCamelContext.RouteDefinitions = ${camelCtx.routeDefinitions}"
        log << "\nCamelContext.Routes = ${camelCtx.routes}"
        camelCtx.getProperties().each{String p, Object r -> "\nCamelContextProperty $p = $r" }

        log << "\n\n\n\norg.osgi.framework.BundleContext\t${osgiCtx}"
        osgiCtx.bundles.each{org.osgi.framework.Bundle b ->
            log << "\n[$b.bundleId] $b.symbolicName\t$b.location\t$b.version\t\t\tstate=$b.state"
            if (b.symbolicName==camelCtx.name) {
                log << "\n................................................................................"
                Dictionary<String,String> d = b.headers
                d.keys().each{k->
                    String v = d.get(k)
                    log << "\n $k = $v"
                }
                log << "\n................................................................................"
            }
        }

        log as String
    }

    public void connectSftp(String host_sftp, Object cpiCred, boolean ignoreSSL = false) throws IOException {
        assert cpiCred != null
        assert cpiCred.getClass().getCanonicalName().startsWith('com.sap.it.api.')
        String uname = cpiCred.username
        char[] password = cpiCred.password
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
        cwd = dir
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
        sftpFiles.add(Paths.get(cwd, name))
    }

    public Path createTempFile(String fn) throws IOException {
        String[] p = fn.split("\\.")
        Path tmp = Files.createTempFile(p[0] + "_", "." + (p.length > 0 ? p[p.length-1] : ""))
        tempFiles[fn] = tmp
    }

    public void appendTempFile(String fn, String cnt) throws IOException {
        Path p = tempFiles[fn]
        if (!p) p = createTempFile(fn)
        OutputStream os = Files.newOutputStream(p, StandardOpenOption.APPEND)
        os.write(cnt.getBytes("UTF-8"))
        os.flush()
        os.close()
    }

    public void logHttpResponse(def headers, def response) throws IOException {
        if (!logHttp) return
        def log = "\u0474\u2697"*20<<""
        headers.each{k,v->
            log << "\n$k: $v"
        }
        log << "\n\n$response"
        putSftp("http_response_${httpRespCount}.txt" as String, log as String, true)
        httpRespCount++
    }

    public void logHttpResponse(CpiMsg msg) throws IOException {
        if (!logHttp) return
        logHttpResponse(msg.headers, msg.getBody(String))
    }

    public void filesToArchive(String archive) throws IOException {
        assert connectedSftp
        if (!connectedSftp) throw new RuntimeException("Not connected to SFTP")
        OutputStream w = sftp.put(archive, ChannelSftp.OVERWRITE)
        ZipOutputStream z = new ZipOutputStream(w)
        Iterator<Path> zz = sftpFiles.iterator()
        while(zz.hasNext()) {
            Path x = zz.next()
            String fn = x.fileName
            println "$x $fn"
            ZipEntry ze = new ZipEntry(fn)
            z.putNextEntry(ze)
            org.apache.commons.io.IOUtils.copy(sftp.get(fn), z)
            z.closeEntry()
            z.flush()
        }
        z.close()
        w.close()
    }

    public void tempToArchive(String archive) throws IOException {
        assert connectedSftp
        if (!connectedSftp) throw new RuntimeException("Not connected to SFTP")
        OutputStream w = sftp.put(archive, ChannelSftp.OVERWRITE)
        ZipOutputStream z = new ZipOutputStream(w)
        Iterator<Map.Entry<String,Path>> zz = tempFiles.iterator()
        while(zz.hasNext()) {
            Map.Entry<String,Path> x = zz.next()
            ZipEntry ze = new ZipEntry(x.key)
            z.putNextEntry(ze)
            if (Files.isRegularFile(x.value)) {
                org.apache.commons.io.IOUtils.copy(Files.newInputStream(x.value), z)
                Files.delete(x.value)
            }
            z.closeEntry()
            z.flush()
        }
        z.close()
        w.close()
    }
}

CpiMsg r002sftp(CpiMsg msg) {
    assert msg  //& msg.getClass() == Class.forName("com.sap.gateway.ip.core.customdev.util.Message")
    String host_sftp = msg.properties.Host_Sftp
    String uc_sftp = msg.properties.Credential_Sftp
    if (!host_sftp)
        throw new RuntimeException("Property Host_Sftp is not set")
    if (!uc_sftp)
        throw new RuntimeException("Property Credential_Sftp is not set")
    Class itApiFactory = Class.forName("com.sap.it.api.ITApiFactory")
    Class secureStoreService = Class.forName("com.sap.it.api.securestore.SecureStoreService")
//    Class keystoreService = Class.forName("com.sap.it.api.keystore.KeystoreService")
//    Class userCredential = Class.forName("com.sap.it.api.securestore.UserCredential")
    def sss = itApiFactory.getApi(secureStoreService, null)
    def uc = sss.getUserCredential(uc_sftp)

    org.apache.camel.CamelContext camelCtx = msg.exchange.context
    String messageId = msg.properties.SAP_MessageProcessingLogID
    String iflowId = camelCtx.name
    String ccts = msg.properties.CamelCreatedTimestamp.format("yyyy-MM-dd'T'HH.mm.ss.SSSXXX")
    String tenantName = System.properties['com.sap.it.node.tenant.name']

    Rsug002log r2d2 = new Rsug002log(msg)
    msg.properties.log002 = r2d2
    r2d2.connectSftp(host_sftp, uc, false)
    String fmt = msg.properties.Directory_Sftp ?: '/outgoing/%1$s/%2$s/%3$s'
    String dir = String.format(fmt, tenantName, iflowId, ccts)
    r2d2.mkdirCdSftp(dir)
    msg.properties.log002name = "${ccts}___${messageId}.zip" as String
    r2d2.putSftp ("mpl.txt", r2d2.mpl(msg))
    msg
}

CpiMsg r002finish(CpiMsg msg) {
    assert msg // && msg.getClass() == Class.forName("com.sap.gateway.ip.core.customdev.util.Message")
    Rsug002log r2d2 = msg.properties.log002
    if (!r2d2)
        throw new RuntimeException("No property `log002` set")
    else {
        Exception exc = msg.properties.CamelExceptionCaught
        if (r2d2.connectedSftp) {
//            r2d2.filesToArchive(msg.properties.log002name as String)
            r2d2.disconnectSftp()
        } else
            throw new RuntimeException("Not connected to SFTP")
    }
    msg
}


