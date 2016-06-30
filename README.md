# jmxstack

Pull stack traces over JMX remote port.

Yes this is a Java thing.

Add the following to eg. tomcat to be able to use this:

    -Dcom.sun.management.jmxremote.port=8990
    -Dcom.sun.management.jmxremote.ssl=false
    -Dcom.sun.management.jmxremote.authenticate=false

## Building

    ./gradlew clean
    ./gradlew jar

## Distribution

Copy `build/libs/jmxstack-1.0-SNAPSHOT.jar` to the destination of your choice.

## Running

    java -jar jmxstack.jar [host] [port]
