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
<t:sidemenu/>
<div id="body_contents">
 <span class="errors">${f:h(errors.page)}</span>
 <form id="main_form" action="update" method="post">
  <table>
   <tbody>
    <tr>
     <th>ユーザID:</th><td><input type="text" ${f:text("uid")} readonly="readonly" maxlength="16" size="16" class="${f:errorClass('uid', 'error')}"/>${e:error('uid')}</td>
    </tr>
    <tr>
     <th>氏名:</th><td><input type="text" ${f:text("name")} required="required" maxlength="40" size="40" class="${f:errorClass('name', 'error')}"/>${e:error('name')}</td>
    </tr>
    <tr>
     <th>メールアドレス:</th><td><input type="email" ${e:text("mail")} required="required" maxlength="256" size="30" class="${f:errorClass('mail', 'error')}"/>${e:error('mail')}</td>
    </tr>
    <tr>
     <th>電話番号:</th><td><input type="tel" ${e:text("phone")} required="required" maxlength="13" size="13" class="${f:errorClass('phone', 'error')}"/>${e:error('phone')}</td>
    </tr>
    <tr>
     <th>郵便番号:</th><td><input type="text" ${f:text("zipcode")} required="required" maxlength="8" size="8" class="${f:errorClass('zipcode', 'error')}">${e:error('zipcode')}</td>
    </tr>
    <tr>
     <th>住所:</th><td><input type="text" ${e:text("address")} required="required" maxlength="256" size="50" class="${f:errorClass('address', 'error')}"/>${e:error('address')}</td>
    </tr>
   </tbody>
  </table>
  <input type="submit" value="登録" />
 </form>
</div>
</div>
<footer>
${e:copyright()}
</footer>
</body>
</html>