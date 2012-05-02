<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="v" uri="http://uq.nskint.co.jp/pd/taglibs/uqSignage-viewer"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<canvas id="${param.id}" width="${param.width}" height="${param.height}"></canvas>
<script>
	var objImages = new Array();
	var intImageIndex = 0;
	var objTimer = null;
	var objCanvasSlide = null;
	var objContext = null;
	var fAlpha = 0.0;
	var lWait = 0;
	var intRadius = 0;

	function showSlide_${id}(res) {
		for (var i = 0, j = res.feed.entry.length; i < j; i ++) {
			var url = res.feed.entry[i].media$group.media$content[0].url;
			objImages[i] = new Image();
			objImages[i].src = url;
		}
		objCanvasSlide = document.getElementById("${id}");
		objContext = objCanvasSlide.getContext("2d");
	<c:choose>
		<c:when test="${param.effect eq 'FADE'}">
	<%-- フェード --%>
	var CONST_FADEIN_INTERVAL = 100;
	var CONST_FADEIN_WAIT = 3000;

		// 使用する変数の初期化
		fAlpha = 0.0;
		lWait = 0;
		// コンテキストの初期化
		objContext.globalAlpha = 1;

		// スライドの開始
		objTimer = setInterval( function() {
					var intNextImageIndex = (intImageIndex + 1 < objImages.length ? intImageIndex + 1 : 0);

					// フェードイン中
					if( fAlpha <= 1.0 ){
						objContext.clearRect(0, 0, objCanvasSlide.width, objCanvasSlide.height);
						objContext.globalAlpha = 1;
						objContext.drawImage(objImages[intImageIndex], 0, 0);
						objContext.globalAlpha = fAlpha;
						objContext.drawImage(objImages[intNextImageIndex], 0, 0);
						fAlpha += 0.1;
					}

					// フェードインした
					if(1.0 < fAlpha){
						// 待機完了
						if(lWait == CONST_FADEIN_WAIT){
							// 次の画像へ
							intImageIndex = intNextImageIndex;
							// 使用する変数の初期化
							fAlpha = 0.0;
							lWait = 0;
						}
						// 待機中
						else {
							lWait += CONST_FADEIN_INTERVAL;
						}
					}
				}, CONST_FADEIN_INTERVAL);
		</c:when>
		<c:when test="${param.effect eq 'CIRCLE'}">
	<%-- サークル --%>
	var CONST_CIRCLE_INTERVAL = 50;
	var CONST_CIRCLE_WAIT = 1500;

		// 使用する変数の初期化
		intRadius = 10;
		lWait = 0;
		// コンテキストの初期化
		objContext.globalAlpha = 1;

		// スライドの開始
		objTimer = setInterval( function() {
					objContext.save();
					var intNextImageIndex = (intImageIndex + 1 < objImages.length ? intImageIndex + 1 : 0);

					// くりぬき中
					if( intRadius <= 550 ){
						objContext.clearRect(0, 0, objCanvasSlide.width, objCanvasSlide.height);
						objContext.drawImage(objImages[intImageIndex], 0, 0);
						objContext.beginPath();
						objContext.arc( 275, 200, intRadius, 0, Math.PI * 2, false);
						objContext.clip();
						objContext.drawImage(objImages[intNextImageIndex], 0, 0);
						intRadius += 10;
					}

					// くりぬきした
					if( 550 < intRadius ){
						// 待機完了
						if(lWait == CONST_CIRCLE_WAIT){
							// 次の画像へ
							intImageIndex = intNextImageIndex;
							// 使用する変数の初期化
							intRadius = 10;
							lWait = 0;
						}
						// 待機中
						else {
							lWait += CONST_CIRCLE_INTERVAL;
						}
					}
					objContext.restore();
				}, CONST_CIRCLE_INTERVAL);
		</c:when>
		<c:otherwise>
		<%-- パラパラ --%>
			// コンテキストの初期化
			objContext.globalAlpha = 1;

			// スライドの開始
			objTimer = setInterval( function() {
							objContext.clearRect(0, 0, objCanvasSlide.width, objCanvasSlide.height);
							objContext.drawImage(objImages[intImageIndex++], 0, 0);
							intImageIndex = (intImageIndex < objImages.length ? intImageIndex : 0);
					}, 4000);
		</c:otherwise>
	</c:choose>
	}
</script>
<script src="http://picasaweb.google.com/data/feed/api/user/${param.user}/album/${param.album}?kind=photo&alt=json-in-script&callback=showSlide_${id}&start-index=1&max-results=20"></script>
