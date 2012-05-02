<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="v" uri="http://uq.nskint.co.jp/pd/taglibs/uqSignage-viewer"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:choose>
 <c:when test="${type == 'UST'}">
  <iframe src="${url}?autoplay=true"
          width="${width}" height="${v:ust_widthToHeight(width)}"
          scrolling="no" frameborder="0"
          style="border: 0px none transparent;"></iframe>
 </c:when>
</c:choose>