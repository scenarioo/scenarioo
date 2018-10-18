## Install and Run with Docker

Install and run using docker:

```
docker pull docker.elastic.co/elasticsearch/elasticsearch:5.6.9
docker run -d --name elasticsearch5 -p 9200:9200 -p 9300:9300 -e cluster.name=elasticsearch -e xpack.ml.enabled=false -e xpack.security.enabled=false -e ES_JAVA_OPTS="-Xms512m -Xmx512m" docker.elastic.co/elasticsearch/elasticsearch:5.6.9 
```

Set port values that do not conflict with other existing installation

## Start and Stop

```
docker stop elasticsearch5
```

```
docker start elasticsearch5
```

## See Logs of Elasticsearch

To see logs in case of problems for debugging:

```
docker logs elasticsearch5
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
    sudo /etc/init.d/elasticsearch5 start
    ```

## Configure max_map_count

To avoid this error message: `max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]`

Temporary configuration: 

`sudo sysctl -w vm.max_map_count=262144`

Permanent configuration:

Change the value in `/etc/sysctl.conf` to `vm.max_map_count=262144`

Source: https://github.com/docker-library/elasticsearch/issues/111
