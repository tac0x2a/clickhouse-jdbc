
ClickHouse JDBC experimentally driver
===============

This is a basic and restricted implementation of jdbc driver for ClickHouse.
It has support of a minimal subset of features to be usable.

This project was forked from [yandex/clickhouse-jdbc](https://github.com/yandex/clickhouse-jdbc) for publishing jar file that contain experimentally features before merge to original.

### Usage
#### Maven

pom.xml
```xml
...
<dependency>
  <groupId>net.tac42.clickhouse</groupId>
  <artifactId>clickhouse-jdbc-exp</artifactId>
  <version>0.1.42</version>
  <type>pom</type>
</dependency>
...
```

settings.xml
```xml
    ...
    <profiles>
        <profile>
            <repositories>
                <repository>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                    <id>bintray-tac0x2a-click-house-jdbc-exp</id>
                    <name>bintray</name>
                    <url>https://dl.bintray.com/tac0x2a/click-house-jdbc-exp</url>
                </repository>
            </repositories>
            <pluginRepositories>
                <pluginRepository>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                    <id>bintray-tac0x2a-click-house-jdbc-exp</id>
                    <name>bintray-plugins</name>
                    <url>https://dl.bintray.com/tac0x2a/click-house-jdbc-exp</url>
                </pluginRepository>
            </pluginRepositories>
            <id>bintray</id>
        </profile>
    </profiles>
    <activeProfiles>
        <activeProfile>bintray</activeProfile>
    </activeProfiles>
    ...
```

#### Gradle

build.gradle
```groovy
repositories {
    ...
    maven {
        url "https://dl.bintray.com/tac0x2a/click-house-jdbc-exp"
    }
    ...
}
...
dependencies {
    ...
    compile "net.tac42.clickhouse:clickhouse-jdbc-exp:0.1.42"
    ...
}
```

### Code
URL syntax:
`jdbc:clickhouse://<host>:<port>[/<database>]`, e.g. `jdbc:clickhouse://localhost:8123/test`

JDBC Driver Class:
`net.tac42.clickhouse.ClickHouseDriver`

additionally, if you have a few instances, you can use `BalancedClickhouseDataSource`.

### Compiling with maven
The driver is built with maven.
`mvn package -DskipTests=true`

To build a jar with dependencies use

`mvn package assembly:single -DskipTests=true`

### Build requirements
In order to build the jdbc client one need to have jdk 1.6 or higher.
