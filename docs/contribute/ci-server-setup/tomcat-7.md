# Tomcat 7

## Installation

Install:

```
sudo apt-get install tomcat7 tomcat7-admin
```

Jenkins runs under user `ubuntu:ubuntu` so Tomcat has to run with the same use in order to be able to read the files written by Jenkins.

```
sudo chown -R ubuntu:ubuntu /var/log/tomcat7
sudo chown -R ubuntu:ubuntu /usr/share/tomcat7
sudo chown -R ubuntu:ubuntu /usr/share/tomcat7
sudo chown -R ubuntu:ubuntu /var/lib/tomcat7
sudo chown -R ubuntu:ubuntu /etc/tomcat7
```

See also: http://askubuntu.com/questions/371809/run-tomcat7-as-tomcat7-or-any-other-user

Add existing user to group (this is probably not necessary...):

```
sudo usermod -a -G ubuntu tomcat7
```

http://askubuntu.com/questions/371809/run-tomcat7-as-tomcat7-or-any-other-user
- 

## Directories

Bin: /usr/share/tomcat7/bin

Config: /etc/tomcat7

Contexts: /etc/tomcat7/Catalina/localhost

Logs: /var/log/tomcat7

Webapps: /var/lib/tomcat7/webapps

Scenarioo documentation data: /var/lib/scenarioo/scenarioDocuExample-...


## Usage

Restart:

```
sudo service tomcat7 restart
```

There's also an alias for that:

```
sc-tomcat-restart
```

## Java Version used by Tomcat

```
sudo vim /etc/default/tomcat7
```

Change this line:

```
JAVA_HOME=/usr/lib/jvm/java-7-oracle
```

Also set `JAVA_HOME` in `.bashrc` to Java 8.

## Change Memory

Increase to 2 GB in file /etc/default/tomcat7:

```
JAVA_OPTS="-Djava.awt.headless=true -Xms1024m -Xmx2048m -XX:MaxPermSize=1024m -XX:+UseConcMarkSweepGC"
```
