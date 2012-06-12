<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="e" uri="http://uq.nskint.co.jp/pd/taglibs/uqSignage-editor"%>
<!DOCTYPE HTML>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>Layout Editor</title>
<link rel="stylesheet" href="/css/html5reset.css" />
<link rel="stylesheet" href="/css/custom-theme/jquery-ui.custom.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery-ui.js"></script>
<style>
.component {
	border: solid 1px #000000;
	background-color: lightgray;
	position: absolute;
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


// 設定ダイアログを表示する
function openEditDialog(id) {
	var dialogName = id + "_dialog";
	$("#" + dialogName).dialog({
		buttons: {
			"登録" : function(event) {
				$(this).dialog("close");
			},
			"キャンセル" : function(event) {
				$(this).dialog("close");
			}
		},
		draggable: true,
		resizable: true,
		modal: true,
		closeText: "",
		close: function(event) {
			// ダイアログクローズ時にダイアログの要素をフォーム内に戻す
			$(this).parent().appendTo("#edit-form");
		}
	});
}

// レイアウトを表示する。
function saveLayout() {
	$("div.component").each(
		function(){
			var index  = getIndexFromAttr($(this).attr("id"));
			$("#" + index + "_x").val(getPixel($(this).css("left")));
			$("#" + index + "_y").val(getPixel($(this).css("top")));
			$("#" + index + "_width").val(toNumber($(this).width()));
			$("#" + index + "_height").val(toNumber($(this).height()));
		}
	);
	$("#sum").val( sum );
	$("form").submit();
}

// スタイル値にpxが含まれる文字列から数値部分を取得する
function getPixel(value) {
	if(value == null || value == "") {
		return 0;
	}
	
	if(value.match(/^([0-9]+)px$/)) {
		return toNumber(RegExp.$1);
	}
	
	return 0;
}

// 文字列から数値を取得する。
function toNumber(value) {
	if(value == null || value == "") {
		return 0;
	}
	
	if(isNaN(value)) {
		return 0;
	}
	
	return parseInt(value);
}

// 引数のHTML属性からインデックスを取得する。
function getIndexFromAttr( attr ) {
	// 検索対象の属性が指定されていない場合
	if(attr == null || attr == "") {
		return -1;
	}
	
	// インデックスが取得できない場合
	if(!attr.match("^([0-9]+)_.+")) {
		return -1;
	}
	
	return toNumber(RegExp.$1);
}

function back() {
	$("#edit-form").attr("action", "/layout/").submit();
}

/// コンポーネントの追加ダイアログを表示する。
function openAddComponentDialog() {
	$("#addComponentDialog :checkbox:checked").removeAttr("checked");
	$("#addComponentDialog").dialog({
		buttons: {
			"登録" : function(event) {
				$("#addComponentDialog :checkbox:checked").each(requestNewComponent);
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

// 新規コンポーネントをリクエストする。
function requestNewComponent() {
	var index = $(this).val();
	var cls = $("#addComp_class_" + index).val();
	var url = $("#addComp_url_" + index).val();
	
	var index = sum++;
	url += "?index=" + index;
	$.get(url,function(data){ putNewComponent(cls, index, data) });
}

// 新規コンポーネントを配置する。
function putNewComponent(cls, index, data) {
	editPanel = $("#edit-panel");
	editForm = $("#edit-form");
	editForm.append($("<input/>")
		.attr("type", "hidden")
		.attr("name", index + "_ComponentClassName")
		.val(cls)
	);
	
	dialog = $("<div/>")
		.addClass("edit_dialog")
		.attr("id", cls + "_" + index + "_dialog")
		.attr("title", "コンポーネントの設定")
		.html(data)
		.hide();
	editForm.append(dialog);
	
	var width = $("#" + index + "_width").val();
	var height = $("#" + index + "_height").val();
	
	component = $("<div/>")
	.addClass("component")
	.attr("id", index + "_component")
	.html(cls + "クラスのダミー")
	.resizable().draggable()
	.css("top", editPanel.height() / 2 + height / 2)
	.css("left", editPanel.width() / 2 - width / 2)
	.css("width", width)
	.css("height", height);
	
	componentCtrl = $("<div/>").addClass("component_ctrl")
	ctrl = $("<input/>")
		.attr("type", "button")
		.attr("value", "編集")
		.click(function(){ openEditDialog(cls+"_" + index); });
		
	componentCtrl.append(ctrl);
	component.append(componentCtrl);
	editForm.append(component);
}

</script>
</head>
<body>
	<div id="layout-ctrl">
		<input type="button" value="戻る" onclick="back()" />
<!-- 		<span id="layout-name-input" style="margin-left: 30px;">レイアウト名：<input type="text" size="60" /></span> -->
		<input type="button" value="要素を追加" onclick="openAddComponentDialog();" />
		<input type="button" value="保存" onclick="saveLayout();" />
	</div>
	<div id="edit-panel">
		<form id="edit-form" action="/layout/save" method="post">
			<c:forEach items="${layout.components}" var="component" varStatus="stat">
				<div class="component" id="${stat.index }_component" style="top:${component.y}px;left:${component.x}px;width:${component.width}px;height:${component.height}px;">
					<div class="component_ctrl">
						<input type="button" value="編集" onclick="openEditDialog('${component.class.simpleName}_${stat.index }')" />
					</div>
					${component.class.simpleName}クラスのダミー<br/>
				</div>
				<input type="hidden" name="${stat.index }_ComponentClassName" value="${component.class.simpleName}" />
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
								<c:param name="x"      value="${component.x}"/>
								<c:param name="y"      value="${component.y}"/>
							</c:import>
						</c:when>
						<c:when test="${component.class.simpleName == 'Marquee'}">
							<c:import url="/marquee/form">
							</c:import>
						</c:when>
						<c:when test="${component.class.simpleName == 'BarGraph'}">
							<c:import url="/bar_graph/form"/>
						</c:when>
						<c:when test="${component.class.simpleName == 'LineGraph'}">
							<c:import url="/line_graph/form"/>
						</c:when>
						<c:when test="${component.class.simpleName == 'PieGraph'}">
							<c:import url="/pie_graph/form"/>
						</c:when>
					</c:choose>
				</div>
			</c:forEach>
			<input type="hidden" name="lid" value="${lid }" />
			<input type="hidden" name="mid" value="${mid }" />
			<input type="hidden" id="sum" name="sum" value="" />
			
			<div id="addComponentDialog" style="display:none" title="コンポーネントの追加">
				<table>
					<tr>
						<td><input type="checkbox" value="1" /></td>
						<td>
							テキスト
							<input type="hidden" id="addComp_class_1" value="Text" />
							<input type="hidden" id="addComp_url_1" value="/text/form" />
						</td>
					</tr>
					<tr>
						<td><input type="checkbox" value="2" /></td>
						<td>
							HTML
							<input type="hidden" id="addComp_class_2" value="Html" />
							<input type="hidden" id="addComp_url_2" value="/html/form" />
						</td>
					</tr>
					<tr>
						<td><input type="checkbox" value="3" /></td>
						<td>
							テーブル
							<input type="hidden" id="addComp_class_3" value="Table" />
							<input type="hidden" id="addComp_url_3" value="/table/form" />
						</td>
					</tr>
					<tr>
						<td><input type="checkbox" value="4" /></td>
						<td>
							画像
							<input type="hidden" id="addComp_class_4" value="Image" />
							<input type="hidden" id="addComp_url_4" value="/image/form" />
						</td>
					</tr>
					<tr>
						<td><input type="checkbox" value="5" /></td>
						<td>
							音楽
							<input type="hidden" id="addComp_class_5" value="Audio" />
							<input type="hidden" id="addComp_url_5" value="/audio/form" />
						</td>
					</tr>
					<tr>
						<td><input type="checkbox" value="6" /></td>
						<td>
							動画
							<input type="hidden" id="addComp_class_6" value="Video" />
							<input type="hidden" id="addComp_url_6" value="/video/form" />
						</td>
					</tr>
					<tr>
						<td><input type="checkbox" value="7" /></td>
						<td>
							ストリームビデオ
							<input type="hidden" id="addComp_class_7" value="StreamVideo" />
							<input type="hidden" id="addComp_url_7" value="/stream/form" />
						</td>
					</tr>
					<tr>
						<td><input type="checkbox" value="8" /></td>
						<td>
							マーキー
							<input type="hidden" id="addComp_class_8" value="Marquee" />
							<input type="hidden" id="addComp_url_8" value="/marquee/form" />
						</td>
					</tr>
					<tr>
						<td><input type="checkbox" value="9" /></td>
						<td>
							棒グラフ
							<input type="hidden" id="addComp_class_9" value="BarGraph" />
							<input type="hidden" id="addComp_url_9" value="/bar_graph/form" />
						</td>
					</tr>
					<tr>
						<td><input type="checkbox" value="10" /></td>
						<td>
							線グラフ
							<input type="hidden" id="addComp_class_10" value="LineGraph" />
							<input type="hidden" id="addComp_url_10" value="/line_graph/form" />
						</td>
					</tr>
					<tr>
						<td><input type="checkbox" value="11" /></td>
						<td>
							円グラフ
							<input type="hidden" id="addComp_class_11" value="PieGraph" />
							<input type="hidden" id="addComp_url_11" value="/pie_graph/form" />
						</td>
					</tr>
				</table>
			</div>
		</form>
	</div>
</body>
</html>