  // RADAR CHART 


  Chart.defaults.global.legend.display=false;

// Radar Data
var data = {
  labels : ["Speed","RPM","Fuel","Load","Altitude","Distance"],
  datasets : [
    {
        responsive: true,
      fillColor : "rgba(255, 0, 0, 1)",
      strokeColor : "rgba(255, 0, 0, 1)",
      borderColor: "#03A9F4",
      backgroundColor: "rgba(3,169,244, 0.5)",
      data : [65,59,50,81,56,55]
    }
  ]
}

var ctx = document.getElementById("radarChart").getContext("2d");


var myRadarChart = new Chart(ctx, {
    type: 'radar',
    data: data
    // options: options
});
// RADAR CHART END


// LINE CHART 

Chart.defaults.global.legend.display=true;
                         Chart.defaults.global.legend.onClick="";
                          var ctx = document.getElementById("myChart");
                          var myLineChart = new Chart(ctx, {
                              type: 'line',
                              data: {
                                labels: ["1 min", "2 min", "3 min", "4 min", "5 min"],
                                datasets: [
                                  {
                                    label: "Fuel",
                                    fill: false,
                                    lineTension: 0.1,
                                    backgroundColor: "rgba(75,192,192,0.4)",
                                    borderColor: "#03A9F4",
                                    borderCapStyle: 'butt',
                                    borderDash: [],
                                    borderDashOffset: 0.0,
                                    borderJoinStyle: 'miter',
                                    pointBorderColor: "#03A9F4",
                                    pointBackgroundColor: "#fff",
                                    pointBorderWidth: 1,
                                    pointHoverRadius: 5,
                                    pointHoverBackgroundColor: "#03A9F4",
                                    pointHoverBorderColor: "#03A9F4",
                                    pointHoverBorderWidth: 2,
                                    pointRadius: 1,
                                    pointHitRadius: 10,
                                    data: [65, 59, 80, 81, 56, 55, 40],
                                    spanGaps: false,

                                  }
                                ]
                              }

                          });


// LINE CHART END