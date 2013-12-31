#!/bin/sh

cd ~/Code 
if [ ! -d ~/Code/mvn-repo ]; then
	git clone https://github.com/scenarioo/mvn-repo
fi
