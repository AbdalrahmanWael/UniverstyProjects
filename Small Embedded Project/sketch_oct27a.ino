#include <Servo.h>

Servo servoX; 
Servo servoY;

const int VRx = A0; // Joystick X-axis
const int VRy = A1; // Joystick Y-axis

int mapJoystickValue(int value) {
  return map(value, 0, 1023, 0, 180);
}

void setup() {
  servoX.attach(9);  // Servo X to pin 9
  servoY.attach(10); // Servo Y to pin 10
  

  Serial.begin(9600); 
}

void loop() {
  // Read joystick values
  int xValue = analogRead(VRx);
  int yValue = analogRead(VRy);
  
  // Map joystick values to servo angles
  int servoXAngle = mapJoystickValue(xValue);
  int servoYAngle = mapJoystickValue(yValue);
  
  // Move servos
  servoX.write(servoXAngle);
  servoY.write(servoYAngle);
  
  Serial.print("X-Axis: ");
  Serial.print(xValue);
  Serial.print(" | Servo X Angle: ");
  Serial.print(servoXAngle);
  Serial.print(" | Y-Axis: ");
  Serial.print(yValue);
  Serial.print(" | Servo Y Angle: ");
  Serial.println(servoYAngle);

  delay(50); 
}