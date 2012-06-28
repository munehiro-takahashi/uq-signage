<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="v" uri="http://uq.nskint.co.jp/pd/taglibs/uqSignage-viewer"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<canvas id="${param.id}" width="${param.width}" height="${param.height}"></canvas>
<script type="text/javascript">
	var canvas = document.getElementById("${param.id}");
	var ctx = canvas.getContext("2d");
	var text = {content:"${param.value}",
			direction:"${param.orientation}",
			isVertical: function() { return this.direction.toLowerCase() === "vertical" }};
	var font = {family:"'${param.fontFamily}'", color:"${param.fontColor}", size:${param.fontSize}, style:"${param.fontStyle}"};
	var marqueeflow = {repeat:${param.repeat}, direction:"${param.direction}".toLowerCase(), bounce:${param.bounce}};
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
	var marquee = createMarquee();
	var timer = setInterval(draw, speed);
	var defaultStep = 10;

	function createMarquee() {
		if (marqueeflow.direction == "up") {
			return new UpMarquee();
		} else if (marqueeflow.direction == "down") {
			return new DownMarquee();
		} else if (marqueeflow.direction == "left") {
			return new LeftMarquee();
		} else if (marqueeflow.direction == "right") {
			return new RightMarquee();
		}
		return {move:function(){window.console.log("invalid direction:" + marqueeflow.direction);}};
	}

	function draw() {
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
	}

	function update() {
		if (marqueeflow.repeat > 0 && counter == marqueeflow.repeat) {
			 clearInterval(timer);
		}
		marquee.move();
	}

	function LeftMarquee() {
		x = canvas.offsetWidth - textWidth;
		y = canvas.offsetHeight / 2 - textHeight / 2;
		var flg = true;
		this.move = function() {
			if (marqueeflow.bounce === false) {
				x = x - defaultStep;
				if (x < 0 - textWidth) {
					x = canvas.offsetWidth - textWidth;
					counter ++;
				}
			} else {
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
			}
		}
	}

	function RightMarquee() {
		x = 0;
		y = (canvas.offsetHeight / 2) - (textHeight / 2);
		var flg = true;
		this.move = function() {
			if (marqueeflow.bounce === false) {
				x = x + defaultStep;
				if (x > canvas.offsetWidth) {
					x = 0;
					counter ++;
				}
			} else {
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
			}
		}
	}

	function UpMarquee() {
		x = canvas.offsetWidth / 2 - textWidth / 2;
		y = canvas.offsetHeight - textHeight;
		var flg = true;
		this.move = function() {
			if (marqueeflow.bounce === false) {
				y = y - defaultStep;
				if (y < 0 - textHeight) {
					y = canvas.offsetHeight - textHeight;
					counter ++;
				}
			} else {
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
			}
		}
	}

	function DownMarquee() {
		x = canvas.offsetWidth / 2 - textWidth / 2;
		y = 0;
		var flg = true;
		this.move = function() {
			if (marqueeflow.bounce === false) {
				y = y + defaultStep;
				if (y > canvas.offsetHeight) {
					y = 0;
					counter ++;
				}
			} else {
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
			}
		}
	}
</script>