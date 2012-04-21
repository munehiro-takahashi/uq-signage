<!DOCTYPE html>
<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="v" uri="http://uq.nskint.co.jp/pd/taglibs/uqSignage-viewer"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<html lang="ja">
<head>
<meta charset="UTF-8" />
<link type="text/css" rel="stylesheet" href="/css/redmomd/jquery-ui.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery-ui.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#dialog_${id}").dialog({
	    buttons: {    // ボタンを設定
	        // 「編集」ボタンのテキストとイベントハンドラ
	        "編集": function(event) {
	            $("#form_${id}").submit();
	            $(this).dialog("close");
	        },

	        // 「閉じる」ボタンのテキストとイベントハンドラ
	        "閉じる": function() { $(this).dialog("close"); }
	    }
	});
});
</script>
</head>
<body>
<div style="width:300px;height:300px">
<div id="toolbar_${id}" style="height:20px" >
<span style="ui-icon ui-icon-gear" onclick="$('#dialog_${id}').dialog('open')"></span>
<span style="ui-icon ui-icon-trash"></span>
</div>
<div id="dialog_${id}" title="">
	<c:import url="/video/coordinator?id=${id}&url=&type=&width=&height=&loop=false"></c:import>
</div>
</div>
</body>
</html>