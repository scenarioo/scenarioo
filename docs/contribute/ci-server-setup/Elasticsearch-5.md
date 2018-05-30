## Install and Run with Docker

Install and run using docker:

```
docker pull docker.elastic.co/elasticsearch/elasticsearch:5.6.9
docker run -d -name elasticsearch5 -p 9205:9200 -p 9305:9300 -e cluster.name=elasticsearch -e xpack.ml.enabled=false -e xpack.security.enabled=false docker.elastic.co/elasticsearch/elasticsearch:5.6.9 
```

Set port values that do not conflict with other existing installation (e.g. Elasticsearch2 whoch is currently running in parallel)

## Start and Stop

```
docker stop elasticsearch5
```

```
docker start elasticsearch5
```

## Register as a System Service

Register as a service using the start script in ./servicescripts/elasticsearch5.

* Copy the script `elasticsearch5` to `/etc/init.d`
* chmod to be executable: `chmod a+x elasticsearch5`
* `service elasticsearch5 start`
* `service elasticsearch5 stop`
* Register the service to start on startup:
    ```
    sudo update-rc.d elasticsearch5 defaults 95 10
    sudo /etc/init.d/elasticsearch start
    ```
