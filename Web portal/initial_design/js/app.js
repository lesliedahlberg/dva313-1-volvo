
var highscoreController = function(){


var fail = function(){

  $('.modal').modal();
  $('#error-modal').modal('open');
          
}
var done = function(data){

var generatedView = "";


for (var i = 0; i < data.length; i++) {
	data[i].index = i;
	generatedView += generateView(data[i]);
};

$('#results-table').html(generatedView);

for (var i = 0; i < data.length; i++) {
	generateRadialChart(data[i]);
	generateLineGraph(data[i]);
};


onComplete();	
}

function onComplete(){
$('body').addClass('loaded');
$(".hhhd:first").addClass('active');
$('.collapsible').collapsible();
}

function getData(){

$.ajax({
	type: "POST",
	url: "http://autoelektronikame.ipage.com/volvo/php/query.php",
	dataType : 'json',
	error: fail,
	success: done
});
}

var init = function (){

getData();
}

return {
init : init
}


function generateView (item){


var html = `
<li>
<!--Header of the item-->
<div class="collapsible-header hhhd">
<i class="material-icons">filter_drama</i>
{{name}}</span> 
<span class="right score">Score: {{score}}</span>
</div>
<!-- Body of first collapsable item -->
<div class="collapsible-body">

<div class="row">

<!--LEFT SIDE-->
<div class="col s6">
<br/><br/><br/>
<div class="radar-chart-container">
<canvas id="radar-chart-{{index}}" class="radar-chart" ></canvas>
</div>
</div>
<!-- END LEFT SIDE-->

	  <!-- BEGINING // CHART, BUTTONS, TABLE-->
	  <div class="col s6">
	  <!--LINE CHART-->
	  <canvas id="line-chart-{{index}}" width="200" height="150" style="margin-top:9px;"></canvas>
	  <!--END LINE CHART-->
	  <!--BUTTONS -->  

	  <a class="waves-effect waves-light btn">Fuel</a>
	  <a class="waves-effect waves-light btn">Distance</a>
	  <a class="waves-effect waves-light btn">Load</a>

	  <!--END BUTTONS -->

		<!-- START // TABLE-->
		<ul class="collection" style="margin-top:9px;">

		<li class="collection-item">Fuel: <span class="fuel">{{totalFuel}}L</span></li>
		<li class="collection-item">Distance: {{totalDistance}}km</li>
		<li class="collection-item">Load: {{totalLoad}}kg</li>
		</ul>
		<!-- END // TABLE-->
		</div>
	  <!-- END // CHART, BUTTONS, TABLE-->
	  </div>
	  </li>
	  `;

	  return Mustache.render(html, item);; 
	}


//THIS IS RADIAL CHART


function generateRadialChart(item){


var data = {
	labels : getRadialChartLabels(),
	datasets : [
	{
		responsive: true,
		fillColor : "rgba(255, 0, 0, 1)",
		strokeColor : "rgba(255, 0, 0, 1)",
		borderColor: "#03A9F4",
		backgroundColor: "rgba(3,169,244, 0.5)",
		data : getRadialChartData(item),
	}]}

	var ctx = document.getElementById("radar-chart-"+ item.index).getContext("2d");

	var myRadarChart = new Chart(ctx, {
		type: 'radar',
		data: data,
		options:{
			legend: {
display: false
}
		}
	});
}

function getRadialChartLabels(item){

	return ["Speed","RPM","Fuel","Load","Altitude","Distance"];
}
function getRadialChartData(item){
//	alert(item);

	return [item.averageSpeed,item.averageRpm, item.averageFuel, item.averageLoad, item.averageAltitude, item.averageDistance];
}

//THIS IS END OF RADIAL CHART

function generateLineGraph(item){

var data = {
	labels: getLineGraphLabels(item),
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
		data: getLineGraphData(item),
		spanGaps: false,

	}
	]
};


var ctx = document.getElementById("line-chart-"+ item.index);
var myLineChart = new Chart(ctx, {
	type: 'line',
	data: data,
	options:{
		
				legend: {
display: true,
onClick: "",
  }
			
		
	}
});

}
function getLineGraphLabels(data){

return timeFactoringForChart(data.TIME);
}
function getLineGraphData(data){


return data.FUEL.split(",");
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



}();