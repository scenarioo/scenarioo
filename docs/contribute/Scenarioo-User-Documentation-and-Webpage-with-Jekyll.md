We use Jekyll to generate our webpage and documentation on the github pages.

What is special: 
We do not directly maintain the docu files in the gh-pages branch. We edit our pages in the 'docu' directory on every branch and finaly release from the docu directory on master branch to gh-pages.

## Jekyll Setup for generating documentation

You need to install Jekyll and all prerequisites on your development environment machine, as explained here under https://help.github.com/articles/using-jekyll-with-pages/.

This was what I did on the scenarioo dev VM to get it installed:

 * Install Ruby 2.x:
   See also http://rvm.io/ for how to install ruby version manager (RVM) to get newest ruby version, I did the following steps on the VM:
    * `sudo apt-get update`
    * `gpg --keyserver hkp://keys.gnupg.net --recv-keys 409B6B1796C275462A1703113804BB82D39DC0E3`
    * `sudo apt-get install curl`
    * `\curl -sSL https://get.rvm.io | bash -s stable`
    * `source /home/scenarioo/.rvm/scripts/rvm`
    * `rvm get stable`
    * `rvm install ruby2.2.1`
 * Install Jekyll:
    * `gem install bundler`
    * call the following command inside the 'scenarioo.github.io' directory (where Gemfile is checked in): 
      `bundle install`

## How to maintain/edit the docu

Simply change the html and markdown files in the repository 'scenarioo.github.io'.

**Attention**: If you work for documentation of a future release, then do not work on the main branch but change the docu on the appropriate branch!

## How to generate and review docu

you can browse the webpage locally, by calling the following commands inside directory 'scenarioo.github.io':
 * `jekyll serve`
 * browse to localhost:4000

To be sure that the compilation is similar and uptodate with github pages, you should maybe use the following instead:
 * TODO


## How to release the docu to github pages

Follow the usual release process, and merge the changes you made to the master branch

If you checkin on master branch your change is directly released.
