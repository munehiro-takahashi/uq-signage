<!DOCTYPE html>
<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="e" uri="http://uq.nskint.co.jp/pd/taglibs/uqSignage-editor"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<html lang="ja">
<head>
<meta charset="UTF-8" />
<title>${f:h(title)}</title>
<!--[if IE]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<script type="text/javascript" src="/js/jquery.js"></script>
<link rel="stylesheet" href="/css/html5reset.css" type="text/css" media="screen" charset="utf-8"/>
<link rel="stylesheet" href="/css/global.css" type="text/css" media="screen" charset="utf-8"/>
<style type="text/css">
</style>
<script type="text/javascript">
function reload() {
	location.reload(true);
}
setTimeout(reload, ${reloadTime});
</script>
</head>
<body style="width:${layout.width}px;height:${layout.height}px;overflow:hidden;">
<c:forEach items="${layout.components}" var="component" varStatus="compStat">
 <div style="position:absolute;top:${component.y}px;left:${component.x}px;width:${component.width}px;height:${component.height}px;">
  <c:choose>
   <c:when test="${component.class.simpleName == 'Text'}">
    <c:import url="/components/text.jsp"/>
   </c:when>
   <c:when test="${component.class.simpleName == 'Html'}">
    <c:import url="'/components/html.jsp">
     <c:param name="id" value="${compStat.index}"/>
     <c:param name="width" value="${component.width}"/>
     <c:param name="height" value="${component.height}"/>
     <c:param name="value" value="${component.value}"/>
    </c:import>
   </c:when>
   <c:when test="${component.class.simpleName == 'Table'}">
    <c:import url="/components/table.jsp"/>
   </c:when>
   <c:when test="${component.class.simpleName == 'Image'}">
    <c:import url="/components/image.jsp" >
     <c:param name="id" value="${compStat.index}"/>
     <c:param name="width" value="${component.width}"/>
     <c:param name="height" value="${component.height}"/>
     <c:param name="type" value="${component.type}"/>
     <c:param name="effect" value="${component.effect}"/>
     <c:param name="interval" value="${component.interval}"/>
     <c:param name="user" value="${component.user}"/>
     <c:param name="album" value="${component.album}"/>
    </c:import>
   </c:when>
   <c:when test="${component.class.simpleName == 'Audio'}">
    <c:import url="/components/audio.jsp"/>
   </c:when>
   <c:when test="${component.class.simpleName == 'Video'}">
    <c:import url="/components/video.jsp">
     <c:param name="id" value="${compStat.index}"/>
     <c:param name="url" value="${component.url}"/>
     <c:param name="width" value="${component.width}"/>
     <c:param name="height" value="${component.height}"/>
     <c:param name="type" value="${component.type}"/>
     <c:param name="loop" value="${component.loop}"/>
    </c:import>
   </c:when>
   <c:when test="${component.class.simpleName == 'StreamVideo'}">
    <c:import url="/components/stream_video.jsp">
     <c:param name="id" value="${compStat.index}"/>
     <c:param name="url" value="${component.url}"/>
     <c:param name="width" value="${component.width}"/>
     <c:param name="height" value="${component.height}"/>
     <c:param name="type" value="${component.type}"/>
    </c:import>
   </c:when>
   <c:when test="${component.class.simpleName == 'Marquee'}">
    <c:import url="/components/marquee.jsp">
     <c:param name="id" value="${compStat.index}"/>
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
    <c:import url="/components/bar_graph.jsp">
     <c:param name="id" value="${compStat.index}"/>
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
    <c:import url="/components/line_graph.jsp"/>
   </c:when>
   <c:when test="${component.class.simpleName == 'PieGraph'}">
    <c:import url="/components/pie_graph.jsp"/>
   </c:when>
  </c:choose>
 </div>
</c:forEach>
</body>
</html>
