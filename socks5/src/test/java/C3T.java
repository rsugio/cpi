import io.rsug.socks5client.CCClient;
import org.junit.Test;

import java.net.Proxy;

public class C3T {
    @Test
    public void simple() {
        CCClient c = CCClient.fromNeo("fff8f0836", "sample");
        System.out.println(c.getInfo());
        CCClient c2 = CCClient.fromNeo("fff8f0836", "");
        System.out.println(c2.getInfo());
        CCClient c3 = CCClient.fromNeo("fff8f0836", null);
        System.out.println(c3.getInfo());
        CCClient f1 = CCClient.fromCF();
        System.out.println(f1.getInfo());
    }

    @Test
    public void proxy() {
        CCClient c = CCClient.fromNeo("fff8f0836", "sample");
        Proxy p = c.getProxy();
        System.out.println(c.getInfo() + "\t" + p);
    }
}
