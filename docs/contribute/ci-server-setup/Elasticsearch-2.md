## Notes from Installation by @adiherzog

https://www.elastic.co/

Install version 2.x as described here for Ubuntu: https://www.elastic.co/guide/en/elasticsearch/reference/current/setup-repositories.html

wget -qO - https://packages.elastic.co/GPG-KEY-elasticsearch | sudo apt-key add -

echo "deb https://packages.elastic.co/elasticsearch/2.x/debian stable main" | sudo tee -a /etc/apt/sources.list.d/elasticsearch-2.x.list

sudo apt-get update && sudo apt-get install elasticsearch


Start Service: sudo /etc/init.d/elasticsearch start

Server available at: http://localhost:9200/

Install as a service that starts at server startup time -> Did not execute this
sudo update-rc.d elasticsearch defaults 95 10

Check service:

ubuntu@ip-172-31-33-58:/var/log/elasticsearch$ ls -l /etc/rc?.d/*elasticsearch
lrwxrwxrwx 1 root root 23 Oct 21 14:37 /etc/rc0.d/K10elasticsearch -> ../init.d/elasticsearch
lrwxrwxrwx 1 root root 23 Oct 21 14:37 /etc/rc1.d/K10elasticsearch -> ../init.d/elasticsearch
lrwxrwxrwx 1 root root 23 Oct 21 14:37 /etc/rc2.d/S95elasticsearch -> ../init.d/elasticsearch
lrwxrwxrwx 1 root root 23 Oct 21 14:37 /etc/rc3.d/S95elasticsearch -> ../init.d/elasticsearch
lrwxrwxrwx 1 root root 23 Oct 21 14:37 /etc/rc4.d/S95elasticsearch -> ../init.d/elasticsearch
lrwxrwxrwx 1 root root 23 Oct 21 14:37 /etc/rc5.d/S95elasticsearch -> ../init.d/elasticsearch
lrwxrwxrwx 1 root root 23 Oct 21 14:37 /etc/rc6.d/K10elasticsearch -> ../init.d/elasticsearch

Data Directory: /var/lib/elasticsearch/elasticsearch/nodes/0/indices


WARN  org.elasticsearch.client.transport: [Virgo] node {#transport#-1}{127.0.0.1}{localhost/127.0.0.1:9300} not part of the cluster Cluster [elasticsearch], ignoring...

=> Cluster must be named "elasticsearch"!

Show indices: curl http://localhost:9200/_cat/indices?v

## Elasticsearch as a service

https://www.elastic.co/guide/en/elasticsearch/reference/current/setup-service.html

sudo update-rc.d elasticsearch defaults 95 10
sudo /etc/init.d/elasticsearch start
