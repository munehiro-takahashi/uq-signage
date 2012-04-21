<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<form action="/video/edit?id=${id}" id="form_${id}">
	<label>url:</label><input type="url" name="url" id="url_${id}" value="${url}" /><br />
	<label>type:</label><select name="type" id="type_${id}">
		<option value="youtube"
		<c:if test="${'youtube' == type}">selected</c:if>
		>YouTube</option>
		<option value="html5"
		<c:if test="${'html5' == type}">selected</c:if>
		>HTML5</option>
	</select><br />
	<label>width:</label><input type="number" name="width" id="width_${id}" value="${width}" /><br />
	<label>height:</label><input type="number" name="height" id="height_${id}" value="${height}" /><br />
	<label>loop:</label><input type="checkbox" name="loop" id="loop_${id}" value="${loop}" /><br />
</form>
