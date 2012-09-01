<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
	<c:when test="${empty param.type }">
		<c:set var="type" value="youtube" />
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
</c:choose><table>
	<tr>
		<td><label>種類</label></td>
		<td><select name="${param.index}_type">
				<option value="YOUTUBE"
					<c:if test="${'YOUTUBE' == param.type}">selected</c:if>>YouTube</option>
				<option value="HTML_5"
					<c:if test="${'HTML_5' == param.type}">selected</c:if>>HTML5</option>
		</select></td>
	</tr>
	<tr>
		<td><label>URL</label></td>
		<td><input type="url" name="${param.index}_url"
			value="${param.url}" /></td>
	</tr>
	<tr>
		<td><label>繰り返し</label></td>
		<td><input type="checkbox" name="${param.index}_loop"
			value="${param.loop}" /></td>

	</tr>
</table>
<input type="hidden" id="${param.index}_width"  name="${param.index}_width"  value="<c:out value="${width}" />" />
<input type="hidden" id="${param.index}_height" name="${param.index}_height" value="<c:out value="${height}" />" />
<input type="hidden" id="${param.index }_x"     name="${param.index }_x"     value="<c:out value="${x }" />" />
<input type="hidden" id="${param.index }_y"     name="${param.index }_y"     value="<c:out value="${y }" />" />