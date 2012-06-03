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
	width:690px;
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
	width: 900px;
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
	$(".start_hour,.start_min,end_hour,.end_min").change(changeTime);
});
function slideTime(event, ui) {
	var val0 = ui.values[ 0 ];
    var min0 = parseInt(val0 % HOUR, 10);
    var hour0 = parseInt(val0 / HOUR % 24, 10);

    var val1 = ui.values[ 1 ];
    var min1 = parseInt(val1 % HOUR, 10);
    var hour1 = parseInt(val1 / HOUR % 24, 10);

    var sche = $(this).parent();

    sche.find(".start_hour").val(hour0);
    sche.find(".start_min").val(digitFormat(min0));
    sche.find(".end_hour").val(hour1);
    sche.find(".end_min").val(digitFormat(min1));
}
function changeTime(event) {
    var sche = $(this).parent();

    var start_hour = parseInt(sche.find(".start_hour").val());
    var start_min = parseInt(sche.find(".start_min").val());
    var end_hour = parseInt(sche.find(".end_hour").val());
    var end_min = parseInt(sche.find(".end_min").val());

    sche.find(".slider").slider("values",
    		[start_hour*HOUR+start_min,
    		 end_hour*HOUR+end_min]);
}
function digitFormat(num) {
	num = parseInt(num);
	if (num < 10) {
		num = "0" + num;
	}
	return num;
}
function addSchedule() {
	var id = parseInt($('#schedule>tbody>tr:last>.sch_id').text()) + 1;
	var html =
			'<tr class="schedules" id="sche_' + id + '">'+
				'<td><input type="checkbox" name="selectSchedule" id="selectSchedule_'+id+'" value="'+id+'"/></td>'+
				'<td class="sch_id">'+id+'</td>'+
				'<td class="time_slider">'+
					'<input type="text" size="2" maxlength="2" class="start_hour" id="start_hour_'+id+'" value="8" />'+
					' : '+
					'<input type="text" size="2" maxlength="2" class="start_min" id="start_min_'+id+'" value="00" />'+
					' - '+
					'<input type="text" size="2" maxlength="2" class="end_hour" id="end_hour_'+id+'" value="20" />'+
					' : '+
					'<input type="text" size="2" maxlength="2" class="end_min" id="end_min_'+id+'" value="00" />'+
					'<div class="slider" id="slider_'+id+'"></div>'+
				'</td>'+
				'<td>'+
				'<select class="blockid" id="blockid_'+id+'" name="blockid">'+
				<c:forEach items="${layoutList}" var="layout" varStatus="layStat">
					'<option value="${f:h(layout.id.id) }" <c:if test="${layStat.first}">selected</c:if>>${f:h(layout.id.id) }</option>'+
				</c:forEach>
			'</select>'+
			'<a href="#" onclick="preview('+id+');return false;"><span class="ui-icon ui-icon-newwin"></span></a>'+
				'</td>'+
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
	$("#start_hour_"+id+",#start_min_"+id+",#end_hour_"+id+",#end_min_"+id)
	.change(changeTime);
}
function preview(id) {
	window.open("/view/"+$('#blockid_'+id).val()+"/preview", "preview");
}
function save() {
	var name = $("#name").val();
	var xml =
		'<tl:timeline id="${tlid}" name="'+name+'" xmlns:h="http://www.w3.org/1999/xhtml" xmlns:lo="http://uq.nskint.co.jp/uqSignage/layout" xmlns:tl="http://uq.nskint.co.jp/uqSignage/timeline" xmlns:xml="http://www.w3.org/XML/1998/namespace" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://uq.nskint.co.jp/uqSignage/timeline timeline.xsd http://uq.nskint.co.jp/uqSignage/layout layout.xsd http://www.w3.org/1999/xhtml xhtml1-strict.xsd ">';

	var blocks = "";
	var block_ids = [];
	var schedules = "";
	$(".schedules").each(
			function(idx) {
				var self = $(this);
				var sid = self.find(".sch_id").text();
				var bid = self.find(".blockid").val();
				var start_hour = self.find(".start_hour").val();
				var start_min = digitFormat(self.find(".start_min").val());
				var end_hour = self.find(".end_hour").val();
				var end_min = digitFormat(self.find(".end_min").val());
				block_ids.push(bid);
				if (idx == 0) {
					  blocks += '<tl:block id="'+bid+'" layout_id="'+bid+'"/>';
					  schedules += '<tl:schedule block_id="'+bid+'" id="'+sid+'" />';
				} else {
					var i;
					for (i=0; i < block_ids.length; i++) {
						if (block_ids[i] == bid) {
							break;
						}
					}
					if (i < block_ids.length) {
						blocks += '<tl:block id="'+bid+'" layout_id="'+bid+'"/>';
					}
					schedules += '<tl:schedule block_id="'+bid+'" id="'+sid+'" start="'+start_hour+':'+start_min+':00" end="'+end_hour+':'+end_min+':00" />';
				}
			});
	xml += blocks + schedules +	'</tl:timeline>';
	$("#xml").val(xml);
	$("#editForm").submit();
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
		<form id="editForm" action="edit" method="post">
			<input type="hidden" id="mid" name="mid" value="${mid}" />
			<input type="hidden" id="tlid" name="tlid" value="${tlid}" />
			<input type="hidden" id="xml" name="xml" value="" />
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
						<col style="width:700px" />
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
							<tr class="schedules" id="sche_${schedule.id}">
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
											<input type="text" size="2" maxlength="2" class="start_hour" id="start_hour_${schedule.id}" value="${schedule.start.hour}" />
											:
											<input type="text" size="2" maxlength="2" class="start_min" id="start_min_${schedule.id}" value="${schedule.start.minute}" />
											-
											<input type="text" size="2" maxlength="2" class="end_hour" id="end_hour_${schedule.id}" value="${schedule.end.hour}" />
											:
											<input type="text" size="2" maxlength="2" class="end_min" id="end_min_${schedule.id}" value="${schedule.end.minute}" />
											<div class="slider" id="slider_${schedule.id}"></div>
										</c:otherwise>
									</c:choose>
								</td>
								<td>
								<select class="blockid" id="blockid_${schedule.id}" name="blockid">
									<c:forEach items="${layoutList }" var="layout">
										<option value="${f:h(layout.id.id) }" <c:if test="${schedule.blockId eq layout.id.id}">selected</c:if>>${f:h(layout.id.id) }</option>
									</c:forEach>
								</select>
								<a href="#" onclick="preview(${schedule.id});return false;"><span class="ui-icon ui-icon-newwin"></span></a>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
<%--
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
							<td>
								<select id="lid_" name="lid">
									<c:forEach items="${layoutList }" var="layout">
										<option value="${f:h(layout.id.id) }" <c:if test="${block.layoutId eq layout.id.id}">selected</c:if>>${f:h(layout.id.id) }</option>
									</c:forEach>
								</select>
								<a href="">プレビュー</a>
							</td>
							<td></td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
--%>
 			<div class="container">
				<input type="button" class="button" value="登録" onclick="save()"/>
			</div>
		</form>
	</div>
</div>
<footer>
${e:copyright()}
</footer>
</body>
</html>
