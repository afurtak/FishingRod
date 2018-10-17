# Fishing Rod
Application to create sets of flashcards and learn.

## Features
Application runs in background. Window with possibility to add flashcards can called any time by pressing shortcut (Shift + Ctrl + Space)
or by clicking on corresponding icon in system tray.  <br />
Also it is possible to run "learning window" with currently existing flashcards from choosen set.

## How it works
Application uses JNativeHook to capture shortcuts without focus on particular window.<br />
Creates own folder in home directory named .FishingRod where saves database with flashcards and sets names.

## Requirements
Operating system: Windows, Linux <br />
Fishing rod requires also installed Java Virtual Machine.

## Run
1) Download jar file from https://github.com/afurtak/FishingRod/releases/download/v0.1-alpha/FishingRod.jar
2) Linux: <br />java -jar FishingRod.jar <br /> Windows 10: <br />double click on jar file.
3) It is recommended to add Fishing Rod to autorun to your system.
