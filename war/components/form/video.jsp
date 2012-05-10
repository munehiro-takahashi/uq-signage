<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<table>
	<tr>
		<td><label>種類</label></td>
		<td><select name="${param.index}_type">
				<option value="youtube"
					<c:if test="${'youtube' == param.type}">selected</c:if>>YouTube</option>
				<option value="html5"
					<c:if test="${'html5' == param.type}">selected</c:if>>HTML5</option>
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
