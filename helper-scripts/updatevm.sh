#!/bin/bash
# usage: updatevm.sh [directory name]
# Runs all vm-update-scripts that are not logged to be run already on your local machine
# CAUTION: when first time setting up your VM for scenarioo you should run the update scripts manually (and probably only run those update scripts realy needed for current setup on your machine), afterwards you should use updatevm-add-previously-executed.sh in that case.
cd ~/Code/scenarioo
directory=~/Code/scenarioo/vm-update-scripts

if [ ! -d "$directory" ]; then
  echo "usage: updatevm.sh [directory name]"
  exit -1
fi

vmupdatelog=~/.updatevm.log
touch $vmupdatelog

find $directory -name '*.sh' |
while read filename
do
	if grep -Fxq "$filename" $vmupdatelog
	then
		echo -e "\e[00;31m>> $filename has already been executed\e[00m"
	else
		echo -e "\e[00;32m>> executing $filename \e[00m"
		$filename
		echo $filename >> $vmupdatelog
	fi
	done
wait

