package io.rsug.socks5client;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.text.MessageFormat;
import java.util.Base64;

public class CCClient {
    private static String REGEX_SUBACCOUNT_NEO = "[a-f0-9]{7,10}";
    @NotNull
    private final String subaccountTechName;
    @Nullable
    private final String locationId;

    private final boolean neo, cf;

    private String uname = null;
    private char[] passwd = null;
    private SocketAddress addr = null;
    private Proxy proxy = null;
    private Authenticator authenticator = null;

    private CCClient(@NotNull String subaccountTechName, @Nullable String locationId, boolean neo, boolean cf) {
        this.subaccountTechName = subaccountTechName;
        this.locationId = locationId;
        this.neo = neo;
        this.cf = cf;
        if (neo) {
            addr = new InetSocketAddress("localhost", 20004);
            proxy = new Proxy(Proxy.Type.SOCKS, addr);
        }
    }

    public static CCClient fromNeo(@NotNull final String subaccountTechName, @Nullable final String locationId) {
        assert subaccountTechName.matches(REGEX_SUBACCOUNT_NEO) : "subaccountTechName must be not null and hex{7,10}";
        if (!subaccountTechName.matches(REGEX_SUBACCOUNT_NEO)) {
            throw new RuntimeException("Invalid subaccountTechName: " + subaccountTechName);
        }
        CCClient clnt = new CCClient(subaccountTechName, locationId, true, false);
        Base64.Encoder enc = Base64.getEncoder().withoutPadding();
        String a = enc.encodeToString(subaccountTechName.getBytes());
        String b = locationId != null ? enc.encodeToString(locationId.getBytes()) : "";
        clnt.uname = "1." + a + "." + b;
        clnt.passwd = new char[]{};

        clnt.authenticator = new Authenticator() {
            @Override
            protected java.net.PasswordAuthentication getPasswordAuthentication() {
                return new java.net.PasswordAuthentication(clnt.uname, clnt.passwd);
            }
        };
        return clnt;
    }

    public static CCClient fromNeo(@Nullable final String locationId) {
        return fromNeo(getTenantId(), locationId);
    }

    public static String getTenantName() {
        return System.getProperty("com.sap.it.node.tenant.name");
    }

    public static String getTenantId() {
        return System.getProperty("com.sap.it.node.tenant.id");
    }

    public static CCClient fromCF() {
        CCClient clnt = new CCClient("", "", false, true);
        clnt.uname = "none";
        clnt.passwd = new char[]{};
        clnt.authenticator = null;
        return clnt;
    }

    /**
     * @return
     */
    public Proxy getProxy() {
        assert authenticator != null && proxy != null;
        if (authenticator == null) {
            throw new RuntimeException("Authenticator is null");
        }
        if (proxy == null) {
            throw new RuntimeException("Proxy is null");
        }
        Authenticator.setDefault(authenticator);
        return proxy;
    }

    /**
     * Просто некоторый отладочный вывод
     *
     * @return
     */
    public String getInfo() {
        assert (neo && !cf) || (!neo && cf);
        String s = null;
        if (neo) {
            s = MessageFormat.format("SOCKS5 Neo subaccount={0} locationId={1}, uname={2} and pwd={3}", subaccountTechName, locationId, uname, new String(passwd));
        } else if (cf) {
            s = MessageFormat.format("SOCKS5 CF subaccount={0} locationId={1}", subaccountTechName, locationId);  //TODO
        }
        return s;
    }
}
