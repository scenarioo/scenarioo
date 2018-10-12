# Circle CI

## Why?

* Reduce load on our server => Ability to run more demo instance on it & increase stability, no more shaky builds on coding days
* We don't have to maintain jenkins anymore and can simplify our setup/maintenance
* Circle CI not Travis? Travis doesn't support test reporting. Circle CI is fucking fast.
* Future: Github Checks API integration for even more information in PRs https://blog.github.com/2018-05-07-introducing-checks-api/

## TODOs

* Publish artifact
* Deploy artifact on our server
* Import scenarioo docu from E2E run
* Disable E2E tests on our server
* Write documentation
* [DONE] Run unit tests
* [DONE] Report test results of tests
* [DONE] Add elastic search docker image
* [DONE] Run E2E tests
* [DONE] Fix memory issues
* [DONE] Report E2E tests

## Configuration

* We are on an open-source plan of Circle CI with up to 4 instances in parallel
* All the config takes place in `./circleci/config.yml`
* Copy everything you want to keep into `./test-results`. These files will be persisted as artifacts
* There you can also find the logging output of the scenarioo server: `./test-results/scenarioo-server.log`



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
    

