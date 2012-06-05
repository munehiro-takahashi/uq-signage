<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:choose>
	<c:when test="${empty param.type }">
		<c:set var="type" value="ust" />
	</c:when>
	<c:otherwise>
		<c:set var="type" value="${param.type }"/>
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${empty param.width }">
		<c:set var="width" value="600" />
	</c:when>
	<c:otherwise>
		<c:set var="width" value="${param.width }"/>
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${empty param.height }">
		<c:set var="height" value="400" />
	</c:when>
	<c:otherwise>
		<c:set var="height" value="${param.height }"/>
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${empty param.x }">
		<c:set var="x" value="0" />
	</c:when>
	<c:otherwise>
		<c:set var="x" value="${param.x }"/>
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${empty param.y }">
		<c:set var="y" value="0" />
	</c:when>
	<c:otherwise>
		<c:set var="y" value="${param.y }"/>
	</c:otherwise>
</c:choose>
<table>
	<tr>
		<td>種類</td>
		<td>
			<select name="${param.index}_type">
				<option value="UST"
					<c:if test="${type=='ust'}">selected="selected"</c:if>
				>UStream</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>URL</td>
		<td><input type="text" name="${param.index}_url" value="${param.url}" /></td>
	</tr>
</table>
<input type="hidden" id="${param.index}_width"  name="${param.index}_width"  value="<c:out value="${width}" />" />
<input type="hidden" id="${param.index}_height" name="${param.index}_height" value="<c:out value="${height}" />" />
<input type="hidden" id="${param.index }_x"     name="${param.index }_x"     value="<c:out value="${x }" />" />
<input type="hidden" id="${param.index }_y"     name="${param.index }_y"     value="<c:out value="${y }" />" />