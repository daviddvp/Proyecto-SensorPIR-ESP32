#include <WiFi.h>
#include <WiFiClientSecure.h>
#include <UniversalTelegramBot.h>
#include <ArduinoJson.h>
#include <Servo.h>
 
// Replace with your network credentials
const char* ssid = "4432casa";
const char* password = "Zj4h8wA19SlVi2";

#define SERVO_PIN    9  //PWM pin that is connected to the servo
// Initialize Telegram BOT
#define BOTtoken "1864900471:AAFybjpkbKMGTIinw_7bJua-1CD8pFeHQHw"  // your Bot Token (Get from Botfather)

// Use @myidbot to find out the chat ID of an individual or a group
// Also note that you need to click "start" on a bot before it can
// message you
#define CHAT_ID "987933826"

Servo demoServo;        //create a servo object
WiFiClientSecure client;
UniversalTelegramBot bot(BOTtoken, client);

int servoAngle = 0;     //servo angle which can vary from 0 - 180
const int motionSensor = 27; // PIR Motion Sensor
bool motionDetected = false;

// Indicates when motion is detected
void IRAM_ATTR detectsMovement() {
  //Serial.println("Movimiento detectado");
  motionDetected = true;
}

void setup() {
  demoServo.attach(SERVO_PIN);  //attach the pin to the object so that we can send the signal to it
  // PIR Motion Sensor mode INPUT_PULLUP
  pinMode(motionSensor, INPUT_PULLUP);
  // Set motionSensor pin as interrupt, assign interrupt function and set RISING mode
  attachInterrupt(digitalPinToInterrupt(motionSensor), detectsMovement, RISING);

  // Attempt to connect to Wifi network:
  Serial.print("Connecting Wifi: ");
  Serial.println(ssid);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  client.setCACert(TELEGRAM_CERTIFICATE_ROOT); // Add root certificate for api.telegram.org
  
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }

  Serial.println("Buenos dias/tardes/noches");
  Serial.println("WiFi connected");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  bot.sendMessage(CHAT_ID, "Iniciando el sistema...", "");
}

void loop() {
  if(motionDetected){
    bot.sendMessage(CHAT_ID, "Movimiento detectado", "");
    Serial.println("Movimiento detectado");
    motionDetected = false;
}
{
  for(servoAngle = 0; servoAngle <= 180; servoAngle++)  //increment the angle from 0 to 180, in steps of 1
  { 
    demoServo.write(servoAngle);                        //set the servo angle and it will move to it
    delay(20);                                          //wait 20ms before moving to the next position
  }
  for (servoAngle = 180; servoAngle >= 0; servoAngle--) //decrement the angle from 180 to 0, in steps of 1 
  {
    demoServo.write(servoAngle);                        //set the servo angle and it will move to it
    delay(20);                                          //wait 20ms before moving to the next position
  }
}