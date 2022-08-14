/*--------------------START後台首頁chart的USER性別統計-------------------*/
let xhr = new XMLHttpRequest();
let countFemale;  //統計全部女性會員
let countMale;  //統計全部男性會員
let countSecret;  //統計全部不公開性別會員
xhr.onreadystatechange = function () {

    if (xhr.readyState === 4 && xhr.status === 200) {
        let result = JSON.parse(xhr.responseText);
        countFemale = parseInt(result.countFemale);
        countMale = parseInt(result.countMale);
        countSecret = parseInt(result.countSecret);
        console.log("取得會員性別統計: ");
        console.log("result.countFemale: " + countFemale);
        console.log("result.countMale: " + countMale);
        console.log("result.countSecret: " + countSecret);
    }

    /* START 會員性別統計Chart ->dashnoard.js 裡的doughnut Chart*/
    if ($("#memberdoughnutChart").length) {
        var doughnutChartCanvas = $("#memberdoughnutChart").get(0).getContext("2d");
        var doughnutPieData = {
            datasets: [{
                data: [countFemale, countMale, countSecret],
                backgroundColor: [
                    'rgba(255, 99, 132, 0.5)',
                    'rgba(255, 206, 86, 0.5)',
                    'rgba(54, 162, 235, 0.5)',
                    'rgba(75, 192, 192, 0.5)',
                    'rgba(153, 102, 255, 0.5)',
                    'rgba(255, 159, 64, 0.5)'
                ],
                borderColor: [
                    'rgba(255,99,132,1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(255, 159, 64, 1)'
                ],
            }],

            // These labels appear in the legend and in the tooltips when hovering different arcs
            labels: [
                '女',
                '男',
                '不公開',
            ]
        };
        var doughnutPieOptions = {
            cutoutPercentage: 50,
            animationEasing: "easeOutBounce",
            animateRotate: true,
            animateScale: false,
            responsive: true,
            maintainAspectRatio: true,
            showScale: true,
            legend: false,
            legendCallback: function (chart) {
                var text = [];
                text.push('<div class="chartjs-legend"><ul class="justify-content-center">');
                for (var i = 0; i < chart.data.datasets[0].data.length; i++) {
                    text.push('<li style="font-size: 16px;"><span style="background-color:' + chart.data.datasets[0].backgroundColor[i] + '">');
                    text.push('</span>');
                    if (chart.data.labels[i]) {
                        text.push(chart.data.labels[i]);
                    }
                    text.push('</li>');
                }
                text.push('</div></ul>');
                return text.join("");
            },

            layout: {
                padding: {
                    left: 0,
                    right: 0,
                    top: 0,
                    bottom: 0
                }
            },
            tooltips: {
                callbacks: {
                    title: function (tooltipItem, data) {
                        return data['labels'][tooltipItem[0]['index']];
                    },
                    label: function (tooltipItem, data) {
                        return data['datasets'][0]['data'][tooltipItem['index']];
                    }
                },
                backgroundColor: '#fff',
                titleFontSize: 18,
                titleFontColor: '#0B0F32',
                bodyFontColor: '#737F8B',
                bodyFontSize: 18,
                displayColors: false
            }
        };
        var doughnutChart = new Chart(doughnutChartCanvas, {
            type: 'doughnut',
            data: doughnutPieData,
            options: doughnutPieOptions
        });
        document.getElementById('member-doughnut-chart-legend').innerHTML = doughnutChart.generateLegend();
    }
    /* END 會員性別統計Chart ->dashnoard.js 裡的doughnut Chart*/
}
xhr.open("GET", "/Lung/StatisticMember/CountAllGender", true);
xhr.send();

/*--------------------END後台首頁chart的USER性別統計-------------------*/


/*--------------------START後台首頁chart的總數統計--------------------*/

let xhr1 = new XMLHttpRequest();
let sumMember;  //會員總數
let sumAnnounce;  //公告總數
let sumAnimal;

