# DataSwitch

An Android app, By using its service any network device can enable or disable the data connection of device on which service is ruuning.



## Code Structure

There are two types of code.

An android app---

1. java/com/android/dataswitch  - This Directory contains java related code 
2. res - This Directory contains resource for application 
3. AndroidManifest.xml - This contains manifest file.

An Gui Client application using gtk for linux platform---

1. dataswitch.c - which contains c code to intract with android service
2. switch.glade - This is like a resource to create a gui.


Android app have two button on and off. Using on button we can start service. This service starts a thread in which network related oparation are performed. these network related oparation are capeble to peform turn off and on the data connection according the information send by the client app.


