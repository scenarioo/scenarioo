## Install and Run with Docker

Install and run using docker:
```
docker pull docker.elastic.co/elasticsearch/elasticsearch:5.6.9
docker run -d -name elasticsearch5 -p 9205:9200 -p 9305:9300 -e cluster.name=elasticsearch -e xpack.ml.enabled=false -e xpack.security.enabled=false docker.elastic.co/elasticsearch/elasticsearch:5.6.9 
```

Set port values that do not conflict with other existing installation (e.g. Elasticsearch2 whoch is currently running in parallel)

## Register as a System Service

Register as a service using the start script in ./servicescripts/elasticsearch5.

* Copy the script to /etc/init.d
* chmod to be executable
* service start elasticsearch5
