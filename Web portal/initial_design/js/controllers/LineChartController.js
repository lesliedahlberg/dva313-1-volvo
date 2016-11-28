var lineChart=[];
var LineChartController = function(){

	var init = function (item){

		initialize(item);
	}

	return {
		init : init

	}

	function initialize(item){

		var data = {
			labels: getLabels(item),
			datasets: [
			{
				label: "FUEL",
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
				data: getData(item, "FUEL"),
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

		lineChart.push(myLineChart);

	}
	function getLabels(item){

		return item.TIME.split(',').map(x => x + " min");
	}
	function getData(item, index){

		return item[index].split(",");
	}

}();

