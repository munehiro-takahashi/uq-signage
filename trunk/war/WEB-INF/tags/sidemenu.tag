<%@tag language="java" pageEncoding="UTF-8"%>
<%@tag import="jp.co.nskint.uq.pd.signage.model.User;"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="e" uri="http://uq.nskint.co.jp/pd/taglibs/uqSignage-editor"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<%--
  String MANAGER = User.TYPE_MANAGER;
  String EDITOR = User.TYPE_EDITOR;
  String ADMIN = User.TYPE_ADMINISTRATOR;
--%>
<c:if test="${!empty loginUser}">
 <nav id="sidemenu">
  <ul>
   <c:choose>
    <c:when test="${loginUser.type eq 'manager'}">
     <li><a href="${f:url('/layout/')}?mid=${loginUser.uid.name}">レイアウト編集</a></li>
     <li><a href="${f:url('/timeline/')}?mid=${loginUser.uid.name}">タイムライン編集</a>
     <li><a href="${f:url('/user/')}">編集者管理</a></li>
    </c:when>
    <c:when test="${loginUser.type eq 'editor'}">
     <li><a href="${f:url('/layout/')}?mid=${loginUser.managerRef.model.uid.name}">レイアウト編集</a></li>
     <li><a href="${f:url('/timeline/')}?mid=${loginUser.managerRef.model.uid.name	}">タイムライン編集</a>
    </c:when>
    <c:when test="${loginUser.type eq 'admin'}">
     <li><a href="${f:url('/user/')}">利用者管理</a></li>
    </c:when>
    <c:otherwise>
    </c:otherwise>
   </c:choose>
  </ul>
 </nav>
</c:if>
