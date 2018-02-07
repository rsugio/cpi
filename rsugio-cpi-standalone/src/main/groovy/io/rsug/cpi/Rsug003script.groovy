package io.rsug.cpi
import com.sap.gateway.ip.core.customdev.util.Message as CpiMsg

CpiMsg m1(CpiMsg msg) {
    msg.headers.clear()
    msg
}
