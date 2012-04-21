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
  <c:when test="${loginUser.type eq 'manager' || loginUser.type eq 'editor'}">
   <form id="main" action="/board/regist" method="post">
     <input type="submit" value="追加" />
   </form>
  </c:when>
  <c:otherwise>
  </c:otherwise>
 </c:choose>
 <table class="data">
  <thead>
   <tr>
    <th class="name">名称</th>
    <th class="url">URL</th>
    <th class="button">&nbsp;</th>
   </tr>
  </thead>
  <tbody>
   <c:forEach items="${requestScope.users}" var="user">
    <tr>
     <td class="name">${board }</td>
     <td class="url"></td>
     <td class="button">
      <input type="button" value="編集" onclick="edit('${board.id.id}')"/>
      <input type="button" value="削除" onclick="remove('${board.id.id}')" />
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
