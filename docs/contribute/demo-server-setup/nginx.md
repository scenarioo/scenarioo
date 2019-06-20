## Nginx Proxy Configurations

We use nginx as a proxy to access all services running on our CI/Demo server.

Modifying NGINX proxy configuration files to redirect URLs on our demo/build server, can be done as follows:

1. Edit [scenarioo-infrastructure\roles\nginx\templates\demo.scenarioo.org.conf.j2](https://github.com/scenarioo/scenarioo-infrastructure/blob/master/roles/nginx/templates/demo.scenarioo.org.conf.j2)
2. Commit changes to master branch using a pull request
3. Wait for Circle CI build to deploy the new server with configuration.
