# GreenfootGame
A simple top down Roguelike using Greenfoot.

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Addon scripts](#addon-scripts)
* [data.nbt](#data.nbt)
* [Contributers](#contributers)
* [Noised map generation](#noised-map-generation)

## General info
This a simple top down Roguelike game based on Greenfoot.  


## Technologies 
Project created with:  
Java 11  
Greenfoot: 3.6.0  
Python 3.9.0

## Setup
Download the game.jar file.  
Install Java 11 from ****.  
Run the game.jar file.  
Yeah.... No!


## Addon scripts
The addonscripts (mapbuilder, nbt_injector) are scripts, written in python.
They are NOT required for the game to work, but are tools for the devs and modders to create new weapons, maps etc. more easily.

## data.nbt
the file data.nbt stores all data except save files. This includes weapons, items, enemies, the world and a lot more. 
NBT or Named Binary Tag is a file format used by the game Minecraft and can be opened using an nbtexplorer such as [NBTExplorer](https://github.com/jaquadro/NBTExplorer/releases/tag/v2.8.0-win)

## Noised map generation
The rooms of the dungeon don't always look the same. They differ each time the game is started (aka the screens are cashed).
This is due to generation noise (defined in the data.nbt) and is applied when the map is generated.

## Contributers
-Thomas Wagner  
Github: https://github.com/ThomasWagner-dev

-Andreas Bücker   
Github: https://github.com/KommentatorForAll

-Jaques Reimers  
Github: https://github.com/Klaus341
