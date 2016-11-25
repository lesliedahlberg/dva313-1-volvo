var functionInFile=false;
var json="";
var averageFuel=0;
var averageRpm=0;
var loaded=false;
var fuell=0;
var fuelComplete=0;
var averageValues=[20,40,100,20,30,40,50];



function highscoresList(){

$.ajax({
  type: "POST",
  url: "http://autoelektronikame.ipage.com/volvo/php/query.php",//http://stackoverflow.com/questions/20035101/no-access-control-allow-origin-header-is-present-on-the-requested-resource
dataType : 'json',
 error: function(){
    window.location.href = "error.html";
  },
  success: function(data, textStatus, jqXHR) {
json = data[0];
//setAlias();
//setScoreValue();
setCompleteConsumption();


functionInFile=true;
  }
});
} 

function setCompleteConsumption(){
var fuelComplete=JSON.parse(JSON.stringify(json.FUEL.split(",")));
var fuell=0;
for (var i = 0; i < fuelComplete.length; i++) {
fuell+=Number(fuelComplete[i]);
}
$(".fuel").html(fuell+"L");
//averageValues.push(Math.round(fuell/fuelComplete.length));
}

function setAverageValue(){
averageFuel=Math.round(fuell/fuelComplete.length);
}

function setScoreValue(){
$(".score").html("Score: "+json.score);
}

function setAlias(){
  $(".alias").html(json.name);
}

function timeFactoringForChart(data){
var data = data.split(",");
var timeRefactored="";
for (var i = 0; i < data.length; i++) {
timeRefactored+=data[i]+"min" +((i!=data.length-1)?", ":"");
}
timeRefactored = JSON.parse(JSON.stringify(timeRefactored.split(",")));//toObject(timeRefactored);//;//=
return timeRefactored;
}

function getRightChartData(){
return JSON.parse(JSON.stringify(json.FUEL.split(",")))
}


$(document).ready(function(){ 


var fileInterval = setInterval(function(){
  if (functionInFile){


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
  data : averageValues
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

 //alert(timeRefactored);

      


Chart.defaults.global.legend.display=true;
                     Chart.defaults.global.legend.onClick="";                    
                      var ctx = document.getElementById("myChart");
                      var myLineChart = new Chart(ctx, {
                          type: 'line',
                          data: {
                            labels: timeFactoringForChart(json.TIME),
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
                                data: getRightChartData(),
                                spanGaps: false,
                          
                              }
                            ]
                          }

                      });
                     // $('body').addClass('loaded');
                       clearTimeout(loadingInterval);
                       clearInterval(fileInterval);
                        }
                        var loadingInterval = setTimeout(function(){
  $('body').addClass('loaded');
  }, 1000);

},100);




});


                  


// LINE CHART END