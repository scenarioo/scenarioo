#!/bin/sh

cd ~/Code/mvn-repo
git remote set-url origin https://github.com/scenarioo/mvn-repo.git
if [ -d ~/Code/ngUSD ]; then
	cd ~/Code/ngUSD
	git remote set-url origin https://github.com/scenarioo/scenarioo.git
	cd ..
	mv ngUSD scenarioo
fi

