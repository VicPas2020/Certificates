package com.example.demo;

import io.micrometer.core.annotation.Timed;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@RestController
@Timed("certificates")
public class CertificateController {

    private final Gauge gauge;

    public CertificateController(CollectorRegistry collectorRegistry) {
        gauge = Gauge.build()
                .name("certificates")
                .help("certificates info")
                .register(collectorRegistry);
    }

    @GetMapping("/certs")
    public long certExpiration() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        KeyStore ks = KeyStore.getInstance("JKS");
        char[] pwdArray = "changeit".toCharArray();

        ks.load(new FileInputStream("C:\\java\\keystore.jks"), pwdArray);
        X509Certificate cert = (X509Certificate) ks.getCertificate("java");
        Date notAfter = cert.getNotAfter();

        Duration between = Duration.between(Instant.now(), notAfter.toInstant());
        gauge.set(between.toDays());
        return between.toDays();
    }
}