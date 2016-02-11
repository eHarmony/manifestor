[![Build Status](https://travis-ci.org/eHarmony/manifestor.svg?branch=master)](https://travis-ci.org/eHarmony/manifestor)

manifestor
===========

Library that contains common code for enhancing RESTFul Services with Manifest Data.

If you want to use Manifestor in your service, here is how Maven dependency should look like:
``` xml
<dependency>
    <groupId>com.eharmony</groupId>
    <artifactId>manifestor</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

You can either use *ManifestUtil* directly inside your custom endpoint, or you
can wire *ApplicationInfoResource*, if you have Dropwizard or Jersey web-service.

**Usage example:**
``` xml
final Package currentPackage = getClass().getPackage();

If you are using Dropwizard, or similar framework.
final ApplicationInfoResource appInfoResource = new ApplicationInfoResource(currentPackage.getImplementationTitle());
environment.jersey().register(appInfoResource);

If your service is using Jersey load the resource by including this inside the WEB-INF/web.xml:
<servlet>
    <servlet-name>jersey</servlet-name>
    <servlet-class>com.sun.jersey.spi.spring.container.servlet.SpringServlet</servlet-class>
    <init-param>
        <param-name>com.sun.jersey.config.property.packages</param-name>
        <param-value>manifestor</param-value>
    </init-param>
</servlet>
```

Manifest files are generally located under *META-INF/MANIFEST.MF* in your artifact (JAR, WAR, EAR), however they do not contain
complete data and if you are using Maven for artifact creation, it can be easily enhanced with more entries like in the following example:

``` xml
<!-- Example of embedding the Manifest inside JAR -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <version>2.4</version>
    <configuration>
        <archive>
            <manifest>
                <addDefaultImplementationEntries>true</addDefaultImplementationEntries>
            </manifest>
        </archive>
        <finalName>${project.artifactId}-${project.version}</finalName>
    </configuration>
</plugin>

<!-- Example of embedding the Manifest inside WAR -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-war-plugin</artifactId>
    <version>2.6</version>
    <configuration>
        <archive>
            <manifest>
                <addDefaultImplementationEntries>true</addDefaultImplementationEntries>
                <addClasspath>true</addClasspath>
            </manifest>
        </archive>
    </configuration>
</plugin>
```