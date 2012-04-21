<%@ page language="java" contentType="text/html; charset=Shift_JIS"
    pageEncoding="Shift_JIS" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Shift_JIS">
<title>Params</title>
</head>
<body>
  <h1>Params: ${x}</h1>
<pre>// action method
    @ActionPath("put")
    public Navigation post(@RequestParam("foo") String foo,
            @RequestParam(value = "bar", defaultValue = "") String bar,
            @RequestParam(value = "poo", defaultValue = "poo") String poo)
            throws IOException {
        return response("foo=" + foo + ", bar=" + bar + ", poo=" + poo);
    }
</pre>
  <p><a href="put">put</a></p>
  <p><a href="put?foo=xx">put?foo=xx</a></p>
  <p><a href="put?bar=yy">put?bar=yy</a></p>
  <p><a href="put?poo=zz">put?poo=zz</a></p>
  <p><a href="put?foo=xx&bar=yy">put?foo=xx&bar=yy</a></p>
  <p><a href="put?bar=yy&poo=zz">put?bar=yy&poo=zz</a></p>
  <p><a href="put?foo=xx&bar=yy&poo=zz">put?foo=xx&bar=yy&poo=zz</a></p>
  <form method="post" action="put">
  <p>foo=<input name="foo" type="text" /></p>
  <p>bar=<input name="bar" type="text" /></p>
  <p>poo=<input name="poo" type="text" /></p>
  <p><input type="submit" value="Submit" /></p>
  </form>
</body>
</html>