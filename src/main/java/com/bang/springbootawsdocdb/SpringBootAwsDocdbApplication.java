package com.bang.springbootawsdocdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import java.lang.invoke.MethodHandles;

@SpringBootApplication
public class SpringBootAwsDocdbApplication {

    public static void main(String[] args) {
        SSLContextHelper.setSslProperties();
        SpringApplication.run(SpringBootAwsDocdbApplication.class, args);
    }

    protected static class SSLContextHelper {
        private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
        private static final String SSL_TRUST_STORE = "javax.net.ssl.trustStore";
        private static final String SSL_TRUST_STORE_PASSWORD = "javax.net.ssl.trustStorePassword";
        private static final String SSL_TRUST_STORE_TYPE = "javax.net.ssl.trustStoreType";
        private static final String KEY_STORE_TYPE = "JKS";
        private static final String DEFAULT_KEY_STORE_PASSWORD = "changeit";
        private static final String DEFAULT_KEYSTORE = "rds-truststore.jks";
        private static final String SSL_KEYSTORE = "sslKeyStore";


        private static void setSslProperties() {
            try {
                String sslKeyStore= System.getProperty(SSL_KEYSTORE);
                logger.info("-DsslKeyStore={}", sslKeyStore);
                if(StringUtils.isEmpty(sslKeyStore)) {
                    sslKeyStore= DEFAULT_KEYSTORE;
                }
                logger.info("ssl keystore path: {}", sslKeyStore);
                System.setProperty(SSL_TRUST_STORE, sslKeyStore);
                System.setProperty(SSL_TRUST_STORE_TYPE, KEY_STORE_TYPE);
                System.setProperty(SSL_TRUST_STORE_PASSWORD, DEFAULT_KEY_STORE_PASSWORD);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
