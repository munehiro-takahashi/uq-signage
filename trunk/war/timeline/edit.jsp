<!DOCTYPE html>
<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="e" uri="http://uq.nskint.co.jp/pd/taglibs/uqSignage-editor"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<html lang="ja">
<head>
<meta charset="UTF-8" />
<title>uqSignage - ${f:h(title)}</title>
<!--[if IE]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<link rel="stylesheet" href="/css/html5reset.css" type="text/css" media="screen" charset="utf-8"/>
<link rel="stylesheet" href="/css/global.css" type="text/css" media="screen" charset="utf-8"/>
<link rel="stylesheet" href="/css/custom-theme/jquery-ui.custom.css" type="text/css" media="screen" charset="utf-8"/>
<style type="text/css">
th.name {
}
.slider {
	width:590px;
	margin: 5px
}
.time_slider {
	margin:5px;
}
.start_time .end_time {
	border: none;

}
.container {
	margin:5px;
	padding: 5px;
	width: 800px;
}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery-ui.js"></script>
<script type="text/javascript">
var HOUR = 60;
$(function(){
	$(".button").button();
	$("#addSchedule").click(addSchedule);
	$(".time_slider").each(function() {

		$(this).find(".slider").slider({
			range: true,
			animate: true,
			min: 0,
			max: 24*HOUR,
			values:[parseInt($(this).find(".start_time").data("min")),parseInt($(this).find(".end_time").data("min"))],
			slide: slideTime
		});
	});
});
function slideTime(event, ui) {
	var val0 = ui.values[ 0 ];
    var min0 = parseInt(val0 % HOUR, 10);
    var hour0 = parseInt(val0 / HOUR % 24, 10);

    var val1 = ui.values[ 1 ];
    var min1 = parseInt(val1 % HOUR, 10);
    var hour1 = parseInt(val1 / HOUR % 24, 10);

    var sche = $(this).parent();

    var start = sche.find(".start_time");
    start.val(hour0 + ":" + digitFormat(min0));
    var end = sche.find(".end_time");
    end.val(hour1 + ":" + digitFormat(min1));
}
function digitFormat(num) {
	if (num < 10) {
		num = "0" + num;
	}
	return num;
}
function addSchedule() {
	var id = parseInt($('#schedule>tbody>tr:last>.sch_id').text()) + 1;
	var html =
			'<tr id="sche_' + id + '">'+
				'<td><input type="checkbox" name="selectSchedule" id="selectSchedule_'+id+'" value="'+id+'"/></td>'+
				'<td class="sch_id">'+id+'</td>'+
				'<td class="time_slider">'+
					'<input type="text" readonly class="start_time" id="start_time_'+id+'" data-time="${schedule.start.hour * 60 + schedule.start.minute}" value="8:00" /> - '+
					'<input type="text" readonly class="end_time" id="end_time_'+id+'" data-time="${schedule.end.hour * 60 + schedule.end.minute}" value="20:00" />'+
					'<div class="slider" id="slider_'+id+'"></div>'+
				'</td>'+
				'<td></td>'+
			'</tr>';
	$("#schedule>tbody").append(html);
	$("#slider_"+id).slider({
		range: true,
		animate: true,
		min: 0,
		max: 24*HOUR,
		values:[8*HOUR,20*HOUR],
		slide: slideTime
	});
}
</script>
</head>
<body>
<header>
<h1>${f:h(title)}</h1>
<t:navbar/>
</header>
<div class="clearfix">
	<t:sidemenu/>
	<div id="body_contents">
		<form id="listForm" action="edit" method="post">
			<input type="hidden" id="mid" name="mid" value="${mid}" />
			<input type="hidden" id="tlid" name="tlid" value="${tlid}" />
			<div class="container">
				名称：<input type="text" id="name" name="name" value="${timeline.name}" />
			</div>
			<div class="container">
				スケジュール：
				<input type="button" class="button" id="addSchedule" name="addSchedule" value="追加" />
				<input type="button" class="button" id="deleteSchedule" name="deleteSchedule" value="削除" />
				<table id="schedule" class="data">
					<colgroup>
						<col style="width:50px" />
						<col style="width:80px" />
						<col style="width:600px" />
						<col style="width:80px" />
					</colgroup>
					<thead>
						<tr>
							<th>選択</th>
							<th>ID</th>
							<th>開始終了</th>
							<th>表示内容</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${requestScope.timeline.schedule}" var="schedule" varStatus="sch_stat">
							<tr id="sche_${schedule.id}">
								<td>
									<c:if test="${not sch_stat.first}">
										<input type="checkbox" name="selectSchedule" id="selectSchedule_${schedule.id}" value="${schedule.id}"/>
									</c:if>
								</td>
								<td class="sch_id">${schedule.id}</td>
								<td class="time_slider">
									<c:choose>
										<c:when test="${sch_stat.first}">
											default
										</c:when>
										<c:otherwise>
											<input type="text" readonly class="start_time" id="start_time_${schedule.id}" data-time="${schedule.start.hour * 60 + schedule.start.minute}" value="${schedule.start.hour}:${schedule.start.minute}" />
											-
											<input type="text" readonly class="end_time" id="end_time_${schedule.id}" data-time="${schedule.end.hour * 60 + schedule.end.minute}" value="${schedule.end.hour}:${schedule.end.minute}" />
											<div class="slider" id="slider_${schedule.id}"></div>
										</c:otherwise>
									</c:choose>
								</td>
								<td>${schedule.blockId}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="container">
				表示内容：
				<input type="button" class="button" id="addBlock" name="addBlock" value="追加" />
				<input type="button" class="button" id="deleteBlock" name="deleteBlock" value="削除" />
				<table id="block" class="data">
					<colgroup>
						<col style="width:50px" />
						<col style="width:80px" />
						<col style="width:100px" />
						<col style="width:50px" />
					</colgroup>
					<thead>
						<tr>
							<th>選択</th>
							<th>ID</th>
							<th>レイアウト</th>
							<th>詳細</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${requestScope.timeline.block}" var="block">
						<tr>
							<td><input type="checkbox" name="selectSchedule" id="selectSchedule_${block.id}" /></td>
							<td>${block.id}</td>
							<td>${block.layoutId}</td>
							<td></td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="container">
				<input type="button" class="button" value="登録" />
			</div>
		</form>
	</div>
</div>
<footer>
${e:copyright()}
</footer>
</body>
</html>
