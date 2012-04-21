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
<h1>${f:h(title)}</h1>
<!--[if IE]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<link rel="stylesheet" href="/css/html5reset.css" type="text/css" media="screen" charset="utf-8"/>
<link rel="stylesheet" href="/css/global.css" type="text/css" media="screen" charset="utf-8"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">

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
<t:navbar/>
</header>
<div class="clearfix">
	<t:sidemenu/>
	<div id="body_contents">
		<form id="listForm" action="edit" method="post">
     		<input type="button" id="add_button" value="追加" onclick="addLayout();" />
     		<input type="button" id="del_button" value="削除" onclick="deleteLayout();" />
			<table class="data">
				<thead>
					<tr>
						<td>削除</td>
						<td>編集</td>
						<td>代表者名</td>
						<td>登録者</td>
						<td>更新日</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${layoutList }" var="layout">
					<tr>
						<td>
							<input type="checkbox" name="lids" value="${f:h(layout.id.id) }" onclick="onClickCheckBox();" />
						</td>
						<td>
							<input type="button" value="編集" onclick="editLayout( '${f:h(layout.id.id) }' );" />
						</td>
						<td>${manager.name }</td>
						<td>${layout.registeredDate }</td>
						<td>${layout.updatedDate }</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			<input type="hidden" id="mid" name="mid" value="${mid }" />
			<input type="hidden" id="lid" name="lid" value="" />
  		</form>
	</div>
</div>
<footer>
${e:copyright()}
</footer>

</body>
</html>

