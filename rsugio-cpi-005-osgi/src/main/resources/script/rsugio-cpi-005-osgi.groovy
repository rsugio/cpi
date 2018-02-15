/**
 * OSGi kovyralka
 *
 * @author Iliya Kuznetsov <iliya.kuznetsov@gmail.com>
 * @version 1.0.0
 * @date 2018-02-15
 * @see https://github.com/rsugio/cpi/tree/master/rsugio-cpi-005-osgi
 */

import com.sap.gateway.ip.core.customdev.util.Message as CpiMsg

CpiMsg r005osgi(CpiMsg msg) {
	msg.setBody("\u0474\u2697"*20)
	msg
}

