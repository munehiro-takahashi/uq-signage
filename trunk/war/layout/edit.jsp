<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<!DOCTYPE HTML>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>Layout Editor</title>
<link rel="stylesheet" href="/css/html5reset.css" />
<link rel="stylesheet" href="/css/jquery-ui/smoothness/jquery-ui.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery-ui.js"></script>
<style>
.component {
	width: 400px;
	height: 300px;
	border: solid 1px #000000;
	background-color: lightgray;
}

.component_ctrl {
	position: absolute;
	top: 0px;
	right: 0px;
	opacity: 0.2;
	-webkit-transition: opacity 0.2s linear;
	-moz-transition: 0.2s;
}
.component_ctrl:hover {
	opacity: 1.0;
}

.edit_dialog {
	background-color: #FFFFFF;
	border: solid 1px #000000;
}
#layout-ctrl {
	background-color: #e6ebf1;
	width:100%;
	position: fixed;
	top: 0px;
	padding: 5px;
	vertical-align: middle;
  	border:1px solid #999;
  	z-index: 1;
	opacity: 0.2;
	-webkit-transition: opacity 0.2s linear;
	-moz-transition: 0.2s;
}
#layout-ctrl:hover {
	opacity:1.0;
}

#resize_test {
	width: 400px;
	height: 250px;
	background-color: red;
}
</style>
<script type="text/javascript">

var sum = ${fn:length(layout.components)};

$(function(){
	$(".component").resizable().draggable();
//	addComponentCtrl();
});

function openEditDialog(id) {
	var dialogName = id + "_dialog";
	$("#" + dialogName).dialog({
		buttons: {
			"登録" : function(event) {
				// TODO: 登録処理を実行する
				$(this).dialog("close");
			},
			"キャンセル" : function(event) {
				$(this).dialog("close");
			}
		},
		draggable: true,
		resizable: true,
		modal: true,
		closeText: ""
	});
}

function saveLayout() {
	$("div.component").each(
		function(){
			var index  = getIndexFromAttr($(this).attr("id"));
			$("#" + index + "_x").val(getPixel($(this).css("left")));
			$("#" + index + "_y").val(getPixel($(this).css("top")));
			$("#" + index + "_width").val(getNumber($(this).width()));
			$("#" + index + "_height").val(getNumber($(this).height()));
		}
	);
	$("#sum").val( sum );
	$("form").submit();
}

function getPixel(value) {
	if(value == null || value == "") {
		return 0;
	}

	if(value.match(/^([0-9]+)px$/)) {
		return getNumber(RegExp.$1);
	}

	return 0;
}
function getNumber(value) {
	if(value == null || value == "") {
		return 0;
	}

	if(isNaN(value)) {
		return 0;
	}

	return parseInt(value);
}

function getIndexFromAttr( attr ) {
	// 検索対象の属性が指定されていない場合
	if(attr == null || attr == "") {
		return -1;
	}

	// インデックスが取得できない場合
	if(!attr.match("^([0-9]+)_.+")) {
		return -1;
	}

	return getNumber(RegExp.$1);
}

function back() {
	$("#editForm").attr("action", "/layout/").submit();
}

</script>
</head>
<body>
	<div id="layout-ctrl">
		<input type="button" value="戻る" onclick="back()" />
