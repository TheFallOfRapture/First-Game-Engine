# First Game Engine (alternatively, No Good Naming Ideas Yet)

One day in Summer 2016, I had the idea of taking my old AP Computer Science final project and making a game engine out of it. A number of weeks later, on August 26, 2016, I pushed the new game engine I had to this repository.

This repository is mainly for storage and version control purposes. No contributions are necessary.

## Hierarchy

### src

Source code. Obviously.

#### com/fate

##### engine

Code common to all games created using this library. Could be thought of as the actual "engine" part of this repository.

##### game

Games created with this game engine. Can be thought of as "demos".

**All content beyond here is subject to change.**

###### flappybird

The project that started it all. The code has been modified since its days as a school project. FlappyBirdLauncher is the main class, if you really want to run my crappy Flappy Bird clone. Please don't. It's probably broken.

###### platformer

??? (Mostly just abandoned code. Probably going to end up deleted some time in the near future.)

###### shooting

Testing grounds for a game idea I had where you have a player that has a pre-determined movement pattern and to win the level, you must manipulate blocks in the environment to get the player to an exit.

###### testing

Not really used for testing. Mostly just abandoned code. Probably will end up deleting the folder entirely.


### res

Resources used by the games created under the games folder.

#### shaders

Shaders used to render objects in game.

#### levels

Text files (may change at some point) containing level data for "ShootingGame". The system used to generate levels using these files is still in development.

#### textures

Textures (PNG files) used by the games created under the games folder.
