<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<table>
	<tr>
		<td>種類</td>
		<td>
			<select name="${param.index}_type">
				<option value="ust"
					<c:if test="${param.type=='ust'}">selected="selected"</c:if>
				>UStream</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>URL</td>
		<td><input type="url" name="${param.index}_url" value="${param.url}" /></td>
	</tr>
</table>
