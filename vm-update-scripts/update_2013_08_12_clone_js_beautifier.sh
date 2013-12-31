#!/bin/bash
cd ~/Code/scenarioo/scenarioo-client/app
if [ ! -d third-party ]; then
	mkdir third-party
	cd third-party
	git clone https://github.com/einars/js-beautify
fi

