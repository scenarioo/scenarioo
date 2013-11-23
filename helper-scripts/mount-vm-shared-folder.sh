#!/bin/sh
#
# Mounting of a shared directory in Oracle VirtualBox VM:
# Do the following manually first:
# 1. VM setting under "Geräte>GemeinsameOrdner...": 
#   - change the name of your shared folder to "vm-shared" 
#    - and the path to a directory named accordingly.
# 2. create an empty folder "vm-shared" in your home directory.
# 3. on every startup of your VM run this script.
#
sudo mount -t vboxsf vm-shared ~/vm-shared
