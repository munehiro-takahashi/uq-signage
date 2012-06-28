<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<table>
	<tr>
		<td><label>種類</label></td>
		<td><select name="${param.index}_type">
				<option value="PICASA_ALBUM"
					<c:if test="${'PICASA_ALBUM' == param.type}">selected</c:if>>picasa</option>
		</select></td>
	</tr>
	<tr>
		<td><label>ユーザ名</label></td>
		<td><input type="text" name="${param.index}_user"
			value="${param.user}" /></td>
	</tr>
	<tr>
		<td><label>アルバム名</label></td>
		<td><input type="text" name="${param.index}_album"
			value="${param.album}" />
			<br>
			アルバム内の写真の先頭20枚までを表示します。
			</td>
	</tr>
	<tr>
		<td><label>表示効果</label></td>
		<td><select name="${param.index}_effect">
				<option value="NORMAL"
					<c:if test="${'NORMAL' == param.effect}">selected</c:if>>効果なし</option>
				<option value="FADE"
					<c:if test="${'FADE' == param.effect}">selected</c:if>>フェードイン</option>
				<option value="CIRCLE"
					<c:if test="${'CIRCLE' == param.effect}">selected</c:if>>サークル</option>
		</select></td>
	</tr>
	<tr>
		<td><label>表示間隔</label></td>
		<td><input type="number" name="${param.index}_interval"
			value="${param.interval}" /></td>
	</tr>
</table>
<input type="hidden" id="${param.index}_width"  name="${param.index}_width"  value="<c:out value="${param.width}" />" />
<input type="hidden" id="${param.index}_height" name="${param.index}_height" value="<c:out value="${param.height}" />" />
<input type="hidden" id="${param.index }_x"     name="${param.index }_x"     value="<c:out value="${param.x }" />" />
<input type="hidden" id="${param.index }_y"     name="${param.index }_y"     value="<c:out value="${param.y }" />" />
