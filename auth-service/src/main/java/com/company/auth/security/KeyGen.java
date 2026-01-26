package com.company.auth.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class KeyGen {
    public static String generate() {
        return (
            java.util.Base64.getEncoder()
                .encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded())
        );
    }
}
