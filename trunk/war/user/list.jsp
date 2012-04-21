<!DOCTYPE html>
<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
th.name {
}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
function edit(uid) {
	$("#main").attr("action", "/user/"+uid+"/edit");
	$("#main").submit();
}
function initPassword(uid, name) {
	$("#main").attr("action", "/user/"+uid+"/initPassword");
	$("#main").submit();
}
function remove(uid, name) {
	confirm("ユーザ");
	$("#main").attr("action", "/user/"+uid+"/remove");
	$("#main").submit();
}
</script>
</head>
<body>
<header>
<h1>${f:h(title)}</h1>
<t:navbar/>
</header>
<div class="clearfix">
<t:sidemenu/>
<div id="body_contents">
 <c:choose>
  <c:when test="${loginUser.type eq 'admin'}">
   <form id="main" action="/admin/regist" method="post">
     <input type="submit" value="管理者追加" />
   </form>
  </c:when>
  <c:when test="${loginUser.type eq 'manager'}">
   <form id="main" action="/editor/regist" method="post">
     <input type="submit" value="編集者追加" />
   </form>
  </c:when>
  <c:otherwise>
   <form id="main" action="" method="post">
   </form>
  </c:otherwise>
 </c:choose>
 <table class="data">
  <thead>
   <tr>
    <th class="uid">ID</th>
    <th class="name">氏名</th>
    <th class="type">種別</th>
    <th class="button">&nbsp;</th>
   </tr>
  </thead>
  <tbody>
   <c:forEach items="${requestScope.users}" var="user">
    <tr>
     <td class="uid">${user.uid.name}</td>
     <td class="name">${user.name}</td>
     <td class="type">
      <c:choose>
       <c:when test="${user.type eq 'admin'}">管理者</c:when>
       <c:when test="${user.type eq 'editor'}">編集者</c:when>
       <c:when test="${user.type eq 'manager'}">代表者</c:when>
       <c:otherwise>不明</c:otherwise>
      </c:choose>
     </td>
     <td class="button">
      <input type="button" value="編集" onclick="edit('${user.uid.name}')"/>
      <c:if test="${loginUser.type eq 'admin'}">
       <%-- パスワード初期化は管理者のみ --%>
       <input type="button" value="パスワード初期化" onclick="initPassword('${user.uid.name}', '${user.name}')" />
      </c:if>
      <input type="button" value="削除" onclick="remove('${user.uid.name}', '${user.name}')" />
     </td>
    </tr>
   </c:forEach>
  </tbody>
 </table>
</div>
</div>
<footer>
${e:copyright()}
</footer>
</body>
</html>
