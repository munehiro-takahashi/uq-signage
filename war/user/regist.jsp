<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="e" uri="http://uq.nskint.co.jp/pd/taglibs/uqSignage-editor"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<html lang="ja">
<head>
<meta charset="UTF-8" />
<title>uqSignage - ${f:h(title)}</title>
<!--[if IE]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>

<![endif]-->
<link rel="stylesheet" href="/css/html5reset.css" type="text/css" media="screen" charset="utf-8"/>
<link rel="stylesheet" href="/css/global.css" type="text/css" media="screen" charset="utf-8"/>
</head>
<body>
<header>
<h1>${f:h(title)}</h1>
<t:navbar/>
</header>
<div class="clearfix">
<t:sidemenu/>
<div id="body_contents">
 <span class="errors">${f:h(errors.page)}</span>
 <form id="main_form" action="insert" method="post">
  <table class="form">
   <tbody>
    <tr>
     <th>${e:label("uid")}:</th><td><input type="text" ${f:text("uid")} required="required" maxlength="16" size="16" class="${f:errorClass('uid', 'error')}"/>${e:error('uid')}</td>
    </tr>
    <tr>
     <th>${e:label("name")}:</th><td><input type="text" ${f:text("name")} required="required" maxlength="40" size="40" class="${f:errorClass('name', 'error')}"/>${e:error('name')}</td>
    </tr>
    <tr>
     <th>${e:label("mail")}:</th><td><input type="email" ${f:text("mail")} required="required" maxlength="256" size="30" class="${f:errorClass('mail', 'error')}"/>${e:error('mail')}</td>
    </tr>
   </tbody>
  </table>
  <input type="submit" value="${e:label('regist')}" />
 </form>
</div>
</div>
<footer>
${e:copyright()}
</footer>
</body>
</html>