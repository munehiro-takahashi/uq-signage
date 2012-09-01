<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="v" uri="http://uq.nskint.co.jp/pd/taglibs/uqSignage-viewer"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<canvas id="${param.id}" width="${param.width}" height="${param.height}"></canvas>
<script type="text/javascript">
(function(){
	var canvas = document.getElementById("${param.id}");
	var ctx = canvas.getContext("2d");
	var text = {content:"${param.value}",
			direction:"${param.orientation}",
			isVertical: function() { return this.direction.toLowerCase() === "vertical" }};
	var font = {family:"'${param.fontFamily}'", color:"${param.fontColor}", size:${param.fontSize}, style:"${param.fontStyle}"};
	var x = 0;
	var y = 0;
	var speed = ${param.speed};
	var fontstyle = font.style + " " + font.size + "px '" + font.family + "'";
	var fillcolor = 'rgb(255, 255, 255)';
	var counter = 0;
	ctx.font = fontstyle;
	ctx.textBaseline = "top";
	var textWidth = text.isVertical() ? ctx.measureText(text.content.charAt(0)).width : ctx.measureText(text.content).width;
	var textHeight = text.isVertical() ? font.size * text.content.length : font.size;
	var timer = setInterval(draw, speed);
	var defaultStep = 10;

		<c:choose>
		 <c:when test="${param.direction eq 'UP'}">
			var x = canvas.offsetWidth / 2 - textWidth / 2;
			var y = canvas.offsetHeight - textHeight;
			var flg = true;
			var move = function() {
				<c:choose>
				<c:when test="${not param.bounce}">
					y = y - defaultStep;
					if (y < 0 - textHeight) {
						y = canvas.offsetHeight - textHeight;
						counter ++;
					}
					</c:when>
					<c:otherwise>
					if (flg == true) {
						y = y - defaultStep;
						if (y < 0) {
							flg = false;
							y = 0;
							counter ++;
						}
					} else {
						y = y + defaultStep;
						if (y > canvas.offsetHeight - textHeight) {
							y = canvas.offsetHeight - textHeight;
							flg = true;
							counter ++;
						}
					}
					</c:otherwise>
					</c:choose>
			};
		 </c:when>
		 <c:when test="${param.direction eq 'DOWN'}">
			var x = canvas.offsetWidth / 2 - textWidth / 2;
			var y = 0;
			var flg = true;
			var move = function() {
				<c:choose>
					<c:when test="${not param.bounce}">
					y = y + defaultStep;
					if (y > canvas.offsetHeight) {
						y = 0;
						counter ++;
					}
					</c:when>
					<c:otherwise>
					if (flg == true) {
						y = y + defaultStep;
						if (y > (canvas.offsetHeight - textHeight)) {
							flg = false;
							y = canvas.offsetHeight  - textHeight;
							counter ++;
						}
					} else {
						y = y - defaultStep;
						if (y < 0) {
							y = 0;
							flg = true;
							counter ++;
						}
					}
					</c:otherwise>
				</c:choose>
			};

		 </c:when>
		 <c:when test="${param.direction eq 'LEFT'}">
				var x = canvas.offsetWidth - textWidth;
				var y = canvas.offsetHeight / 2 - textHeight / 2;
				var flg = true;
				var move = function() {
					<c:choose>
					<c:when test="${not param.bounce}">
						x = x - defaultStep;
						if (x < 0 - textWidth) {
							x = canvas.offsetWidth - textWidth;
							counter ++;
						}
						</c:when>
						<c:otherwise>
						if (flg == true) {
							x = x - defaultStep;
							if (x < 0) {
								x = 0;
								flg = false;
								counter ++;
							}
						} else {
							x = x + defaultStep;
							if (x > canvas.offsetWidth - textWidth) {
								x = canvas.offsetWidth - textWidth;
								flg = true;
								counter ++;
							}
						}
						</c:otherwise>
						</c:choose>
				};
		 </c:when>
		 <c:otherwise>
				var x = 0;
				var y = (canvas.offsetHeight / 2) - (textHeight / 2);
				var flg = true;
				var move = function() {
					<c:choose>
					<c:when test="${not param.bounce}">
						x = x + defaultStep;
						if (x > canvas.offsetWidth) {
							x = 0;
							counter ++;
						}
						</c:when>
						<c:otherwise>
						if (flg == true) {
							x = x + defaultStep;
							if (x > (canvas.offsetWidth - textWidth)) {
								flg = false;
								x = canvas.offsetWidth  - textWidth;
								counter ++;
							}
						} else {
							x = x - defaultStep;
							if (x < 0) {
								x = 0;
								flg = true;
								counter ++;
							}
						}
						</c:otherwise>
						</c:choose>
				};
		 </c:otherwise>
		</c:choose>
	}

	var draw = function() {
		ctx.fillStyle = fillcolor;
		ctx.clearRect(0, 0, canvas.offsetWidth, canvas.offsetHeight);
		ctx.fillRect(0, 0, canvas.offsetWidth, canvas.offsetHeight);
		ctx.fillStyle = font.color;
		update();
		if (text.isVertical()) {
			for (var index = 0; index < text.content.length; index ++) {
				ctx.fillText(text.content.charAt(index), x, y + font.size * index);
			}
		} else {
			ctx.fillText(text.content, x, y);
		}
	};

	var update = function() {
		<c:if test="${param.repeat > 0}">
		if (counter == ${param.repeat}) {
			 clearInterval(timer);
		}
		</c:if>
		move();
	};
})();
</script>