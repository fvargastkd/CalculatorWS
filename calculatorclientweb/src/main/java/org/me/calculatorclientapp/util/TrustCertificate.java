package org.me.calculatorclientwso2.util;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TrustCertificate {

    private static SSLSocketFactory sslSocketTrust = null;
    private static HostnameVerifier hostVerifierTrust = null;
    private static SSLContext sslContextTrust = null;

    public static SSLContext getSSLContext() {
        try {
            getSslSocketTrust();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return sslContextTrust;
    }

    private static SSLSocketFactory getSslSocketTrust() throws NoSuchAlgorithmException, KeyManagementException {
        if (TrustCertificate.sslSocketTrust == null) {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(final java.security.cert.X509Certificate[] arg0, final String arg1) throws CertificateException {
                }

                public void checkServerTrusted(final java.security.cert.X509Certificate[] arg0, final String arg1) throws CertificateException {
                }
            }};
            sslContextTrust = SSLContext.getInstance("SSL");
            sslContextTrust.init(null, trustAllCerts, new java.security.SecureRandom());
            TrustCertificate.sslSocketTrust = sslContextTrust.getSocketFactory();
        }
        return TrustCertificate.sslSocketTrust;
    }

    public static void allowAllCerts() {
        final SSLSocketFactory sslSocket = HttpsURLConnection.getDefaultSSLSocketFactory();
        try {
            if (sslSocket != TrustCertificate.getSslSocketTrust()) {
                HttpsURLConnection.setDefaultSSLSocketFactory(TrustCertificate.getSslSocketTrust());
                TrustCertificate.allowHosts();
            }
        } catch (final KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the hostVerifierTrust
     */
    private static HostnameVerifier getHostVerifierTrust() {
        if (TrustCertificate.hostVerifierTrust == null) {
            TrustCertificate.hostVerifierTrust = new HostnameVerifier() {
                public boolean verify(final String hostname, final SSLSession session) {
                    return true;
                }
            };

        }
        return TrustCertificate.hostVerifierTrust;
    }

    private static void allowHosts() {
        final HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
        if (hostnameVerifier != TrustCertificate.getHostVerifierTrust()) {
            HttpsURLConnection.setDefaultHostnameVerifier(TrustCertificate.getHostVerifierTrust());
        }
    }

}