xhr1.onreadystatechange = function () {

    if (xhr1.readyState === 4 && xhr1.status === 200) {
        let result = JSON.parse(xhr1.responseText);
        sumMember = parseInt(result.sumMember);
        sumAnnounce = parseInt(result.sumAnnounce);
        sumAnimal = parseInt(result.sumAnimal);

        console.log("result.sumMember: " + sumMember);
        console.log("result.sumAnnounce: " + sumAnnounce);
        console.log("result.sumAnimal: " + sumAnimal);

    }
    if ($("#lungHiOverview").length) {
        var lungHiOverviewChart = document.getElementById("lungHiOverview").getContext('2d');
        var lungHiOverviewData = {
            labels: ["會員總數", "商品總數", "訂單總數", "志工活動總數", "公告總數", "待認養總數", "待送養總數"],
            datasets: [{
                label: '',
                data: [sumMember, 5, 6, 7, sumAnnounce, 8, 9,sumAnimal,10],
                backgroundColor: [
                    'rgba(255, 99, 132, 0.5)',
                    'rgba(255, 206, 86, 0.5)',
                    'rgba(54, 162, 235, 0.5)',
                    'rgba(75, 192, 192, 0.5)',
                    'rgba(153, 102, 255, 0.5)',
                    'rgba(255, 159, 64, 0.5)'
                ],
                borderColor: [
                    'rgba(255,99,132,1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(255, 159, 64, 1)'
                ],
                borderWidth: 0,
                fill: true, // 3: no fill

            }
                // ,{
                //     label: 'This week',
                //     data: [215, 290, 210, 250, 290, 230, 290],
                //     backgroundColor: "#1F3BB3",
                //     borderColor: [
                //         '#1F3BB3',
                //     ],
                //     borderWidth: 0,
                //     fill: true, // 3: no fill
                // }
            ]
        };

        var lungHiOverviewOptions = {
            responsive: true,
            maintainAspectRatio: false,
            // scales: {ticks: {beginAtZero: true,}},
            scales: {
                yAxes: [{
                    gridLines: {
                        display: true,
                        drawBorder: false,
                        color: "#F0F0F0",
                        zeroLineColor: '#F0F0F0',
                    },
                    ticks: {
                        beginAtZero: true,
                        autoSkip: true,
                        maxTicksLimit: 5,
                        fontSize: 14,
                        color: "#6B778C"
                    }
                }],
                xAxes: [{
                    stacked: true,
                    barPercentage: 0.35,
                    gridLines: {
                        display: false,
                        drawBorder: false,
                    },
                    ticks: {
                        beginAtZero: false,
                        autoSkip: true,
                        maxTicksLimit: 12,
                        fontSize: 14,
                        color: "#6B778C"
                    }
                }],
            },
            legend: false,
            legendCallback: function (chart) {
                var text = [];
                text.push('<div class="chartjs-legend"><ul>');
                for (var i = 0; i < chart.data.datasets.length; i++) {
                    console.log(chart.data.datasets[i]); // see what's inside the obj.
                    text.push('<li class="text-muted text-small">');
                    text.push('<span style="background-color:' + chart.data.datasets[i].borderColor + '">' + '</span>');
                    text.push(chart.data.datasets[i].label);
                    text.push('</li>');
                }
                text.push('</ul></div>');
                return text.join("");
            },

            elements: {
                line: {
                    tension: 0.4,
                }
            },
            tooltips: {
                backgroundColor: '#0B0F32',
            }
        }
        var lungHiOverview = new Chart(lungHiOverviewChart, {
            type: 'bar',
            data: lungHiOverviewData,
            options: lungHiOverviewOptions
        });
        document.getElementById('lungHiOverview-legend').innerHTML = lungHiOverview.generateLegend();
    }

}
xhr1.open("GET", "/Lung/BackendStatistic/SummaryAll", true);
xhr1.send();
/*--------------------END後台首頁chart的總數統計--------------------*/








