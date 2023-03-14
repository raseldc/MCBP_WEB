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

<c:set var="map" value="${countData}"/>
<script>
    var locale = "${pageContext.response.locale}";
    $(function () {
        messageResource.init({
            filePath: contextPath + '/resources'
        });
        messageResource.load('messages_bn', function () {});
        messageResource.load('messages_en', function () {});
        $("#dashboard").addClass("active");

    });
    function commaSeparateNumber(val) {
        while (/(\d+)(\d{3})/.test(val.toString())) {
            val = val.toString().replace(/(\d+)(\d{3})/, '$1' + ',' + '$2');
        }
        return val;
    }
    Chart.defaults.scale.ticks.beginAtZero = true;
    Chart.defaults.global.defaultFontFamily = "SolaimanLipi";
    function drawBenPieChart(ctx)
    {
//        var jsonData = $.ajax({
//            url: "benByDivision",
//            dataType: "json",
//            async: false
//        }).responseText;

        var jsonData =${map['benDataAreaWise']};
        console.log(jsonData);
        //jsonData = JSON.parse(jsonData);
        benData = jsonData[0];
        var labelArray = [];
        var benDataArray = [];
        for (key in benData)
        {
            labelArray.push(key);
            benDataArray.push(getNumberInEnglish(benData[key].toString().replace(',', '')));
        }
        chartData = {
            datasets: [{
                    backgroundColor: ["#3e95cd", "#8e5ea2", "#3cba9f", "#e8c3b9", "#c45850", "#C9C9C5", "#FBDA79", "#A1D27A", "#4763A5", "#F26522"],
                    data: benDataArray
                }],
            labels: labelArray
        };
        options = {
            responsive: true,
            maintainAspectRatio: false,
            title: {
                display: false
//              text: 'Beneficiary by division'
            },
            legend: {
                position: 'right',
                labels: {
                    fontSize: 14
                },
                onClick: (e) => e.stopPropagation()
            }
        };
        new Chart(ctx, {
            type: 'pie',
            data: chartData,
            options: options
        });
    }

    function drawPayrollBarChart(ctx) {
//        var jsonData = $.ajax({
//            url: "payrollByFiscalYear",
//            dataType: "json",
//            async: false
//        }).responseText;
//        console.log(jsonData);
        var jsonData =${map['ficalYearWisePayment']};
        // jsonData = JSON.parse(jsonData);
        var paymentData = jsonData[0];
        var dataArray = [];
        var labelArray = [];
        for (key in paymentData)
        {
            labelArray.push(key);
            dataArray.push(paymentData[key]);
        }
//        console.log(dataArray);
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
    function drawApplicantBarChart(ctx) {
//        var jsonData = $.ajax({
//            url: "applicantByMonth",
//            dataType: "json",
//            async: false
//        }).responseText;
        var jsonData =${map['monthWiseApplicant']};
        console.log(jsonData);
        //  jsonData = JSON.parse(jsonData);
        var applicantData = jsonData[0];
        var dataArray = [];
        var labelArray = [];
        for (key in applicantData)
        {
            labelArray.push(key);
            dataArray.push(applicantData[key]);
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
<style>
    .circle {
        display: inline-block;
        width: 30px;
        height: 30px;
        border-radius: 50%;
        font-size: 14px;
        color: #fff;
        line-height: 30px;
        text-align: center;
        background: lightseagreen;
    }
    .dashboard-status{
        padding-bottom: 15px;
    }
</style>

<section class="content-header clearfix">
    <h1 class="pull-left">
        <spring:message code="dashboard.dashboard" />
    </h1>
    <div class="pull-right">
        <spring:message code="dashboard.loggedInUsers" />&nbsp;<span class="circle">${map['totalLoggedInUsers']}</span>
    </div>
</section>

<section class="content">
    <div class="row">    
        <div class="col-md-6 col-sm-6 col-xs-12">
            <div class="box box-info">
                <div class="box-header with-border">
                    <h3 class="box-title">
                        <div class="text-center">
                            <i class="fa fa-child " style="vertical-align: middle;" aria-hidden="true"></i><spring:message code="dashboard.benInfo" />
                        </div>
                    </h3>                                    
                </div>
                <div class="box-body">
                    <div class="row">
                        <div class="col-md-12">
                            <c:forEach var="data" items="${map['benData']}">
                                <div class="clearfix">
                                    <span class="pull-left">${data.key}</span>
                                    <strong class="pull-right" style="font-size: 20px;">${data.value}</strong>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>          
        </div>
        <div class="col-md-6 col-sm-6 col-xs-12">
            <div class="box box-info">
                <div class="box-header with-border">
                    <h3 class="box-title">
                        <div class="text-center">
                            <i class="fa fa-money " style="vertical-align: middle;" aria-hidden="true"></i><spring:message code="dashboard.paymentInfo" />
                        </div>
                    </h3>                                    
                </div>
                <div class="box-body">
                    <div class="row">
                        <div class="col-md-12">
                            <c:forEach var="data" items="${map['paymentData']}">
                                <div class="clearfix">                                                    
                                    <span class="pull-left">${data.key}</span>
                                    <span class="pull-right"><spring:message code="taka" />&nbsp;<strong style="font-size: 20px;">${data.value}</strong></span>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>              
        </div>        
    </div>    
    <div class="row">
        <div class="col-md-6">
            <div class="box box-info">
                <div class="box-header with-border">
                    <h3 class="box-title"><i class="fa fa-pie-chart " aria-hidden="true"></i><spring:message code="dashboard.benByDivision" /></h3>                                    
                </div>
                <div class="box-body">
                    <div class="row">
                        <div class="col-md-12">
                            <div id="benByAreaLoading"><i class="fa fa-spinner fa-spin" style="font-size:24px;"></i></div>
                            <div>
                                <canvas id="benByArea"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>   
        <div class="col-md-6">
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title"><i class="fa fa-bar-chart " aria-hidden="true"></i><spring:message code="dashboard.payrollByFiscalYear" /></h3>                                
                </div>
                <div class="box-body">
                    <div class="row">
                        <div class="col-md-12">
                            <div>
                                <div id="payrollLoading"><i class="fa fa-spinner fa-spin" style="font-size:24px;"></i></div>
                                <canvas id="payroll"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>        
    </div>
    <c:if test="${sessionScope.userDetail.userType.displayName eq 'Ministry' || sessionScope.userDetail.userType.displayName eq 'Directorate'}">

    </c:if>
    <div class="row">
        <div class="col-md-6">
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title"><i class="fa fa-user " aria-hidden="true"></i><spring:message code="dashboard.applicantInfo" /></h3>
                </div>
                <c:set var="applicantCountByStatus" value="${map['applicantCountByStatus']}"/>
                <div class="box-body" style="height: 240px;">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="clearfix dashboard-status">
                                <span class="pull-left"><spring:message code="dashboard.pa" /></span>
                                <strong class="pull-right">${applicantCountByStatus['primarilyApproved']}</strong>
                            </div>
                            <div class="clearfix dashboard-status">
                                <span class="pull-left"><spring:message code="dashboard.fvp" /></span>
                                <strong class="pull-right">${applicantCountByStatus['fieldVerifiedCount']}</strong>
                            </div>
                            <div class="clearfix dashboard-status">
                                <span class="pull-left"><spring:message code="dashboard.rf" /></span>
                                <strong class="pull-right">${applicantCountByStatus['rejectedAtField']}</strong>
                            </div>
                        </div>
                    </div>                    
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title"><i class="fa fa-user " aria-hidden="true"></i><spring:message code="dashboard.applicantByMonth" /></h3>
                </div>
                <div class="box-body">
                    <div class="row">
                        <div class="col-md-12">                            
                            <div class="chart">
                                <div id="applicantLoading"><i class="fa fa-spinner fa-spin" style="font-size:24px;"></i></div>
                                <canvas id="applicant"></canvas>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</section>
<script>



    $(function () {

//        if ('${sessionScope.userDetail.userType.displayName}' === 'Ministry' || '${sessionScope.userDetail.userType.displayName}' === 'Directorate')
//        {
        $("#benByAreaLoading").hide();
        $("#benByArea").css({height: "220px"});
        var ctx = $("#benByArea").get(0).getContext("2d");
        drawBenPieChart(ctx);
        $("#payrollLoading").hide();
        $("#payroll").css({height: "220px"});
        ctx = $("#payroll").get(0).getContext("2d");
        drawPayrollBarChart(ctx);
//        }
        $("#applicantLoading").hide();
        $("#applicant").css({height: "220px"});
        var ctx = $("#applicant").get(0).getContext("2d");
        drawApplicantBarChart(ctx);
    });
</script>