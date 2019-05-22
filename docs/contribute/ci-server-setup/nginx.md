## Nginx Proxy Configurations

We use nginx as a proxy to access all services running on our CI/Demo server.

Modifying NGINX proxy configuration files to redirect URLs on our demo/build server, can be done as follows:

0. All the config files are found on the demo VM in following directory:
/etc/nginx/sites-available

1. Download/Upload config files
    * Use SCP to copy files
      * from server: `scp ubuntu@54.88.202.24:/etc/nginx/sites-available/* .`
      * to server: `scp ./* ubuntu@54.88.202.24:/home/ubuntu/upload/sites-available`
    * On windows use putty, pageant and pscp (all from putty)
      * register the key for the VM with pageant
      * then use pscp in same way as scp (see above)

3. SSH to VM and copy the files from uploaded place to /etc/nginx/sites-available
> cd ~/upload/sites-available
> sudo cp ./<changed-file> /etc/nginx/sites-available/<changed-file>

4. Only in case of a new config file: Generate link for new server files in `sites-enabled`:
> sudo ln -s /etc/nginx/sites-available/<name-of-new-server-file> /etc/nginx/sites-enabled/

5. Restart the NGINX Server
> sudo service nginx restart
