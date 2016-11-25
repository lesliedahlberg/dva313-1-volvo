
var HighscoreController = function(radialChartController, lineChartController){


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
			radialChartController.init(data[i]);	
			lineChartController.init(data[i]);
		};


	onComplete();	
}

function onComplete(){
	$('body').addClass('loaded');
	$(".hhhd:first").addClass('active');
	$('.collapsible').collapsible({
		onOpen: function(el) { 
			
		}
	});
	$(".collapsible-header").on("click", function(e){
		 e.preventDefault();
		var contentPanelId = jQuery(this).attr("id");
		var classHeader = jQuery(this).attr("class");
		classHeader=classHeader.split(" ");
		
		 if(classHeader.length<3){
		setTimeout(function(){
			
			var completeS="#"+contentPanelId;
// alert(contentPanelId);

$('html, body').animate({scrollTop:$(completeS).position().top}, 'normal');
			//window.location.hash=completeS;
       // window.scrollTo(200, 100);
    }, 300);
	}

	});

}




$.fn.focusWithoutScrolling = function(){
var x = window.scrollX, y = window.scrollY;
alert(x +" "+ y);
this.focus();
window.scrollTo(x, y);
// return this; //chainability
};


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

  <a class="waves-effect waves-light btn">Fuel</a>
  <a class="waves-effect waves-light btn">Distance</a>
  <a class="waves-effect waves-light btn">Load</a>

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