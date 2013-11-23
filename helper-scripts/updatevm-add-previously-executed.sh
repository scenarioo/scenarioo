#!/bin/bash
# usage: updatevm.sh [directory name]
# adds all scripts to the log of executed scripts without executing them:
# CAUTION: only use in case you have run the scripts that have to be run manually on your own already 
# (e.g. because you wanted to cherry pick witch ones to run!)

directory=~/Code/scenarioo/vm-update-scripts

if [ ! -d "$directory" ]; then
  echo "usage: updatevm-add-previously-executed.sh [directory name]"
  exit -1
fi

vmupdatelog=~/.updatevm.log
echo -e "\e[00;32mclearing previously executed script file $vmupdatelog\e[00m"

read -p "Are you sure [y/n]? " -n 1 -r
echo    # (optional) move to a new line
if [[ ! $REPLY =~ ^[Yy]$ ]]
then
	exit
fi
rm $vmupdatelog


find $directory -name '*.sh' |
while read filename
do
  echo -e "\e[00;32m>> adding $filename to log\e[00m"
  echo $filename >> $vmupdatelog
done
wait
