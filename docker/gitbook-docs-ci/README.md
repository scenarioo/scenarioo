# Circle CI Docker image for Docs Build

This image is used on our Circle CI build job to publish our markdown documentation with Graphviz and node tooling with plantuml


1. Build the image:

```
docker build scenarioo/circleci-graphviz-java-node:<version> .
```

2. Test the image: 

```
docker run --rm -it -v /home/user/tmp:/tmp --entrypoint /bin/sh scenarioo/circleci-graphviz-java-node:<version>
git clone https://github.com/scenarioo/scenarioo.git
cd scenarioo
cd docs
npm install
npm run build
```
Check that everything is created in the docs folder as expected under `_book` and `assets` - especially `assets/images/uml` which contains the relevant plantuml images.

You can use the mapped `/tmp` folder to map these files outside the container to check them (e.g. svg qraphics - which is the problematic stuff here).

3. How to publish the image

```
docker login
username: scenarioo
password: <our-ususal-master-pw>
docker push scenarioo/circleci-graphviz-java-node:<version>
```
4. New published image must be set with correct version in Circle CI configuration.
