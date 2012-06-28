<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="v" uri="http://uq.nskint.co.jp/pd/taglibs/uqSignage-viewer"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:choose>
 <c:when test="${param.type == 'youtube'}">
  <c:choose>
   <c:when test="${param.loop}">
    <iframe id="${param.id}" width="${param.width}" height="${param.height}" src="${param.url}?autoplay=1&loop=1" frameborder="0" allowfullscreen></iframe>
   </c:when>
   <c:otherwise>
    <iframe id="${param.id}" width="${param.width}" height="${param.height}" src="${param.url}?autoplay=1" frameborder="0" allowfullscreen></iframe>
   </c:otherwise>
  </c:choose>
 </c:when>
 <c:when test="${param.type == 'html5'}">
  <c:choose>
   <c:when test="${param.loop}">
    <video id="${param.id}" width="${param.width}" height="${param.height}" src="${param.url}" autoplay loop />
   </c:when>
   <c:otherwise>
    <video id="${param.id}" width="${param.width}" height="${param.height}" src="${param.url}" autoplay />
   </c:otherwise>
  </c:choose>
 </c:when>
</c:choose>