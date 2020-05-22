# 110 End Of Year Project - 8 Ball Pool "Cool Pool"
Written in Java, by Killian Higgins, using accompanied Game Arena - found [here](https://github.com/finneyj/GameArena/).

## Compilation

Before compiling, clone or download this project zip/tar file and direct yourself to that directory. 

### To Compile on linux:
Open a terminal to this project directory and type the command "<code>sh LINUXCOMPILE.sh</code>".<br>
If this doesn't work for you, try using the command "<code>javac ./pool-game/src/main/java/com/killian/*.java -d ./target/</code>".<br>Although this is unlikely to work any better than just running the bash script.

### To Compile on Windows:
Open this project directory in File Explorer and double click on the "<code>WINDOWSCOMPILE.bat</code>" file to
run the batch script.<br> If this doesn't work for you, try opening this directory in command prompt and 
type the command "<code>javac .\pool-game\src\main\java\com\killian\\*.java -d .\target\\</code>". <br> Although this is unlikely to work any better than running the batch file.

## Running Compiled Game
### To run on Linux:

Open this project directory in a terminal and type the command "<code>sh LINUXRUN.sh</code>".<br>
If this doesn't run the game, type the command "<code>java -cp ./target/ com.killian.App</code>".<br>
Although the same with compilation; this is unlikely to work given running the bash file did not work.

### To run on Windows:
Open this project directory in File Explorer and double click on the "<code>WINDOWSRUN.bat</code>" batch file.<br> If this doesn't work for you, try opening this directory in command prompt and 
type the command "<code>java -cp .\target\ com.killian.App</code>". <br> Although this is unlikely to work any better than running the batch file.

## Known Bugs

There are a number of known bugs that I have been unable to fix or presently replicate in time for assessment:

- Clicking and placing cue ball while it is in hand, then immediately clicking again to attempt to take a shot will result in cue's X and Y position (and possibly respective velocitys) to NaN.
- Cue will flip orientation to current mouse position when directly north or south pointing
    - This is likely due to the tangent of ±pi/2 radians is undefined - this will need fixing by hard coding a solution at these extremes.
