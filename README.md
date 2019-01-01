# IOTBoard
**IOTBoard** is an application designed to be able to read measurements from various sensors and visualize the data in real-time. All incoming data is assumed to be time-based, in practice meaning:

- A sensor identifier (for example "Basement Sensor")
- Time of measurement
- Name/value pairs representing the measurements (for example "Temperature", "Humidity")

The UI display a panel for each sensor, with different line charts for each measurement. The panels can be freely arranged on the board, and the charts are updated as data arrives (no need to refresh).

![ScreenShot](/etc/screenshot-1.png)

Originally the application was used on a Raspberry Pi to display temperature and humidity readings from wireless sensors around the household and display the information in one central place. The design was extended to easily support pretty much any kind of time-based measurement, and the application can be run on any platform supporting Java (Raspberry Pi, Desktop, Server, Cloud).

## Features

- JSON API to receive measurements from sensors
- Web UI to view measurement data in real-time
- Java reference client

## Requirements

- Java SDK (1.8 or later)
- Git client
- Apache Ant

## Compiling from Source

```
$ git clone https://github.com/tutikka/IOTBoard.git
$ cd IOTBoard
$ ant
```

## Example Usage

Starting the server:

```
$ cd dist
$ java -cp .:iotboard.jar:lib/* com.tt.iotboard.server.Server etc/iotboard.properties
```

Open your favorite browser and navigate to:

```
http://localhost:9090/html
```

Starting the example client:

```
$ cd dist
$ java -cp .:iotboard.jar:lib/* com.tt.iotboard.client.Client
```

## Configuration options

Server:

```
#
# The port number to listen on (API & UI).
#
iotboard.port=9090

#
# The authentication scheme (API only).
#
# com.tt.iotboard.server.authentication.NoAuthentication          : No authentication required
# com.tt.iotboard.server.authentication.BasicAccessAuthentication : The server requires Basic Access Authentication
#
iotboard.authentication=com.tt.iotboard.server.authentication.BasicAccessAuthentication

#
# Authentication options.
#
# No Authentication           : Not needed
# Basic Access Authentication : Specify username and password using "username=[USERNAME],password=[PASSWORD]"
#
iotboard.authentication.options=username=user,password=pass
```

Client:

```
TODO
```

## API Documentation

### Request URL

Syntax:

```
http(s)://[HOST]:[PORT]/api/[VERSION]/measurement
```

Example:

```
http://localhost:9090/api/v1/measurement
```

### Authentication

If the server requires **Basic Access Authentication**, include the following HTTP header in your request.

Syntax:

```
Authorization: Basic BASE64_ENCODE([USERNAME]:[PASSWORD])
```

Example (username = *user*, password = *pass*):

```
Authorization: Basic dXNlcjpwYXNz
```

For more information, refer to https://en.wikipedia.org/wiki/Basic_access_authentication.

### Required Headers

Syntax:

```
Content-Type: application/json
Content-Length: [LENGTH OF REQUEST BODY]
```

Example:

```
Content-Type: application/json
Content-Length: 1024
```

### Request Body

Syntax:

```
   [
       {
         "sensor": "[SENSOR_ID]",
         "timestamp": [MILLISECONDS FROM EPOCH],
         "history": [NUMBER OF ITEMS ON X-AXIS],
         "values": [
            {
                "name": "[MEASUREMENT NAME]",
                "value": [MEASUREMENT VALUE]
            },
            {
                "name": "[MEASUREMENT NAME]",
                "value": [MEASUREMENT VALUE]
            }
         ]
       },
       {
            ...
       }
   ]
```

Example:

```
   [
       {
         "sensor": "#001",
         "timestamp": "1544556043832",
         "history": 10,
         "values": [
            {
                "name": "Temperature",
                "value": 25.0
            },
            {
                "name": "Humidity",
                "value": 75.0
            }
         ]
       },
       {
         "sensor": "#002",
         "timestamp": "1544556043832",
         "history": 10,
         "values": [
            {
                "name": "Temperature",
                "value": 15.0
            },
            {
                "name": "Humidity",
                "value": 50.0
            }
         ]
       } 
   ]
```

## Credits

- Chart visualizations: https://www.highcharts.com
- Font: https://fonts.google.com/specimen/Unica+One