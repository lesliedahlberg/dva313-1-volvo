

var RadialChartController = function(){


	var init = function (item){

		initialize(item);
	}

	return {
		init : init

	}

	function initialize(item){


		var data = {
			labels : getLabels(),
			datasets : [
			{
				responsive: true,
				fillColor : "rgba(255, 0, 0, 1)",
				strokeColor : "rgba(255, 0, 0, 1)",
				borderColor: "#03A9F4",
				backgroundColor: "rgba(3,169,244, 0.5)",
				data : getData(item),
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

		function getLabels(item){

			return ["Speed","RPM","Fuel","Load","Altitude","Distance"];
		}

		function getData(item){

			return [item.averageSpeed,item.averageRpm, item.averageFuel, item.averageLoad, item.averageAltitude, item.averageDistance];
		}
	}();

