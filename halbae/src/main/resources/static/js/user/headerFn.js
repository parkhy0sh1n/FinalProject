/**
 * 
 */
function fnToggleLnb() {
	$('.myLnb').toggleClass("hidden");
}

 	// 메시지창 팝업 	열기
	function openMessage() {
	    // 팝업 창을 열기 위한 설정
	    var popupWidth = 500;
	    var popupHeight = 1000;
	    var left = (screen.width - popupWidth) / 2;
	    var top = (screen.height - popupHeight) / 2;
	    var popupOptions = 'width=' + popupWidth + ', height=' + popupHeight + ', left=' + left + ', top=' + top;
	    // 팝업 창을 열고 해당 HTML 페이지를 로드
	    window.open('/chat/list', '메시지', popupOptions);
}


   // 쿠폰 사용 팝업 열기
   function openCouponUse(userNo) {
       var popupWidth = 500;
       var popupHeight = 1000;
       var left = Math.floor((screen.width - popupWidth) / 2);
       var top = Math.floor((screen.height - popupHeight) / 2);
       var popupOptions = 'width=' + popupWidth + ', height=' + popupHeight + ', left=' + left + ', top=' + top;
       var payPoint = $('#payPoint').val();
       var schNo = $('#schNo').val();
       window.open('/couponUse?userNo=' + userNo + '&payPoint=' + payPoint + '&schNo=' + schNo, '쿠폰', popupOptions);
   }
   
   // 포인트 사용 팝업 열기
   function openPointUse(){
      var popupWidth = 500;
       var popupHeight = 1000;
       var left = (screen.width - popupWidth) / 2;
       var top = (screen.height - popupHeight) / 2;
       var popupOptions = 'width=' + popupWidth + ', height=' + popupHeight + ', left=' + left + ', top=' + top;
       var payCoupon = $('#payCoupon').val();
       var schNo = $('#schNo').val();
       window.open('/user/pointUse?payCoupon=' + payCoupon + '&schNo=' + schNo, '포인트', popupOptions);
   }
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 