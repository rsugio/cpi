import io.rsug.cpi.Rsug002log
import org.junit.Before
import org.junit.Test

import java.nio.file.Files
import java.nio.file.Path

class Test002Log {
    Properties sftp18 = null
    byte[] b = [1,2,3,4,5,6,7,8,9,10,11,12,0,-1,-2,-3]*13
    @Before
    void init() {
        this.sftp18 = new Properties()
        InputStream is = getClass().getResourceAsStream("sftp18.properties.secure")
        assert is : "You have to create test resource sftp18.properties.secure file"
        sftp18.load(is)
    }

    @Test
    void throughSFTP() {
        Rsug002log logger = new Rsug002log()
        String pwd = sftp18.sftp_pwd
        boolean strict = Boolean.valueOf(sftp18.sftp_strictSSL).booleanValue()
        println "sftp://$sftp18.sftp_uname@$sftp18.sftp_host/$sftp18.sftp_dir, strict=$strict"
        logger.connectSftp(sftp18.sftp_host, sftp18.sftp_uname, pwd.bytes, strict)
        logger.mkdirCdSftp(sftp18.sftp_dir)
        logger.putSftp("azaza.txt", "1\n2\n3\nРусские", false)
        logger.putSftp("azaza.txt", "1\n2\n3\nРусские2", true)

        logger.putSftp("azaza2.txt", b, true)
        logger.putSftp("azaza2.txt", b, false)
        logger.putSftp("azaza2.txt", b, true)
        logger.disconnectSftp()
    }

    @Test
    void throughSFTP2() {
        Rsug002log logger = new Rsug002log()
        logger.connectSftp(sftp18.sftp_host, sftp18.sftp_uname, sftp18.sftp_pwd.bytes, false)
        logger.mkdirCdSftp("/outgoing/test")
        logger.putSftp("file1.txt", "СодержимоеУникод")
        logger.putSftp("file2.txt", "СодержимоеУникод-2")
        logger.filesToArchive("archive.zip")
        logger.disconnectSftp()
    }

    @Test
    void throughTempFile() {
        Rsug002log logger = new Rsug002log()
        Object x = logger.createTempFile("1_.txt")
        println x.getClass()
        BufferedWriter b1 = Files.newBufferedWriter(x)
        BufferedWriter b2 = Files.newBufferedWriter(logger.createTempFile("2_.txt"))
        b1.write("123\n")
        b1.write("123\n")
        b1.write("123")
        b1.close()
        b2.write("123\n\n_Ыыы")
        b2.close()

        logger.connectSftp(sftp18.sftp_host, sftp18.sftp_uname, sftp18.sftp_pwd.bytes, Boolean.valueOf(sftp18.sftp_strictSSL).booleanValue())
        logger.mkdirCdSftp(sftp18.sftp_dir)
        logger.tempToArchive("azaza_1234567.zip")

        logger.disconnectSftp()
    }

    @Test
    void z() {
        Path tmp = Files.createTempFile("a_", ".txt")
        OutputStream w = Files.newOutputStream(tmp)
        (1..500000).each{
            w.write(45)
        }
        w.close()
        println tmp
    }

    @Test
    void deleteTempFiles() {
        java.nio.file.Path p = java.nio.file.Paths.get("C:\\Temp\\")
        java.nio.file.DirectoryStream<Path> directoryStream = Files.newDirectoryStream(p)
        directoryStream.each {
            println it
        }
    }
}
