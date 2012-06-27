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
		<td>表示内容</td>
		<td><input type="text" name="${index}_value" value="${value}" /></td>
	</tr>
	<tr>
		<td>フォント</td>
		<td><input type="text" name="${index}_fontFamily" value="${fontFamily}" /></td>
	</tr>
	<tr>
		<td>フォントサイズ</td>
		<td><input type="text" name="${index}_fontSize" value="${fontSize}" /></td>
	</tr>
	<tr>
		<td>フォントの色</td>
		<td><input type="text" name="${index}_fontColor" value="${fontColor}" /></td>
	</tr>
	<tr>
		<td>フォントスタイル</td>
		<td><input type="text" name="${index}_fontStyle" value="${fontStyle}" /></td>
	</tr>
	<tr>
		<td>画面端でのバウンド</td>
		<td>
			<select name="${index}_bounce">
				<option value="true"
					<c:if test="${bounce=='true'}">selected="selected"</c:if>
				>true</option>
				<option value="false"
					<c:if test="${bounce=='false'}">selected="selected"</c:if>
				>false</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>繰り返し回数</td>
		<td><input type="text" name="${index}_repeat" value="${repeat}" /></td>
	</tr>
	<tr>
		<td>テキストの流れる方向</td>
		<td>
			<select name="${index}_direction">
				<option value="RIGHT"
					<c:if test="${direction=='right'}">selected="selected"</c:if>
				>Right</option>
				<option value="LEFT"
					<c:if test="${direction=='left'}">selected="selected"</c:if>
				>Left</option>
				<option value="UP"
					<c:if test="${direction=='up'}">selected="selected"</c:if>
				>Top</option>
				<option value="DOWN"
					<c:if test="${direction=='down'}">selected="selected"</c:if>
				>Bottom</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>テキストの書き方</td>
		<td>
			<select name="${index}_orientation">
				<option value="HORIZONTAL"
					<c:if test="${orientation=='horizontal'}">selected="selected"</c:if>
				>横書き</option>
				<option value="VERTICAL"
					<c:if test="${orientation=='vertical'}">selected="selected"</c:if>
				>縦書き</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>スピード</td>
		<td><input type="text" name="${index}_speed" value="${speed}" /></td>
	</tr>
</table>
<input type="hidden" id="${param.index}_width"  name="${param.index}_width"  value="<c:out value="${width}" />" />
<input type="hidden" id="${param.index}_height" name="${param.index}_height" value="<c:out value="${height}" />" />
<input type="hidden" id="${param.index }_x"     name="${param.index }_x"     value="<c:out value="${x }" />" />
<input type="hidden" id="${param.index }_y"     name="${param.index }_y"     value="<c:out value="${y }" />" />
