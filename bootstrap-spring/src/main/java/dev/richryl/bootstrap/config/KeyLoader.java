package dev.richryl.bootstrap.config;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;

import java.security.interfaces.RSAPublicKey;

import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyLoader {


    public static RSAPublicKey loadPublicKey() throws Exception {
        String pem;
        try (InputStream is = KeyLoader.class.getResourceAsStream("/public-key.pem")) {
            assert is != null;
            pem = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            String publicKeyPEM = pem
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll("\\R", "")
                    .replace("-----END PUBLIC KEY-----", "");

            byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            return (RSAPublicKey) kf.generatePublic(keySpec);
        }

    }
}