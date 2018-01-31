import com.sap.gateway.ip.core.customdev.util.Message as CpiMsg
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

/**
 * When your IFlow gets running in CPI environment, you are in need of detecting some parameters.
 * Here below is the code for system environment reporting.
 *
 * @author Iliya Kuznetsov <iliya.kuznetsov@gmail.com>
 * @version 1.0.1
 * @date 2018-01-26
 */

/**
 * Show version and environment information; just plain text and human readable
 *
 * @param msg Incoming message (not used)
 * @return Outgoing message with plain text
 */
CpiMsg r001showVersion(CpiMsg msg) {
	Map<String,Object> ps = msg.properties
	org.apache.camel.Exchange camelExch = msg.exchange
	org.apache.camel.CamelContext camelCtx = camelExch.context
	org.osgi.framework.BundleContext osgiCtx = org.osgi.framework.FrameworkUtil.getBundle(msg.class).bundleContext
	String ccts = ps.CamelCreatedTimestamp.format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	com.sap.it.op.agent.mpl.MplConfiguration mpc = ps.SAP_MessageProcessingLogConfiguration	
	String iflmapNode = camelExch.exchangeId.split("-")[1]
	def a = ""<<""
	osgiCtx.bundles.each{org.osgi.framework.Bundle b ->
		if (b.symbolicName.startsWith('org.apache.http')) a << "\n$b.symbolicName:\t$b.version"
	}

	def log = "\u0474\u2697"*20<<""
	log << "\n@author Iliya Kuznetsov"
	log << "\n@version 1.0.2\n@date 2018-01-31"
	log << "\n@see https://github.com/rsugio/cpi/tree/master/rsugio-cpi-001-showVersion\n"
    log << "\nJava version:\t${System.properties['java.version']}"
    log << "\nGroovy version:\t${GroovySystem.version}"
	log << "\nCamel version:\t$camelCtx.version"
	log << "\nCamel uptime:\t$camelCtx.uptime"
	log << "\nIFLMAP node:\t$iflmapNode"
	log << "\ncom.sap.it.node.tenant.name:\t${System.properties['com.sap.it.node.tenant.name']}"
	log << "\ncom.sap.it.node.tenant.id:\t${System.properties['com.sap.it.node.tenant.id']}"
	log << "\ncom.sap.it.node.vmsize:\t${System.properties['com.sap.it.node.vmsize']}"
	
	log << a
	
	log << "\n\nIFlow id:\t$camelCtx.name"
	log << "\nCamelExchange pattern:\t$camelExch.pattern"
	log << "\nCamelCreatedTimestamp:\t$ccts"
	log << "\nSAP_MessageProcessingLogID:\t$ps.SAP_MessageProcessingLogID\t\t//use it for navigation on MPLs. Somewhere it's MessageGuid"

	log << "\nCamelExchange id:\t$camelExch.exchangeId"
	log << "\nMessage.bodySize:\t$msg.bodySize"
	log << "\nMessage.attachmentsSize:\t$msg.attachmentsSize"
	log << "\nMessage.attachmentWrapperObjects:\t$msg.attachmentWrapperObjects"

//	com.sap.it.op.mpl.impl.MessageProcessingLogChildPartImpl SAP_MessageProcessingLog = ps['SAP_MessageProcessingLog']
//	log << "\n${SAP_MessageProcessingLog.getClass()}"

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
//	    String bn = b.headers.get('Bundle-Name')
//	    log << "\nBundle-Name=$bn"
	}

	msg.setBody(log as String)
	msg.setHeader('Content-Type', 'text/plain;charset=UTF-8')
	msg
}

CpiMsg r001showVersionReflection(CpiMsg msg) {
	def log = "\u0474\u2697"*20<<""
	log << "\n@author Iliya Kuznetsov"
	log << "\n@version 1.0.2\n@date 2018-01-31"
	log << "\n@see https://github.com/rsugio/cpi/tree/master/rsugio-cpi-001-showVersion\n"
    log << "\nJava version:\t${System.properties['java.version']}"
    log << "\nGroovy version:\t${GroovySystem.version}"
	org.apache.camel.Exchange camelExch = msg.exchange
	org.apache.camel.CamelContext camelCtx = camelExch.context

	log << "\n\nIFlow id:\t$camelCtx.name"
	String ccts = msg.properties.CamelCreatedTimestamp.format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	log << "\nCamelCreatedTimestamp:\t$ccts"

	msg.setBody(log as String)
	msg.setHeader('Content-Type', 'text/plain;charset=UTF-8')
	msg
}
