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

```properties
#
# The port number to listen on (API & UI).
#
iotboard.port=9090

#
# The authentication scheme (API only).
#
# com.tt.iotboard.server.authentication.NoAuthentication          : No authentication required
# com.tt.iotboard.server.authentication.BasicAccessAuthentication : The server requires Basic Access Authentication
# com.tt.iotboard.server.authentication.TokenAuthentication       : The server requires a signed access token
#
iotboard.authentication=com.tt.iotboard.server.authentication.TokenAuthentication

#
# Authentication options.
#
# No Authentication           : Not needed
# Basic Access Authentication : Specify username and password using "username=[USERNAME],password=[PASSWORD]"
# TokenAuthentication         : Specify the shared secret using "secret=[SECRET]"
#
iotboard.authentication.options=secret=abcd
```

Client:

You can use the builder pattern to configure the Java reference client and send measurements:

```java
Client client = new Client.Builder()
    .host("localhost")
    .port(9090)
    .connectTimeout(10000)
    .readTimeout(10000)
    .authentication(new TokenAuthentication("abcd"))
    .build();

Measurement measurement = new Measurement("Sensor #0001", System.currentTimeMillis(), 10);
measurement.getValues().add(new Value("Temperature", 25.0));
measurement.getValues().add(new Value("Humidity", 75.0));

client.send(measurement);
```

## API Documentation

### New Measurement

#### Request URL

Syntax:

```
http(s)://[HOST]:[PORT]/api/[VERSION]/measurement
```

Example:

```
http://localhost:9090/api/v1/measurement
```

#### Request Method

`HTTP POST`

#### Authentication

If the server requires **Basic Access Authentication**, include the following HTTP header in your request.

Syntax:

```
Authorization: Basic BASE64_ENCODE([USERNAME]:[PASSWORD])
```

Example:

- Username = `user`
- Password = `pass`

```
Authorization: Basic dXNlcjpwYXNz
```

For more information, refer to https://en.wikipedia.org/wiki/Basic_access_authentication.

If the server requires **Token Authentication**, include the following HTTP header in your request.

Syntax:

```
Authorization: IOTBoard BASE64_ENCODE(HMAC_SHA256([SECRET], [HTTP METHOD]\n[CONTENT_TYPE]\n[DATE]\n[URI]\nMD5[CONTENT]))
```

Example:

- Secret = `abcd`
- HTTP method = `POST`
- Content type = `application/json`
- Date = `Wed, 02 Jan 2019 10:21:04 EET`
- URI = `/api/v1/measurement`
- Content MD5 = `feac48bbe0beb7b1a54d07ae400f7aa3`

```
Authorization: IOTBoard MjFhZjU3NmRlM2Q4Zjg3ZjhlMjk4ZmM3OWE1ODc2N2U2MzM2OThhYjQ1ZTVhMjkxZWE4NzUxZTM5ZjhhNDhlZg==
```

#### Required Headers

Syntax:

```
Content-Type: application/json
Content-Length: [LENGTH OF REQUEST BODY]
```

Example:

- Content length = `1024`

```
Content-Type: application/json
Content-Length: 1024
```

#### Request Body

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

#### Response

- `HTTP OK` if the request succeeded
- `HTTP FORBIDDEN` if the server required authentication and access was denied
- `HTTP NOT FOUND` if the requested resource was not found (wrong URL)
- `HTTP INTERNAL SERVER ERROR` if an error occurred while processing a valid request

## Credits

- Chart visualizations: https://www.highcharts.com
- Font: https://fonts.google.com/specimen/Unica+One