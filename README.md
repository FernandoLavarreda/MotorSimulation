# MotorSimulation
Simulation of inline-four motor with the firing order 1-3-4-2

https://github.com/FernandoLavarreda/MotorSimulation/assets/70668651/c8c5d1cf-0762-4b9d-a611-4dfc3ca01eb4


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
```
git clone https://github.com/FernandoLavarreda/MotorSimulation
cd MotorSimulation\src
javac -cp ".(colon ':' or semicolon ';' depending platform)..\thirdparty\flatlaf.jar" *.java -d "..\run"
```

## Run
```
cd ..\run
java -cp ".(colon ':' or semicolon ';' depending platform)..\thirdparty\flatlaf.jar" MotorSimulation "..\resources"
```
