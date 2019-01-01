# IOTBoard
Web-based board to display real-time sensor data

![ScreenShot](/etc/screenshot-1.png)

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

TODO

## API Documentation

TODO

## Credits

- Chart visualizations: https://www.highcharts.com
- Font: https://fonts.google.com/specimen/Unica+One