#!/bin/bash
# usage: updatevm.sh [directory name]
directory=vm-update-scripts

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

