##  VM Installation

**this page is probably outdated, we have a different VM that we use now, please contact us, if you need it!**

We develop in an Ubuntu VM.
Setup by: Benjamin Müller and Sandra Weber

1. Download and install Oracle VM VirtualBox:
https://www.virtualbox.org/wiki/Downloads

2. Copy the VM from:
TODO currently there is no actual image available for download, please ask one of the other developers to hand you a VM over.

**IMPORTANT note to all developers when copying a VM to somebody else:** 
Make sure that the VM copy does not contain any private passwords or other confidential data!
For example check and clean the following places before copying your VM:
  * directory ~/.ssh: remove it, could contain keys or even passwords

##  VM Configuration (Personalization)

After copying the VM and starting it you have to personalize your VM as follows:

1. Setting your GIT account username and email to use, type the following in the command line:

  > git config --global user.name "yourgitusername"

  > git config --global user.email "youremail@yourdomian.com"

That's it so far. You can check the config settings with `git config -l`.

That should be everything.

## VM/IDE Updates shared between Developers

Important changes to the VM will be shared between developers as VM update scripts, you can find these in our git repository under ~/Code/scenarioo/vm-update-scripts. To run newly added update scripts after each update, you should consider the following steps:

1. this step is only necessary, if you do not have a file ".updatevm.log" in your home directory on your VM yet:
if this file is missing (check it!) you first have to decide on your own, which of the update scripts have not yet been executed for your VM installation and execute the appropriate scripts. After that you can run the script scenarioo/helper-scripts/updatevm-add-previously-executed.sh. This will mark all the currently available scripts as already executed in the file .updatevm.log in your home directory.

2. From now on you are prepared to simply call scenarioo/helper-scripts/updatevm.sh after each repository update to bring you VM to the appropriate updated/changed state to happily continue developing.

Please make sure, that if you make an important change in the development environment to document it on this WIKI page and to provide an appropriate update script to the other developers by checking it in under scenarioo/vm-update-scripts. 

### Rules / Guidelines / Checklist for VM Update Scripts

#### VM-Update-Scripts Directory Switching Guidelines
The scripts should not switch to a different directory and have to be written in such a way that they work no matter from which directory they are called (or assume "~/Code/scenarioo" as the working directory, because the updatevm.sh-Script starts from this directory. But this might be error-prone, because a preceeding script might have changed this directory location, even though a script should not do that).

Therefore please adhere to the following directory switching guidelines in your script:
1. Do not assume any directory location the script must be running in. If the directory location is important to your script, then start with a "cd"-Command switching to the appropriate directory.
2. If your script had to switch the directory location, then let your script finally switch back to "~/Code/scenarioo" when finished, just in case that a following script assumes to be in that directory (where updatevm.sh is starting).

#### VM-Update-Scripts Naming Guidelines
Adhere to the naming used in previous update scripts:
 * Always start with "update_yyyy_mm_dd_nnn"
   * where yyyy_mm_dd is the current date (today, when you created the update script) 
   * and nnn is the number of the update script on that day (starting with '001', increased by one for each new update script on that same day).
 * After the unique name prefix "update_yyyy_mm_dd_nnn", a short description should follow describing what the update script does.

#### VM-Update-Scripts Checkin Guidelines
Finally do not forget to change the execution flag of your script and to check that it runs properly using "updatevm.sh" from helper-scripts, before committing your script to the repository
