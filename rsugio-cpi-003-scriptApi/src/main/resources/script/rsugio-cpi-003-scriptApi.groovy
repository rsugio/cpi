/**
 * Some scriptApi 
 *
 * @author Iliya Kuznetsov <iliya.kuznetsov@gmail.com>
 * @version 1.0.1
 * @date 2018-01-31
 * @see https://github.com/rsugio/cpi/tree/master/rsugio-cpi-003-scriptApi
 */

import com.sap.gateway.ip.core.customdev.util.Message as CpiMsg

CpiMsg r003scriptApi(CpiMsg msg) {
	def log = "\u0474\u2697"*20<<""
	log << "\ncom.sap.it.api.ITApiFactory:\t${com.sap.it.api.ITApiFactory}"
	com.sap.it.api.securestore.SecureStoreService sss = com.sap.it.api.ITApiFactory.getApi(com.sap.it.api.securestore.SecureStoreService.class, null)
	log << "\ncom.sap.it.api.securestore.SecureStoreService:\t$sss"

	if (msg.headers.Credential) {
		log << "\n\nCredential: $msg.headers.Credential"
		com.sap.it.api.securestore.UserCredential uc = sss.getUserCredential(msg.headers.Credential as String)
		log << "\ncom.sap.it.api.securestore.UserCredential:\t$uc"
		log << "\nUserCredential properties:\t${uc.credentialProperties}"
		log << "\nUserCredential username:\t${uc.username}"
		log << "\nUserCredential password:\t${uc.password}"
		log << "\n"+"-"*40
	}

	com.sap.it.api.keystore.KeystoreService ks = com.sap.it.api.ITApiFactory.getApi(com.sap.it.api.keystore.KeystoreService.class, null)
	log << "\n\ncom.sap.it.api.keystore.KeystoreService:\t$ks"
	log << "\ncom.sap.it.api.keystore.KeystoreService javax.net.ssl.KeyManager:\t${ks.keyManager}"
	log << "\ncom.sap.it.api.keystore.KeystoreService javax.net.ssl.KeyManagers:\t${ks.keyManagers}"
	log << "\ncom.sap.it.api.keystore.KeystoreService javax.net.ssl.TrustManager:\t${ks.trustManager}"
	log << "\ncom.sap.it.api.keystore.KeystoreService javax.net.ssl.TrustManagers:\t${ks.trustManagers}"
	
	if (msg.headers.Keystore) {
		log << "\n\nKeystore: $msg.headers.Keystore"
		log << "\n"+"-"*40
	}

	msg.setBody(log as String)	
	msg                                
}

CpiMsg r003scriptApiReflection(CpiMsg msg) {
	def log = "\u0474\u2697"*20<<""

	Class itApiFactory = Class.forName("com.sap.it.api.ITApiFactory")
	Class secureStoreService = Class.forName("com.sap.it.api.securestore.SecureStoreService")
	Class keystoreService = Class.forName("com.sap.it.api.keystore.KeystoreService")
	Class userCredential = Class.forName("com.sap.it.api.securestore.UserCredential")
	
	log << "\nclass com.sap.it.api.ITApiFactory:\t$itApiFactory"
	log << "\nclass com.sap.it.api.securestore.SecureStoreService:\t$secureStoreService"
	log << "\nclass com.sap.it.api.keystore.KeystoreService:\t$keystoreService"
	log << "\nclass com.sap.it.api.securestore.UserCredential:\t$userCredential"

	def sss = itApiFactory.getApi(secureStoreService, null)
	log << "\ncom.sap.it.api.securestore.SecureStoreService:\t$sss"
	if (msg.headers.Credential) {
		log << "\n\nCredential: $msg.headers.Credential"
		def uc = sss.getUserCredential(msg.headers.Credential as String)
		log << "\ncom.sap.it.api.securestore.UserCredential:\t$uc"
		log << "\nUserCredential properties:\t${uc.credentialProperties}"
		log << "\nUserCredential username:\t${uc.username}"
		log << "\nUserCredential password:\t${uc.password}"
		log << "\n"+"-"*40
	}
	msg.setBody(log as String)	
	msg                                
}
