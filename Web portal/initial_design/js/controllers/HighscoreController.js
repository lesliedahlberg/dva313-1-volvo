var json="";

var HighscoreController = function(radialChartController, lineChartController){


var fail = function(){

	$('.modal').modal();
	$('#error-modal').modal('open');

}
var done = function(data){
	json=data;

	var generatedView = "";

	for (var i = 0; i < data.length; i++) {
		data[i].index = i;
		generatedView += generateView(data[i]);
	};

	$('#results-table').html(generatedView);

	for (var i = 0; i < data.length; i++) {
		radialChartController.init(data[i]);	
		lineChartController.init(data[i]);
	};


	onComplete();	
}

function onComplete(){

$('body').addClass('loaded');
$(".hhhd:first").addClass('active');
$('.collapsible').collapsible();
//Scroll on click but only if it is inactive highscore
$(".collapsible-header").on("click", function(e){
	e.preventDefault();

	var idClicked = jQuery(this).attr("id");
	var isClicked = $(this).hasClass("active");

	scrollToActiveItem(idClicked, isClicked);
});

$(".waves-light.btn").on("click", function(){
	
	var type =  $(this).attr('data-type');
	var index = $(this).attr("data-id");
	var label = $(this).attr("data-label");

	var data = json[index][type].split(",");

	lineChartController.change(index, data, label);
});


//On machine changed 
$('#machine-types input').on('change', function() {
    update($('input[name="Gender"]:checked', '#machine-types').val());
});


}

function scrollToActiveItem(id, isClicked){

	if(!isClicked){

		setTimeout(function(){
			
			var completeS= "#" + id;
			$('html, body').animate({
			scrollTop:$(completeS).position().top}, 
			'normal');
		}, 300);
	}
}



function getData(machineType){

$.ajax({
type: "POST",
url: "http://autoelektronikame.ipage.com/volvo/php/query.php",
dataType : 'json',
data: { getData : machineType },
error: fail,
success: done
});
}

var init = function (){

	getData("All");
}

function update (machineType){
	
	getData(machineType);
}


return {
init : init
}


function generateView (item){


var html = `
<li>
<!--Header of the item-->
<div class="collapsible-header hhhd" id="{{index}}">

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

<a class="waves-effect waves-light btn" data-label="Fuel" data-type="FUEL" data-id="{{index}}">Fuel</a>
<a class="waves-effect waves-light btn" data-label="Distance" data-type="DISTANCE" data-id="{{index}}">Distance</a>
<a class="waves-effect waves-light btn" data-label="Load" data-type="LOADED" data-id="{{index}}">Load</a>

<!--END BUTTONS -->

<!-- START // TABLE-->
<ul class="collection" style="margin-top:9px;">

<li class="collection-item">Fuel: {{totalFuel}} L</li>
<li class="collection-item">Distance: {{totalDistance}} km</li>
<li class="collection-item">Load: {{totalLoad}} kg</li>
</ul>
<!-- END // TABLE-->
</div>
<!-- END // CHART, BUTTONS, TABLE-->
</div>
</li>
`;

return Mustache.render(html, item);; 
}




}(RadialChartController, LineChartController);	