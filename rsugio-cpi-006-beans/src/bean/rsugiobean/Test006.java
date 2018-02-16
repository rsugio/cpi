package rsugiobean;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class Test006 implements Processor {

    public void process(Exchange exchange) throws Exception {
        Thread.currentThread();
        Thread.sleep(10L);
    }

    public String echo(String in) throws Exception {
        return in;
    }

    public String sleep(long ms) throws Exception {
        Thread.sleep(ms);
        return "Slept for " + ms + "ms";
    }

}
