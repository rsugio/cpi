package sample;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(SampleProcessor.class);
//    private Utils utils = new Utils();

    public void process(Exchange exchange) throws Exception {
        String name = exchange.getContext().getName();
//        String str = this.utils.dumpExchange(exchange);
//        this.utils.saveFile(name + "_log", str.getBytes());
    }

    public void logIn(Exchange exchange) throws Exception {
        String name = exchange.getContext().getName();
        StringBuffer sb = new StringBuffer();
        sb.append(exchange.getIn().toString());
//        this.utils.dumpMessage(exchange.getIn(), sb);
//        this.utils.saveFile(name + "_in_log", sb.toString().getBytes());
    }

    public void logOut(Exchange exchange) throws Exception {
        String name = exchange.getContext().getName();
        StringBuffer sb = new StringBuffer();
        sb.append(exchange.getOut().toString());
//        this.utils.dumpMessage(exchange.getOut(), sb);
//        this.utils.saveFile(name + "_out_log", sb.toString().getBytes());
    }

    public void logProperties(Exchange exchange) throws Exception {
        String name = exchange.getContext().getName();
        StringBuffer sb = new StringBuffer();
//        this.utils.dumpMap(exchange.getProperties(), sb);
//        this.utils.saveFile(name + "_props_log", sb.toString().getBytes());
    }

    public void logHeaders(Exchange exchange) throws Exception {
        String name = exchange.getContext().getName();
        StringBuffer sb = new StringBuffer();
//        this.utils.dumpMap(exchange.getIn().getHeaders(), sb);
//        this.utils.saveFile(name + "_headers_log", sb.toString().getBytes());
    }

}

