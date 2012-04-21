<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="e" uri="http://uq.nskint.co.jp/pd/taglibs/uqSignage-editor"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<html lang="ja">
<head>
<meta charset="UTF-8" />
<title>uqSignage - パスワード更新</title>
<!--[if IE]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>

<![endif]-->
<link rel="stylesheet" href="/css/html5reset.css" type="text/css" media="screen" charset="utf-8"/>
<link rel="stylesheet" href="/css/global.css" type="text/css" media="screen" charset="utf-8"/>
<style type="text/css">

</style>
</head>
<body>
<header>
<h1>パスワード更新</h1>
<t:navbar/>
</header>
<div class="clearfix">
<t:sidemenu/>
<div id="body_contents">
 <span class="errors">${f:h(errors.page)}</span>
 <form action="/user/${f:h(user.uid.name)}/updatePassword" method="post">
  <table class="form">
   <tr><th>${e:label("uid")}:</th><td>${f:h(user.uid.name)}</td></tr>
   <c:if test="${!empty user.password}">
    <tr><th>${e:label("oldPassword")}:</th><td><input type="password" name="oldPassword" class="${f:errorClass('oldPassword', 'error')}" />${e:error('oldPassword')}</td></tr>
   </c:if>
   <tr><th>${e:label("newPassword")}:</th><td><input type="password" name="newPassword" class="${f:errorClass('newPassword', 'error')}" />${e:error('newPassword')}</td></tr>
   <tr><th>${e:label("newPassword2")}:</th><td><input type="password" name="newPassword2" class="${f:errorClass('newPassword2', 'error')}" />${e:error('newPassword2')}</td></tr>
  </table>
  <input type="submit" value="更新" />
 </form>
</div>
</div>
<footer>
${e:copyright()}
</footer>
</body>
</html>