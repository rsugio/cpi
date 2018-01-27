import io.rsug.cpi.Rsug002log
import org.junit.Test

class Test002Log {

    @Test
    void sftp18() {
        Properties p = new Properties()
        InputStream is = getClass().getResourceAsStream("sftp18.properties.secure")
        assert is : "You have to create test resource sftp18.properties.secure file"
        p.load(is)
        Rsug002log logger = new Rsug002log()
        String pwd = p.sftp_pwd
        boolean strict = Boolean.valueOf(p.sftp_strictSSL).booleanValue()
        println "sftp://$p.sftp_uname@$p.sftp_host/$p.sftp_dir, strict=$strict"
        logger.connectSftp(p.sftp_host, p.sftp_uname, pwd.bytes, strict)
//        logger.chdirSftp(p.sftp_dir)
        logger.disconnect()

    }
}
