# How to Install Bakong SDK Manually

## If you have the JAR file:

1. Download the Bakong KHQR SDK JAR file from the official source
2. Install it to your local Maven repository:

```bash
mvn install:install-file \
  -Dfile=path/to/bakong-khqr-sdk.jar \
  -DgroupId=kh.gov.nbc.bakong_khqr \
  -DartifactId=sdk-java \
  -Dversion=1.0.0.15 \
  -Dpackaging=jar
```

## If you don't have the JAR file:

Contact the National Bank of Cambodia or your payment provider to get:
1. The Bakong KHQR SDK JAR file
2. Or the Maven repository URL where it's hosted
3. Your Bakong merchant credentials

## Alternative: Add NBC Maven Repository (if available)

If NBC provides a Maven repository, add this to your pom.xml before the dependencies section:

```xml
<repositories>
    <repository>
        <id>nbc-bakong</id>
        <name>NBC Bakong Repository</name>
        <url>https://maven.nbc.gov.kh/repository/bakong/</url>
    </repository>
</repositories>
```

Replace the URL with the actual NBC Maven repository URL.

