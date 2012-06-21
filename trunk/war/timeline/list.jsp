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
<title>uqSignage - タイムライン管理</title>
<!--[if IE]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<link rel="stylesheet" href="/css/html5reset.css" type="text/css" media="screen" charset="utf-8"/>
<link rel="stylesheet" href="/css/global.css" type="text/css" media="screen" charset="utf-8"/>
<link rel="stylesheet" href="/css/custom-theme/jquery-ui.custom.css" type="text/css" media="screen" charset="utf-8"/>
<style type="text/css">
th.name {
}
#add_dialog {
	width:300px;
}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery-ui.js"></script>
<script type="text/javascript">
$(function(){
	$(".button").button();
	$("#add_dialog").dialog(
			{
				disabled: true,
				autoOpen:false,
				minWidth: 300,
				buttons:[
					{
						text: "追加",
						click: addTimeline
					},
					{
						text: "キャンセル",
						click: function() {
							$("#add_dialog").dialog("close");
						}
					}
				]
			}
		);
});
function openAddDialog() {
	$("#add_dialog").dialog("open");
}
function addTimeline() {
	$("#listForm").attr("action", "add");
	$("#lid").val($("#lid_").val());
	$("#name").val($("#name_").val());
	$("#listForm").submit();
}

function onClickCheckBox() {
	$("input[name=chk_tlid]:checkbox:checked").length
}

function deleteTimeline() {
	$("#listForm").attr("action", "delete");
	$("#listForm").submit();
}

function editTimeline(tlid) {
	$("#tlid").val(tlid);
	$("#listForm").submit();
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
		<form id="listForm" action="edit" method="get">
			<div>
				<input class="button" type="button" id="create_button" value="追加" onclick="openAddDialog();" />
 				<input class="button" type="button" id="del_button" value="削除" onclick="deleteTimeline();" />
			</div>
			<table class="data">
				<thead>
					<tr>
						<th>削除</th>
						<th>編集</th>
						<th>タイムライン名</th>
						<th>代表者名</th>
						<th>登録日</th>
						<th>更新日</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${requestScope.timelineList}" var="timeline">
						<tr>
							<%-- 削除 --%>
							<td>
								<input type="checkbox" name="tlids" value="${f:h(timeline.id.id) }" onclick="onClickCheckBox();" />
							</td>
							<%-- 編集 --%>
							<td>
 								<input type="button" value="編集" onclick="editTimeline('${timeline.id.id}');"/>
							</td>
							<%-- タイムライン名 --%>
							<td>
								${timeline.xmlModel.name}
							</td>
							<%-- 表示URL --%>
							<td>
								<c:url value='${e:url("/view/")}' var="viewUrl"><c:param name="tlid" value="${timeline.id.id}"/></c:url>
								<a href="${viewUrl}">${viewUrl}</a>
							</td>
							<%-- 登録日 --%>
							<td>
								${timeline.registeredDate }
							</td>
							<%-- 更新日 --%>
							<td>
								${timeline.updatedDate }
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<input type="hidden" id="mid" name="mid" value="${mid}" />
			<input type="hidden" id="tlid" name="tlid" value="" />
			<input type="hidden" id="name" name="name" value="" />
			<input type="hidden" id="lid" name="lid" value="" />
		</form>
			<div id="add_dialog" title="新規タイムラインの追加">
				タイムライン名：<input type="text" id="name_" name="name" value="" />
				<br />
			    デフォルトレイアウトID：
				<select id="lid_" name="lid">
					<c:forEach items="${layoutList }" var="layout">
							<option value="${f:h(layout.id.id) }">${f:h(layout.id.id) }:${f:h(layout.xmlModel.name) }</option>
					</c:forEach>
				</select>
			</div>
	</div>
</div>
<footer>
${e:copyright()}
</footer>
</body>
</html>
