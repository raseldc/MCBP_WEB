<%-- 
    Document   : dashboard
    Created on : Feb 5, 2017, 3:00:12 PM
    Author     : Philip
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<script src="<c:url value="/resources/js/Chart.min.js" />"></script> 
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<script>
    var locale = "${pageContext.response.locale}";
    $(function () {
        messageResource.init({
            filePath: contextPath + '/resources'
        });
        messageResource.load('messages_bn', function () {});
        messageResource.load('messages_en', function () {});

        $("#dashboard").addClass("active");
        if (${sessionScope.user.headOfficeUser} === true)
        {
            var ctx = $("#quotaVsBen").get(0).getContext("2d");
            drawQuotaVsBenBarChart(ctx);
            ctx = $("#payroll").get(0).getContext("2d");
            drawPayrollBarChart(ctx);
            ctx = $("#training").get(0).getContext("2d");
            drawTrainingBarChart(ctx);

        }

    });
    function commaSeparateNumber(val) {
        while (/(\d+)(\d{3})/.test(val.toString())) {
            val = val.toString().replace(/(\d+)(\d{3})/, '$1' + ',' + '$2');
        }
        return val;
    }
    Chart.defaults.scale.ticks.beginAtZero = true;
    Chart.defaults.global.defaultFontFamily = "SolaimanLipi";
    function drawQuotaVsBenBarChart(ctx) {
        var jsonData = $.ajax({
            url: "quotaVsBen",
            dataType: "json",
            async: false
        }).responseText;
        console.log(jsonData);
        jsonData = JSON.parse(jsonData);
        quotaData = jsonData[0];
        benData = jsonData[1];
        var labelArray = [];
        var quotaDataArray = [];
        var benDataArray = [];
        for (key in quotaData)
        {
            labelArray.push(key);
            quotaDataArray.push(quotaData[key]);
        }
        for (key in benData)
        {
            benDataArray.push(benData[key]);
        }
        console.log(quotaDataArray);

        var chartData = {
            labels: labelArray,
            datasets: [{
                    label: jsonData[2],
                    backgroundColor: 'rgb(0,192,239)',
                    data: quotaDataArray,
                    borderWidth: 1
                },
                {
                    label: jsonData[3],
                    backgroundColor: 'rgb(0,166,90)',
                    data: benDataArray,
                    borderWidth: 1
                }]
        };
        var chartOptions = {
            responsive: true,
            maintainAspectRatio: false,
            legend: {
                position: 'top',
                labels: {
                    fontSize: 14
                },
                onClick: (e) => e.stopPropagation()
            },
            scales: {
                xAxes: [{
                        ticks: {
                            fontSize: 14
                        },
                        gridLines: {
                            display: false
                        },
                        scaleLabel: {
                            display: true,
                            fontSize: 14,
                            labelString: jsonData[4]
                        }
                    }],
                yAxes: [{
                        ticks: {
                            fontSize: 14,
                            userCallback: function (label, index, labels) {
                                if (Math.floor(label) === label) {
                                    return locale === 'bn' ? getNumberInBangla(label.toString()) : label;
                                }
                            }
                        },
                        gridLines: {
                            display: false
                        },
                        scaleLabel: {
                            display: true,
                            fontSize: 14,
                            labelString: jsonData[5]
                        }
                    }]
            }
        };
        new Chart(ctx, {
            type: "bar",
            data: chartData,
            options: chartOptions
        });
    }
    function drawPayrollBarChart(ctx) {
        var jsonData = $.ajax({
            url: "payroll",
            dataType: "json",
            async: false
        }).responseText;
        console.log(jsonData);
        jsonData = JSON.parse(jsonData);
        var paymentData = jsonData[0];
        var dataArray = [];
        var labelArray = [];
        for (key in paymentData)
        {
            labelArray.push(key);
            dataArray.push(paymentData[key]);
        }
        console.log(dataArray);
        var chartData = {
            labels: labelArray,
            datasets: [{
                    label: jsonData[1],
                    backgroundColor: 'rgb(60,141,188)',
                    data: dataArray,
                    borderWidth: 1
                }]
        };
        var chartOptions = {
            responsive: true,
            maintainAspectRatio: false,
            legend: {
                position: 'top',
                labels: {
                    fontSize: 14
                },
                onClick: (e) => e.stopPropagation()
            },
            scales: {
                xAxes: [{
                        //barPercentage: 0.2,
                        ticks: {
                            fontSize: 14
                        },
                        gridLines: {
                            display: false
                        },
                        scaleLabel: {
                            display: true,
                            fontSize: 14,
                            labelString: jsonData[2]
                        }
                    }],
                yAxes: [{
                        ticks: {
                            fontSize: 14,                           
                            userCallback: function (label, index, labels) {
                                if (Math.floor(label) === label) {
                                    return locale === 'bn' ? getNumberInBangla(commaSeparateNumber(label.toString())) : commaSeparateNumber(label);
                                }
                            }
                        },
                        gridLines: {
                            display: false
                        },
                        scaleLabel: {
                            display: true,
                            fontSize: 14,
                            labelString: jsonData[3]
                        }
                    }]
            }
        };
        new Chart(ctx, {
            type: "bar",
            data: chartData,
            options: chartOptions
        });
    }
    function drawTrainingBarChart(ctx) {
        var jsonData = $.ajax({
            url: "trainingByMonth",
            dataType: "json",
            async: false
        }).responseText;
        console.log(jsonData);
        jsonData = JSON.parse(jsonData);
        var trainingData = jsonData[0];
        var dataArray = [];
        var labelArray = [];
        for (key in trainingData)
        {
            labelArray.push(key);
            dataArray.push(trainingData[key]);
        }
        console.log(dataArray);
        var chartData = {
            labels: labelArray,
            datasets: [{
                    label: jsonData[1],
                    backgroundColor: 'rgb(60,141,188)',
                    data: dataArray,
                    borderWidth: 1
                }]
        };
        var chartOptions = {
            responsive: true,
            maintainAspectRatio: false,
            defaultFontColor: '#333',
            defaultFontSize: 14,
            legend: {
                position: 'top',
                labels: {
                    fontSize: 14
                },
                onClick: (e) => e.stopPropagation()
            },
            scales: {
                xAxes: [{
//                        barPercentage: 0.6,
                        ticks: {
                            fontSize: 14
                        },
                        gridLines: {
                            display: false
                        },
                        scaleLabel: {
                            display: true,
                            fontSize: 14,
                            labelString: jsonData[2]
                        }
                    }],
                yAxes: [{
                        ticks: {
                            beginAtZero: true,
//                            stepSize: 1,
                            fontSize: 14,
//                            callback: function (label, index, labels) {
//                                return locale === 'bn' ? getNumberInBangla(label.toString()) : label;
//                            }
                            userCallback: function (label, index, labels) {
                                if (Math.floor(label) === label) {
                                    return locale === 'bn' ? getNumberInBangla(label.toString()) : label;
                                }

                            }

                        },
                        gridLines: {
                            display: false
                        },
                        scaleLabel: {
                            display: true,
                            fontSize: 14,
                            labelString: jsonData[3]
                        }
                    }]
            }
        };
        new Chart(ctx, {
            type: "bar",
            data: chartData,
            options: chartOptions
        });
    }
