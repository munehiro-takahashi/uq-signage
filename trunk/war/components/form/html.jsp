<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<textarea name="${param.index}_html" id="${param.index}_html">${param.value}</textarea>
<input type="hidden" id="${param.index}_width"  name="${param.index}_width"  value="<c:out value="${param.width}" />" />
<input type="hidden" id="${param.index}_height" name="${param.index}_height" value="<c:out value="${param.height}" />" />
<input type="hidden" id="${param.index }_x"     name="${param.index }_x"     value="<c:out value="${param.x }" />" />
<input type="hidden" id="${param.index }_y"     name="${param.index }_y"     value="<c:out value="${param.y }" />" />
<script type="text/javascript">
$(function(){
	$("#${param.index}_html").cleditor();
})
</script>
