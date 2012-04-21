<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<nav id="navbar">
 <ul class="clearfix">
  <c:choose>
   <c:when test="${empty loginUser}">
  <li><a href="/">Top</a></li>
    <li><a href="/login">ログイン</a></li>
    <li><a href="/manager/regist">利用者登録</a></li>
   </c:when>
   <c:otherwise>
    <li><a href="/menu">Top</a></li>
    <li><a href="/user/${loginUser.uid.name}/password">パスワード変更</a></li>
    <li><a href="/">ログアウト</a></li>
    <li class="label">${loginUser.name}</li>
    <li class="label">${loginUser.uid.name}</li>
   </c:otherwise>
  </c:choose>
 </ul>
</nav>