</script>
<section class="content-header">
    <h1>
        <spring:message code="dashboard.dashboard" />
        <small><spring:message code="paymentCycle.label.fiscalYear" />&nbsp;${fiscalYearName}</small>
    </h1>
</section>
<c:set var="map" value="${countData}"/>
<section class="content">
    <div class="row">
        <div class="col-md-3 col-sm-6 col-xs-12">
            <div class="info-box">
                <span class="info-box-icon bg-aqua"><i class="ion ion-ios-gear-outline"></i></span>
                <div class="info-box-content">
                    <span class="info-box-text"><spring:message code="dashboard.totalQuota" /></span>
                    <span class="info-box-number">${map['totalQuota']}<small></small></span>
                </div>
            </div>
        </div>
        <div class="col-md-3 col-sm-6 col-xs-12">
            <div class="info-box">
                <span class="info-box-icon bg-red"><i class="ion ion-ios-people-outline"></i></span>
                <div class="info-box-content">
                    <span class="info-box-text"><spring:message code="dashboard.totalApplicants" /></span>
                    <span class="info-box-number">${map['totalApplicant']}</span>
                </div>
            </div>
        </div>
        <div class="clearfix visible-sm-block"></div>
        <div class="col-md-3 col-sm-6 col-xs-12">
            <div class="info-box">
                <span class="info-box-icon bg-green"><i class="ion ion-ios-people-outline"></i></span>
                <div class="info-box-content">
                    <span class="info-box-text"><spring:message code="dashboard.totalBeneficiary" /></span>
                    <span class="info-box-number">${map['totalBeneficiary']}</span>
                </div>
            </div>
        </div>
        <div class="col-md-3 col-sm-6 col-xs-12">
            <div class="info-box">
                <span class="info-box-icon bg-yellow"><i class="ion ion-ios-people-outline"></i></span>
                <div class="info-box-content">
                    <span class="info-box-text"><spring:message code="dashboard.loggedInUsers" /></span>
                    <span class="info-box-number">${map['totalLoggedInUsers']}</span>
                </div>
            </div>
        </div>
    </div>
    <c:if test="${sessionScope.user.headOfficeUser == true}">
        <div class="row">
            <div class="col-md-6">
                <div class="box box-info">
                    <div class="box-header with-border">
                        <h3 class="box-title"><spring:message code="dashboard.quotaVsBen" /></h3>                                    
                    </div>
                    <div class="box-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div>
                                    <canvas id="quotaVsBen" style="height: 180px"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>   
            <div class="col-md-6">
                <div class="box box-success">
                    <div class="box-header with-border">
                        <h3 class="box-title"><spring:message code="dashboard.payrollByCycle" /></h3>                                
                    </div>
                    <div class="box-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div >
                                    <canvas id="payroll" style="height: 180px;"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>        
        </div>
    </c:if>
    <div class="row">
        <div class="col-md-4">
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title"><spring:message code="dashboard.applicantsCount" /></h3>
                </div>
                <c:set var="applicantCountByStatus" value="${map['applicantCountByStatus']}"/>
                <div class="box-body">
                    <div class="table-responsive">
                        <table class="table no-margin">
                            <thead>
                                <tr>
                                    <th><spring:message code="dashboard.applicantStatus" /></th>
                                    <th><spring:message code="dashboard.count" /></th>                                  
                                </tr>
                            </thead>
                            <tbody>                               
                                <tr>
                                    <td><spring:message code="dashboard.pa" /></td>
                                    <td>${applicantCountByStatus['primarilyApproved']}</td>
                                </tr>
                                <tr>
                                    <td><spring:message code="dashboard.rsp" /></td>
                                    <td>${applicantCountByStatus['spbmuRejected']}</td>
                                </tr>
                                <tr>
                                    <td><spring:message code="dashboard.fvp" /></td>
                                    <td>${applicantCountByStatus['fieldVerifiedCount']}</td>
                                </tr>
                                <tr>
                                    <td><spring:message code="dashboard.rf" /></td>
                                    <td>${applicantCountByStatus['rejectedAtField']}</td>
                                </tr>
                                <tr>
                                    <td><spring:message code="dashboard.nv" /></td>
                                    <td>${applicantCountByStatus['rejectedAtNIDCheck']}</td>
                                </tr>
                            <tbody>                            
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <c:if test="${sessionScope.user.headOfficeUser == true}">
            <div class="col-md-8">
                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title"><spring:message code="dashboard.training" /></h3>
                    </div>
                    <div class="box-body">
                        <div class="row">
                            <div class="col-md-12">                            
                                <div class="chart">
                                    <canvas id="training" style="height: 220px;"></canvas>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</section>
