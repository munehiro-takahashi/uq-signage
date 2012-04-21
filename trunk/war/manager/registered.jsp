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
<style type="text/css">
th {
  text-align: left;
}
</style>
</head>
<body>
<header>
<h1>${f:h(title)}</h1>
<t:navbar/>
</header>
<div class="clearfix">
<div id="body_contents">
以下の情報で、利用者登録を承りました。<br />
登録メールアドレス宛に、登録確認のメールをお送りしましたので、
24時間以内にメールに記載したURLにアクセスし、登録を完了してください。
  <table class="form">
   <tbody>
    <tr>
     <th>${e:label("uid")}:</th><td>${f:h(uid)}</td>
    </tr>
    <tr>
     <th>${e:label("name")}:</th><td>${f:h(name)}</td>
    </tr>
    <tr>
     <th>${e:label("mail")}:</th><td>${f:h(mail)}</td>
    </tr>
    <tr>
     <th>${e:label("phone")}:</th><td>${f:h(phone)}</td>
    </tr>
    <tr>
     <th>${e:label("zipcode")}:</th><td>${f:h(zipcode)}</td>
    </tr>
    <tr>
     <th>${e:label("address")}:</th><td>${f:h(address)}</td>
    </tr>
   </tbody>
  </table>
</div>
</div>
<footer>
${e:copyright()}
</footer>
</body>
</html>