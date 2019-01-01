/**
 * Calculate a 32 bit FNV-1a hash
 * Found here: https://gist.github.com/vaiorabbit/5657561
 * Ref.: http://isthe.com/chongo/tech/comp/fnv/
 *
 * @param {string} str the input value
 * @param {boolean} [asString=false] set to true to return the hash value as
 *     8-digit hex string instead of an integer
 * @param {integer} [seed] optionally pass the hash of the previous chunk
 * @returns {integer | string}
 */
function hashFnv32a(str, asString, seed) {
    /*jshint bitwise:false */
    var i, l,
        hval = (seed === undefined) ? 0x811c9dc5 : seed;

    for (i = 0, l = str.length; i < l; i++) {
        hval ^= str.charCodeAt(i);
        hval += (hval << 1) + (hval << 4) + (hval << 7) + (hval << 8) + (hval << 24);
    }
    if( asString ){
        // Convert to 8 digit hex string
        return ("0000000" + (hval >>> 0).toString(16)).substr(-8);
    }
    return hval >>> 0;
}

// event bus (server to client)
var eventBus = new EventBus("/event/");

// when event bus is opened
eventBus.onopen = function () {
    eventBus.registerHandler("event.to.client", function (err, msg) {

        var json = JSON.stringify(msg.body);
        window.console && console.log("*** MEASUREMENT ***");
        window.console && console.log('.. json = ' + json);

        var measurements = JSON.parse(json);

        for (var i in measurements) {

            var measurement = measurements[i];

            var sensor = measurement.sensor;
            window.console && console.log('.. sensor = ' + sensor);

            var timestamp = measurement.timestamp;
            window.console && console.log('.. timestamp = ' + timestamp);

            var history = measurement.history;
            window.console && console.log('.. history = ' + history);

            var id = "chart_container_" + hashFnv32a(sensor, true);
            window.console && console.log('.. container id = ' + id);

            if (document.getElementById(id) == null) {

                window.console && console.log(".. creating new chart container (id = " + id + ")");

                $('body').append('<div id="' + id + '" class="chart-container"></div>');

                $("#" + id).highcharts({
                    chart: {
                        type: "spline",
                        animation: Highcharts.svg
                    },
                    title: {
                        text: sensor
                    },
                    xAxis: {
                        title: {
                            text: "Time"
                        },
                        type: "datetime",
                        labels: {
                            overflow: 'justify'
                        }
                    },
                    yAxis: {
                        title: {
                            text: "Temperature & Humidity"
                        },
                        minorGridLineWidth: 0,
                        gridLineWidth: 0,
                        alternateGridColor: null
                    },
                    plotOptions: {
                        spline: {
                            lineWidth: 4,
                            states: {
                                hover: {
                                    lineWidth: 5
                                }
                            },
                            marker: {
                                enabled: false
                            }
                        }
                    },
                    tooltip: {
                        formatter: function () {
                            return '<b>' + this.series.name + '</b><br/>' + Highcharts.dateFormat('%H:%M:%S', this.x) + '<br/>' + Highcharts.numberFormat(this.y, 2);
                        }
                    }
                });

                var values = measurement.values;
                for (var i in values) {
                    var name = values[i].name;
                    window.console && console.log('.. creating new data series (name = " + name + ")');
                    $("#" + id).highcharts().addSeries({ name: name }, false);
                }

                $("#" + id).draggable({stack: "div"});
            }

            var values = measurement.values;
            for (var i in values) {
                var value = values[i].value;
                if ($("#" + id).highcharts().series[i].data.length >= history) {
                    $("#" + id).highcharts().series[i].addPoint([timestamp, value], true, true);
                } else {
                    $("#" + id).highcharts().series[i].addPoint([timestamp, value]);
                }
            }

            $("#" + id).highcharts().redraw();

        }

    });

}