<!-- 		<span id="layout-name-input" style="margin-left: 30px;">レイアウト名：<input type="text" size="60" /></span> -->
		<input type="button" value="要素を追加" onclick="javascript:alert('【未作成】要素を追加します。');" />
		<input type="button" value="保存" onclick="saveLayout();" />
	</div>
	<div id="edit-panel">
		<form id="editForm" action="/layout/save" method="post">
			<c:forEach items="${layout.components}" var="component" varStatus="stat">
				<div class="component" id="${stat.index }_component" style="top:${component.y}px;left:${component.x}px;width:${component.width}px;height:${component.height}px;">
					<div class="component_ctrl">
						<input type="button" value="編集" onclick="openEditDialog('${component.class.simpleName}_${stat.index }')" />
					</div>
					${component.class.simpleName}クラスのダミー<br/>
				</div>
				<input type="hidden" name="${stat.index }_ComponentClassName" value="${component.class.name}" />
				<div id="${component.class.simpleName}_${stat.index }_dialog"  class="edit_dialog" style="display:none;"
					title="コンポーネントの設定">
					<c:choose>
						<c:when test="${component.class.simpleName == 'Text'}">
							<c:import url="/text/form"/>
						</c:when>
						<c:when test="${component.class.simpleName == 'Html'}">
							<c:import url="/html/form"/>
						</c:when>
						<c:when test="${component.class.simpleName == 'Table'}">
							<c:import url="/table/form"/>
						</c:when>
						<c:when test="${component.class.simpleName == 'Image'}">
							<c:import url="/image/form"/>
						</c:when>
						<c:when test="${component.class.simpleName == 'Audio'}">
							<c:import url="/audio/form"/>
						</c:when>
						<c:when test="${component.class.simpleName == 'Video'}">
							<c:import url="/video/form">
								<c:param name="url"    value="${component.url}"/>
								<c:param name="width"  value="${component.width}"/>
								<c:param name="height" value="${component.height}"/>
								<c:param name="type"   value="${component.type}"/>
								<c:param name="loop"   value="${component.loop}"/>
							</c:import>
						</c:when>
						<c:when test="${component.class.simpleName == 'StreamVideo'}">

							<c:import url="/components/form/stream_video.jsp">
								<c:param name="index"  value="${stat.index}"/>
								<c:param name="url"    value="${component.url}"/>
								<c:param name="width"  value="${component.width}"/>
								<c:param name="height" value="${component.height}"/>
								<c:param name="type"   value="${component.type}"/>
							</c:import>
						</c:when>
						<c:when test="${component.class.simpleName == 'Marquee'}">
							<c:import url="/marquee/form">
								<c:param name="index"  value="${stat.index}"/>
								<c:param name="content" value="${component.value}"/>
								<c:param name="fontFamily" value="${component.fontFamily}"/>
								<c:param name="fontSize" value="${component.fontSize}"/>
								<c:param name="fontColor" value="${component.fontColor}"/>
								<c:param name="fontStyle" value="${component.fontStyle}"/>
								<c:param name="bounce" value="${component.bounce}"/>
								<c:param name="repeat" value="${component.repeat}"/>
								<c:param name="direction" value="${component.direction}"/>
								<c:param name="writtenDirection" value="${component.orientation}"/>
								<c:param name="speed" value="${component.speed}"/>
								<c:param name="width" value="${component.width}"/>
								<c:param name="height" value="${component.height}"/>
							</c:import>
						</c:when>
						<c:when test="${component.class.simpleName == 'BarGraph'}">
							<c:import url="/bar_graph/form">
								<c:param name="index"  value="${stat.index}"/>
								<c:param name="data" value="${component.data}"/>
								<c:param name="data_caption" value="${component.dataCaption}"/>
								<c:param name="scale_caption" value="${component.scaleCaption}"/>
								<c:param name="scale_max" value="${component.scaleMax}"/>
								<c:param name="scale_min" value="${component.scaleMin}"/>
								<c:param name="scale_step" value="${component.scaleStep}"/>
								<c:param name="orientation" value="${component.orientation}"/>
								<c:param name="bar_width" value="${component.barWidth}"/>
								<c:param name="bar_margin" value="${component.barMargin}"/>
								<c:param name="width" value="${component.width}"/>
								<c:param name="height" value="${component.height}"/>
							</c:import>
						</c:when>
						<c:when test="${component.class.simpleName == 'LineGraph'}">
							<c:import url="/line_graph/form"/>
						</c:when>
						<c:when test="${component.class.simpleName == 'PieGraph'}">
							<c:import url="/pie_graph/form"/>
						</c:when>
					</c:choose>
					<input type="hidden" id="${stat.index }_x"      name="${stat.index }_x"      value="${component.x }" />
					<input type="hidden" id="${stat.index }_y"      name="${stat.index }_y"      value="${component.y }" />
					<input type="hidden" id="${stat.index }_width"  name="${stat.index }_width"  value="${component.width }" />
					<input type="hidden" id="${stat.index }_height" name="${stat.index }_height" value="${component.height }" />
				</div>
			</c:forEach>
			<input type="hidden" name="lid" value="${lid }" />
			<input type="hidden" name="mid" value="${mid }" />
			<input type="hidden" id="sum" name="sum" value="" />
		</form>
	</div>
</body>
</html>