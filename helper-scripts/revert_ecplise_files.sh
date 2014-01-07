#!/bin/bash
files_to_revert=`find . -name "org.eclipse.jdt.core.prefs"`
echo -e "\e[00;32mreverting the following files:\e[00m"

for i in $files_to_revert; do
	echo $i
done

read -p "Are you sure [y/n]? " -n 1 -r
echo    # (optional) move to a new line
if [[ ! $REPLY =~ ^[Yy]$ ]]
then
	exit
fi
for i in $files_to_revert; do
	echo "reverting $i"
	git checkout -- $i
done

