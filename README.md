# spring-boot-aws-docdb
AWS Document DB integration with spring data mongodb repository.

# Environment
-DsslKeyStore=PATH TO SSL KEYSTORE

Pass the property above to VM Options, or when running project: java -jar target/*.jar -DsslKeyStore=PATH TO SSL KEYSTORE

# Generate keystore from rds-combined-ca-bundle.pem
## Linux
```
./generateKeyStore-linux.sh

```

## Macos

```
./generateKeyStore-macos.sh

```

The command above will generate new keystore from rds-combined-ca-bundle.pem, the keystore file will be saved default in `sslKeyStore` 

# Run Project locally
After the step above, you were able to run the project with spring boot. 

```
mvn clean install
java -jar target/*.jar -DsslKeyStore=PATH TO KEYSTORE
```

# JIB configuration
We use JIB to build and deploy docker image to ECR or ACR. 
## Copy keystore into root folder of docker container /
```
    <extraDirectories>
          <paths>
               <path>sslKeyStore</path>
          </paths>
    </extraDirectories>
```

## Insert JVM Arg to entrypoint
```
    <container>
         <jvmFlags>
              <jvmFlag>-DsslKeyStore=/rds-truststore.jks</jvmFlag>
         </jvmFlags>
   </container>
```

# Jenkins and Helm chart deploy (Optional)
You can set up Jenkins and helm chart to run the docker image in AWS EKS cluster. 



[AWS DocumentDB - Connecting Programmatically](https://docs.aws.amazon.com/documentdb/latest/developerguide/connect_programmatically.html)
