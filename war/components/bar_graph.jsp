<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="v" uri="http://uq.nskint.co.jp/pd/taglibs/uqSignage-viewer"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<canvas id="${param.id}" width="${param.width}" height="${param.height}"></canvas>
<script language="javascript" type="text/javascript">
  var canvas = document.getElementById('${param.id}');
  var context = canvas.getContext('2d');
  var data = JSON.parse('${param.data}');
  var data_caption = "${param.data_caption}";
  var scale_caption = "${param.scale_caption}";
  var max = 0;
  for (var i = 0; i < data.length; i ++) {
    if (data[i].value > max) {
      max = data[i].value;
    }
  }
  function round(value, step) {
    if (value == 0) return 0;
    return Math.ceil(value / step) * step;
  }

  var scale_min = ${param.scale_min} <= 0 ? 0 : ${param.scale_min};
  var scale_step = ${param.scale_step} <= 0 ? round(max - scale_min, 10) / 5 : ${param.scale_step};
  var scale_max = ${param.scale_max} <= 0 ? round(max - scale_min, scale_step) : ${param.scale_max};
  var orientation = "${param.orientation}".toLowerCase();
  var h_margin = Math.max(40, canvas.height / 10);
  var w_margin = Math.max(40, canvas.width / 10);
  var graph_height = canvas.height - h_margin;
  var graph_width = canvas.width - w_margin;

  if (orientation == "vertical") {
    var bar = graph_width / data.length;
    var bar_width = ${param.bar_width} <= 0 ? bar * (3/4) : ${param.bar_width};
    var bar_margin = ${param.bar_margin} <= 0 ? bar * (1/4) : ${param.bar_margin};
    var mag = graph_height / (scale_max);
    context.moveTo(w_margin, graph_height);
    context.lineTo(canvas.width, graph_height);
    context.strokeStyle = "#000000";
    context.stroke();

    context.moveTo(w_margin, graph_height);
    context.lineTo(w_margin, 0);
    context.strokeStyle = "#000000";
    context.stroke();

    context.font = "12pt MS Gothic";
    context.textBaseline = "top";
    context.fillText(data_caption, canvas.width / 2 - context.measureText(data_caption).width / 2, canvas.height - 20);
    context.rotate(90 * Math.PI / 180);
    context.fillText(scale_caption, canvas.height / 2 - context.measureText(scale_caption).width / 2, -20);
    context.rotate(- 90 * Math.PI / 180);

    var x = bar_margin + w_margin;
    context.font = "10pt MS Gothic";
    for (var i = 0; i < data.length; i ++) {
      var elem = data[i];
      var val =  Math.floor((elem.value - scale_min) * mag);
      context.fillStyle = elem.color;
      context.fillRect(x, graph_height - val, bar_width, val);
      context.fillStyle = "#000000";
      var text_width = context.measureText(elem.title).width;
      context.fillText(elem.title, x + bar_width / 2 - text_width / 2, graph_height + 3);
      x += bar_width + bar_margin;
    }

    var num = Math.floor(scale_max / scale_step);
    var y = graph_height;
    context.font = "10pt MS Gothic";
    context.textBaseline = "middle";
    for (var i = 0; y > 0; i ++) {
      context.strokeStyle = "#000000";
      context.moveTo(w_margin, Math.floor(y));
      context.lineTo(canvas.width, Math.floor(y));
      context.stroke();
      context.fillStyle = "#000000";
      context.fillText(scale_min + i * scale_step, w_margin - 20 - 3, Math.floor(y), 20);
      y -= scale_step * mag;
    }
  } else {
    var bar = graph_height / data.length;
    var bar_width = ${param.bar_width} <= 0 ? bar * (3/4) : ${param.bar_width};
    var bar_margin = ${param.bar_margin} <= 0 ? bar * (1/4) : ${param.bar_margin};
    var mag = graph_width / (scale_max);
    context.moveTo(w_margin, h_margin);
    context.lineTo(canvas.width, h_margin);
    context.strokeStyle = "#000000";
    context.stroke();

    context.moveTo(w_margin, canvas.height);
    context.lineTo(w_margin, h_margin);
    context.strokeStyle = "#000000";
    context.stroke();

    context.font = "12pt MS Gothic";
    context.textBaseline = "top";
    context.fillStyle = "#000000";
    context.fillText(scale_caption, canvas.width / 2 - context.measureText(scale_caption).width / 2, 0);
    context.rotate(90 * Math.PI / 180);
    context.fillText(data_caption, canvas.height / 2 - context.measureText(data_caption).width / 2, -20);
    context.rotate(- 90 * Math.PI / 180);

    var y = bar_margin + h_margin;
    context.font = "10pt MS Gothic";
    context.textBaseline = "middle";
    for (var i = 0; i < data.length; i ++) {
      var elem = data[i];
      var val =  Math.floor((elem.value - scale_min) * mag);
      context.fillStyle = elem.color;
      context.fillRect(w_margin, y, val, bar_width);
      context.fillStyle = "#000000";
      context.fillText(elem.title, w_margin - 20 - 3, y + bar_width / 2, 20);
      y += bar_width + bar_margin;
    }

    var num = Math.floor(scale_max / scale_step);
    var x = w_margin;
    context.font = "10pt MS Gothic";
    context.textBaseline = "bottom";
    for (var i = 0; x <= graph_width; i ++) {
      context.strokeStyle = "#000000";
      context.moveTo(Math.floor(x), h_margin);
      context.lineTo(Math.floor(x), canvas.height);
      context.stroke();
      context.fillStyle = "#000000";
      var text_width = context.measureText(scale_min + i * scale_step).width;
      context.fillText(scale_min + i * scale_step, Math.floor(x) - text_width / 2, h_margin - 3);
      x += scale_step * mag;
    }
  }
</script>