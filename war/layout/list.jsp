<!DOCTYPE HTML>
<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="e" uri="http://uq.nskint.co.jp/pd/taglibs/uqSignage-editor"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>uqSignage - ${f:h(title)}</title>
<!--[if IE]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<link rel="stylesheet" href="/css/html5reset.css" />
<link rel="stylesheet" href="/css/custom-theme/jquery-ui.custom.css" />
<link rel="stylesheet" href="/css/global.css" type="text/css" media="screen" charset="utf-8"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery-ui.js"></script>
<script type="text/javascript">

$(function(){
	$(".button").button();
});
function onClickCheckBox() {
	$("input[name=chk_lid]:checkbox:checked").length
}

function addLayout() {
	$("#lid").val("");
	$("#listForm").submit();
}

function deleteLayout() {
	$("#listForm").attr("action", "delete");
	$("#listForm").submit();
}

function editLayout(lid) {
	$("#lid").val(lid);
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
		<form id="listForm" action="edit" method="post">
     		<input class="button" type="button" id="add_button" value="追加" onclick="addLayout();" />
     		<input class="button" type="button" id="del_button" value="削除" onclick="deleteLayout();" />
			<table class="data">
				<thead>
					<tr>
						<th>削除</th>
						<th>編集</th>
						<th>レイアウト名</th>
						<th>代表者名</th>
						<th>サイズ</th>
						<th>更新日</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${layoutList }" var="layout">
					<c:set var="xml" value="layout.xmlModel"/>
					<tr>
						<td>
							<input type="checkbox" name="lids" value="${f:h(layout.id.id) }" onclick="onClickCheckBox();" />
						</td>
						<td>
							<input class="button" type="button" value="編集" onclick="editLayout( '${f:h(layout.id.id) }' );" />
						</td>
						<td>${f:h(layout.xmlModel.name)}</td>
						<td>${f:h(manager.name)}</td>
						<td>${f:h(layout.xmlModel.width)}×${f:h(layout.xmlModel.height)}</td>
						<td>${layout.updatedDate }</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			<input type="hidden" id="mid" name="mid" value="${f:h(mid) }" />
			<input type="hidden" id="lid" name="lid" value="" />
  		</form>
	</div>
</div>
<footer>
${e:copyright()}
</footer>

</body>
</html>

