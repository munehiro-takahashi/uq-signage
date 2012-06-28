<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<table>
	<tr>
		<td>表示データ</td>
		<td><input type="text" name="${param.index}_data" value="${param.data}" /></td>
	</tr>
	<tr>
		<td>グラフのタイトル</td>
		<td><input type="text" name="${param.index}_dataCaption" value="${param.dataCaption}" /></td>
	</tr>
	<tr>
		<td>目盛のラベル</td>
		<td><input type="text" name="${param.index}_scaleCaption" value="${param.scaleCaption}" /></td>
	</tr>
	<tr>
		<td>目盛の最小値</td>
		<td><input type="text" name="${param.index}_scaleMax" value="${param.scaleMin}" /></td>
	</tr>
	<tr>
		<td>目盛の最大値</td>
		<td><input type="text" name="${param.index}_scaleMin" value="${param.scaleMax}" /></td>
	</tr>
	<tr>
		<td>目盛の間隔</td>
		<td><input type="text" name="${param.index}_scaleStep" value="${param.scaleStep}" /></td>
	</tr>
	<tr>
		<td>グラフの方向</td>
		<td>
			<select name="${param.index}_orientation">
				<option value="horizontal"
					<c:if test="${param.orientation=='horizontal'}">selected="selected"</c:if>
				>横</option>
				<option value="vertical"
					<c:if test="${param.orientation=='vertical'}">selected="selected"</c:if>
				>縦</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>バーの幅</td>
		<td><input type="text" name="${param.index}_barWidth" value="${param.barWidth}" /></td>
	</tr>
	<tr>
		<td>バーの間隔</td>
		<td><input type="text" name="${param.index}_barMargin" value="${param.barMargin}" /></td>
	</tr>
	<tr>
		<td>幅</td>
		<td><input type="text" name="${param.index}_width" value="${param.width}" /></td>
	</tr>
	<tr>
		<td>高さ</td>
		<td><input type="text" name="${param.index}_height" value="${param.height}" /></td>
	</tr>
</table>
