	$(function(){
		/* 평점 ★로 표시 */
		let grade = /*[[${classList.classGrade}]]*/ null;
	
		function printStar() {
			for(let i=0; i< grade; i++) {
				$('#star').append('<i class="fa-solid fa-star" style="color: #f4575f;"></i>');
			}
		}
		printStar();
		
		/* 결제 수단 선택 */
		$('.payButton').on('click', function() {
			var paymentMethod = $(this).text();
		    var choice = (paymentMethod === '신용카드') ? '0' : '1';
		    $('#paymentMethodInput').val(choice);

		    $(this).toggleClass('selected');
		    $('.payButton').not(this).removeClass('selected');
		 });
		
		/* 결제 금액 표시 */
		var couponNo = $('#payCoupon').val();					  // 쿠폰번호
	 	var point = $('#payPoint').val();						  // 포인트 금액
	  	var classMoney = parseInt($('.classM').text()) * 1000;	  // 수업 가격
	  	var number = parseInt($('#person').text());				  // 인원 수
	
		var couponDC = '';		// 쿠폰 할인 숫자
		var percent = '';		// 쿠폰 할인률
		
	
		if(couponNo == '' && point == '') {
			var total = classMoney * number;
	    	var formatTotal = total.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
	    	$('#amount').val(classMoney);
	    	$('#totalCP').val(classMoney);
	    	$('#tctc2').text(formatTotal.toLocaleString() + ' 원');
	    	
	    }
	
		if (couponNo == '') {
	    	$('#cpcp').after('<td>0 원</td>');
	  	} else {
		 	 $.ajax({
		 		 type: 'get',
				 url: '/payment/couponCalculate',
	     		 data: 'couponNo=' + couponNo,
	     		 dataType: 'json',
	     	  	 success: function(resData) {
	     	  		 couponDC = resData.couponDiscount;
	     			 percent = parseInt(couponDC) * 0.01;
	     			 var totalCP = classMoney * percent;	// 쿠폰 할인 금액
	     		  	 $('#totalCP').val(totalCP);
	     		  	 var formatCP = totalCP.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
	     		  	 $('#cpcp').after('<td>' + formatCP + ' 원</td>');
	     		  	 calTotal(); // calTotal 함수를 호출하는 부분을 여기로 이동
	     		  }
		 	 }); // ajax
	  	}
		
		if (point == '') {
			$('#ptpt').after('<td>0 원</td>');
		} else {
	    	var formatPT = point.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
	    	$('#ptpt').after('<td>' + formatPT + ' 원</td>');
	    	calTotal(); // calTotal 함수를 호출하는 부분을 여기로 이동
		}
	    
	});  // 익명함수


	/* 총 합계 구하기 */
	function calTotal() {
		var classMoney = parseInt($('.classM').text()) * 1000;	  // 수업 가격
		var number = parseInt($('#person').text());				  // 인원 수
		var total = '';		// 총 금액
		var totalCP = $('#totalCP').val();
		var payPoint = $('#payPoint').val();
	    	
		if (totalCP == '') {
			totalCP = 0;
	    } else if (payPoint == '') {
			payPoint = 0;
	    }
	    	
    	total = classMoney * number - parseInt(totalCP) - parseInt(payPoint);
    	$('#amount').val(total);
    	var formatTotal = total.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
		$('#tctc2').text(formatTotal.toLocaleString() + ' 원');
	
	}
	
	
	/* 결제하기 클릭 */
	function fnNext(){
		
		var paymentMethodInput = $('#paymentMethodInput').val();
		var ok = $('#ok').is(':checked')
			
		if(paymentMethodInput == '' || $('#ok').is(':checked') != true) {
			alert('⚠ 결제수단 및 동의안내를 체크해 주세요. ⚠');
		} else {

			var userNo = $('#userNo').val();						// 회원번호
			var classTitle = $('#classTitle').text();				// 수업명
			
			var couponNo = $('#payCoupon').val();					// 사용한 쿠폰번호
			if(couponNo == '') { couponNo = 0; }

			var totalCP = $('#totalCP').val(); 						// 쿠폰 할인금액
			if(totalCP == '') { totalCP = 0; }
			
			var point = $('#payPoint').val();						// 사용한 포인트 금액
			if(point == '') { point = 0; }
			
			var amount = $('#amount').val(); 						// 총 금액
			var payMethod = $('#paymentMethodInput').val();			// 결제 수단 (신용카드 0, 카카오페이 1)
			var randomNumber = Math.floor(Math.random() * 1000);	// 아임포트용 주문번호
			
			// 결제수단 : 신용카드
			if(payMethod === '0') {
			
				$.ajax({
					type: 'post',
					url: '/payment/pay.do',
					data: 'userNo=' + userNo,
					dataType: 'json',
					success: function(resData) {
						requestPay(couponNo, totalCP, point, payMethod, resData.enrollDTO.enrollNo, classTitle, amount, resData.userDTO.userId, resData.userDTO.userName, resData.userDTO.userTel);
					}
				}); // ajax
			} else {
				
				// 결제수단 : 카카오페이
				$.ajax({
					type: 'post',
					url: '/payment/pay.do',
					data: 'userNo=' + userNo,
					dataType: 'json',
					success: function(resData) {
						requestPay2(couponNo, totalCP, point, payMethod, resData.enrollDTO.enrollNo, classTitle, amount, resData.userDTO.userId, resData.userDTO.userName, resData.userDTO.userTel);
					}
				}); // ajax
			}  // if
		} 
	} // fnNext()
	
	
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
		var popupHeight = 600;
		var left = (screen.width - popupWidth) / 2;
		var top = (screen.height - popupHeight) / 2;
		var popupOptions = 'width=' + popupWidth + ', height=' + 450 + ', left=' + left + ', top=' + top;
		var payCoupon = $('#payCoupon').val();
		var schNo = $('#schNo').val();
		window.open('/user/pointUse?payCoupon=' + payCoupon + '&schNo=' + schNo, '포인트', popupOptions);
	}