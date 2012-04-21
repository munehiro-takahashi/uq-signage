<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="e" uri="http://uq.nskint.co.jp/pd/taglibs/uqSignage-editor"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<html lang="ja">
<head>
<meta charset="UTF-8" />
<title>uqSignage - Login</title>
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
<h1>Login</h1>
<t:navbar/>
</header>
<div class="clearfix">
<div id="body_contents">
 <span class="errors">${f:h(errors.page)}</span>
 <form action="doLogin" method="post">
  <table class="form">
   <tr><th>${e:label("uid")}:</th><td><input type="text" ${f:text("uid")} class="${f:errorClass('uid', 'error')}"/>${e:error('uid')}</td></tr>
   <tr><th>${e:label("password")}:</th><td><input type="password" name="password" class="${f:errorClass('password', 'error')}" />${e:error('password')}</td></tr>
  </table>
  <input type="submit" value="ログイン" />
 </form>
</div>
</div>
<footer>
${e:copyright()}
</footer>
</body>
</html>