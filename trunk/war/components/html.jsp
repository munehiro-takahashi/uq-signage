<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="v" uri="http://uq.nskint.co.jp/pd/taglibs/uqSignage-viewer"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<div id="${param.id}" width="${param.width}" height="${param.height}">
${param.value}
</div>
