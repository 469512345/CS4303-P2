# Robotron 4303

Ballista command is a simple city defense game where the player must protect their cities from falling meteorites (and more).

# How to run Robotron 4303

This game is just a java project which includes Processing as a library (libjars/core.jar).

A makefile is provided to build a shaded jar file which contains the processing core, as well as the game code.

```bash
make jar
java -jar CS4303P2.jar
```
or, for convenience 
```bash
make run
```

# How to play Robotron 4303

1) Press any key to start the game. (Pressing escape will close the game instead)
2) Use WASD to control the player
3) Aim using the mouse cursor
4) Fire by left-clicking.
5) Kill the robots using your laser.
6) Save the family by reaching them and touching.
7) Survive the waves of increasing difficulty, until you finally lose your last life!

# Controls

| Key          | Action                                                    |
|--------------|-----------------------------------------------------------|
| `W`          | Move forwards                                             |
| `A`          | Move left                                                 |
| `S`          | Move backwards                                            |
| `D`          | Move right                                                |
| `Left Click` | Fire to the mouse cursor                                  | 
| `Escape`     | Close game (menu screens) <br/> Pause/play game (in game) |