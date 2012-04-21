<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<table>
	<tr>
		<td>表示データ</td>
		<td><input type="text" name="${index}_data" value="${data}" /></td>
	</tr>
	<tr>
		<td>グラフのタイトル</td>
		<td><input type="text" name="${index}_dataCaption" value="${dataCaption}" /></td>
	</tr>
	<tr>
		<td>目盛のラベル</td>
		<td><input type="text" name="${index}_scaleCaption" value="${scaleCaption}" /></td>
	</tr>
	<tr>
		<td>目盛の最小値</td>
		<td><input type="text" name="${index}_scaleMax" value="${scaleMin}" /></td>
	</tr>
	<tr>
		<td>目盛の最大値</td>
		<td><input type="text" name="${index}_scaleMin" value="${scaleMax}" /></td>
	</tr>
	<tr>
		<td>目盛の間隔</td>
		<td><input type="text" name="${index}_scaleStep" value="${scaleStep}" /></td>
	</tr>
	<tr>
		<td>グラフの方向</td>
		<td>
			<select name="${index}_orientation">
				<option value="horizontal"
					<c:if test="${orientation=='horizontal'}">selected="selected"</c:if>
				>横</option>
				<option value="vertical"
					<c:if test="${orientation=='vertical'}">selected="selected"</c:if>
				>縦</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>バーの幅</td>
		<td><input type="text" name="${index}_barWidth" value="${barWidth}" /></td>
	</tr>
	<tr>
		<td>バーの間隔</td>
		<td><input type="text" name="${index}_barMargin" value="${barMargin}" /></td>
	</tr>
	<tr>
		<td>幅</td>
		<td><input type="text" name="${index}_width" value="${width}" /></td>
	</tr>
	<tr>
		<td>高さ</td>
		<td><input type="text" name="${index}_height" value="${height}" /></td>
	</tr>
</table>
