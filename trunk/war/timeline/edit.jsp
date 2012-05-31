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
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery-ui.js"></script>
<script type="text/javascript">
var HOUR = 60;
$(function(){
	$(".button").button();
	$(".time_slider").each(function() {
		$(this).find(".slider").slider({
			min: 0,
			max: 24*HOUR,
			values:[$(this).find(".start_time").data("min")*HOUR,$(this).find(".end_time").data("min")*HOUR],
			slide: slideTime
		});
	});
});
function slideTime(event, ui) {
	var val0 = ui.values[ 0 ];
    var minutes0 = parseInt(val0 % HOUR, 10),
    var hours0 = parseInt(val0 / HOUR % 24, 10),

    var val1 = ui.values[ 1 ];
    var minutes1 = parseInt(val1 % HOUR, 10),
    var hours1 = parseInt(val1 / HOUR % 24, 10);

}
function addSchedule() {
	var id = 0;
	var html =
			'<tr>'+
				'<td><input type="checkbox" name="selectSchedule" id="selectSchedule_'+id+'" value="'+id+'"/></td>'+
				'<td>'+id+'</td>'+
				'<td class="time_slider">'+
					'<span class="start_time" id="start_time_'+id+'" data-time="${schedule.start.hour * 60 + schedule.start.minute}">${schedule.start.hour}:${schedule.start.minute}</span>'+
					'<span class="slider" id="slider_'+id+'"></span>'+
					'<span class="end_time" id="end_time_'+id+'" data-time="${schedule.end.hour * 60 + schedule.end.minute}">${schedule.end.hour}:${schedule.end.minute}</span>'+
				'</td>'+
				'<td></td>'+
			'</tr>';


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
			<div>
				名称：<input type="text" id="name" name="name" value="${timeline.name}" />
			</div>
			<div>
				スケジュール：
				<input type="button" class="button" id="addSchedule" name="addSchedule" value="追加" />
				<input type="button" class="button" id="deleteSchedule" name="deleteSchedule" value="削除" />
				<table id="schedule" class="data">
					<thead>
						<tr>
							<th>選択</th>
							<th>ID</th>
							<th>開始終了</th>
							<th>表示内容</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${requestScope.timeline.schedule}" var="schedule">
						<tr>
							<td><input type="checkbox" name="selectSchedule" id="selectSchedule_${schedule.id}" value="${schedule.id}"/></td>
							<td>${schedule.id}</td>
							<td class="time_slider">
								<span class="start_time" id="start_time_${schedule.id}" data-time="${schedule.start.hour * 60 + schedule.start.minute}">${schedule.start.hour}:${schedule.start.minute}</span>
								<span class="slider"></span>
								<span class="end_time" id="end_time_${schedule.id}" data-time="${schedule.end.hour * 60 + schedule.end.minute}">${schedule.end.hour}:${schedule.end.minute}</span>
							</td>
							<td></td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div>
				表示内容：
				<input type="button" class="button" id="addBlock" name="addBlock" value="追加" />
				<input type="button" class="button" id="deleteBlock" name="deleteBlock" value="削除" />
				<table id="block" class="data">
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
							<td></td>
							<td></td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</form>
	</div>
</div>
<footer>
${e:copyright()}
</footer>
</body>
</html>
