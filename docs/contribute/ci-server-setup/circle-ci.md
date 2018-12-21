# Circle CI

## Configuration

* We are on an open-source plan of Circle CI with up to 4 instances in parallel
* All the config takes place in `./circleci/config.yml`
* Copy everything you want to keep into `./test-results`. These files will be persisted as artifacts
* There you can also find the logging output of the scenarioo server: `./test-results/scenarioo-server.log`

## Environment vars - Context

In Circle CI environment vars are grouped in contexts. We store all variables in the context `Scenarioo`. It can be modified here: https://circleci.com/gh/organizations/scenarioo/settings#contexts

The following variables are needed:
 * `TOMCAT_USER_PASSWORD`: Used to secure the publish scenarioo docu endpoint. Defaults to: 'scenarioo' and user is always 'scenarioo'.
 * `CIRLCE_TOKEN`: Used to download WAR and scenarioo docu artifacts from CircleCI
 * `DOCU_GIT_USERPASS`: `user:password` Used to publish our docu with gitbook.
 
## SSH Keys

We need a commit key pair and a deploy key pair for our infrastructure to work.

### Commit key pair

To commit to the `scenarioo-infrastructure` repository we need to configure an SSH key. 

* Generate one with: `ssh-keygen -t rsa -b 4096 -C "scenarioo ci"`
* Add the private key on Circle CI: https://circleci.com/gh/scenarioo/scenarioo/edit#ssh
* Add public key on Github to allow commits: https://github.com/scenarioo/scenarioo-infrastructure/settings/keys
* Add the fingerprint of the SSH key to the deploy job in `.circleci/config.yml` of the `scenarioo/scenarioo` repository

### Deploy key pair

To deploy the server using ansible we need an authorized key pair of the host we want to deploy to.

* Add the private key here: https://circleci.com/gh/scenarioo/scenarioo-infrastructure/edit#ssh
* Add the fingerprint of the SSH key to the deploy job in `.circleci/config.yml` of the `scenarioo/scenarioo-infrastructure` repository


## Debugging

* Biggest problem during setup were memory issues
* If processes die for no reason always suspect not enough memory. Log the exit code after running the commend:
`your command || echo "Exit code: $?"`
* If the exit code is `137` you are running out of memory
    * Run gradle with `--no-daemon` it eats more memory than Jabba the Hutt
    * You can SSH into your build and debug memory directly: https://circleci.com/docs/2.0/ssh-access-jobs/
        * `ps -eo size,pid,user,command --sort -size | more`
        * `ps -eo size,pid,user,command --sort -size | awk '{ hr=$1/1024 ; printf("%13.2f Mb ",hr) } { for ( x=4 ; x<=NF ; x++ ) { printf("%s ",$x) } print "" }' |cut -d "" -f2 | cut -d "-" -f1`
        * `top`
        * Limit memory of gradle, elastic search and wherever else you can
    

