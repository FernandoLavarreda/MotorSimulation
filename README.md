# MotorSimulation
Simulation of inline-four motor with the firing order 1-3-4-2


https://github.com/FernandoLavarreda/MotorSimulation/assets/70668651/1e61e475-06eb-4c46-9279-0788b1ee8e32


## Measurements
Crankshaft radius: 42mm <br>
Piston head diameter: 82.5mm <br>
Connecting Rod length: 147.36mm <br>
Intake valve: length 62.5mm, diameter 44mm <br>
Outake valve: length 62.5mm, diameter 34mm <br>
The cams for the outake base radius: 35mm, lift 11mm, sinusoidal curve max height at 109° <br>
The cams for the intake valves base radius: 40mm, lift 11mm, sinusoidal curve max height at 104° <br>

Timing was made based on max lift from cams in relation to the TDC of the pistons. Exhaust max lift was set 112° before TDC, Intake max lift was set 108° after TDC.

## Build
### Linux & Mac
```console
git clone https://github.com/FernandoLavarreda/MotorSimulation
cd MotorSimulation\src
javac -cp ".:..\thirdparty\flatlaf.jar" *.java -d "..\run"
```

### Windows
```console
git clone https://github.com/FernandoLavarreda/MotorSimulation
cd MotorSimulation\src
javac -cp ".;..\thirdparty\flatlaf.jar" *.java -d "..\run"
```

## Run
### Linux & Mac
```console
cd ..\run
java -cp ".:..\thirdparty\flatlaf.jar" MotorSimulation "..\resources"
```
### Windows
```console
cd ..\run
java -cp ".;..\thirdparty\flatlaf.jar" MotorSimulation "..\resources"
```
