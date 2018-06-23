package com.github.nexus.node;

import com.github.nexus.node.model.ClientAuthMode;
import com.github.nexus.node.model.TrustMode;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class ClientFactory {

    private static Client buildInsecureClient(){
        return ClientBuilder.newClient();
    }

    private static Client buildSecureClient(String keyStore, String keyStorePassword, String trustStore,
                                           String trustStorePassword, String trustMode, String knownServers)
        throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException,
        KeyStoreException, KeyManagementException, IOException {

        final SSLContext sslContext = TrustMode
            .getValueIfPresent(trustMode)
            .orElse(TrustMode.NONE)
            .createSSLContext(keyStore,keyStorePassword,trustStore,trustStorePassword,knownServers);

        return  ClientBuilder.newBuilder()
            .sslContext(sslContext)
            .build();
    }

    public static Client buildClient(String secure, String keyStore, String keyStorePassword, String trustStore,
                                     String trustStorePassword, String trustMode, String knownServers) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        if (ClientAuthMode.strict == ClientAuthMode.valueOf(secure)){
            return buildSecureClient(keyStore, keyStorePassword, trustStore, trustStorePassword, trustMode, knownServers);
        }
        else {
            return buildInsecureClient();
        }
    }


}
