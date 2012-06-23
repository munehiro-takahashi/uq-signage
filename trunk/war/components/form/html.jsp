<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<textarea name="${param.index}_html" id="html_${param.index}">${param.value}</textarea>
<script>
$(function(){
	$("#html_${param.index}").cleditor();
})
</script>
