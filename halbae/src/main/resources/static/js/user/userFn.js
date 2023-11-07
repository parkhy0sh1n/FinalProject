/**
 * 
 */
 
$(function() {
	
	$('#confirmed').val('off');
	
		$('#newProfile').on('change', function(){
			
			console.log(this);
			var formData = new FormData();
			formData.append("profile", $(this)[0].files[0]);
			formData.append("userId", $('#userId').val());
	
			$.ajax({
				contentType: false,
				processData: false,
				type: 'post',
				url: '/user/updateProfile.do',
				data: formData,
				dataType: 'json',
				success: function(res) {
					var display = res.display;
					$('#userImgPath').val(display);
					$('.userImg').attr('src', display);
				}
			})
		})	
	
})
 
	
		
function fnLogout() {
	location.href="/user/logout.do?loginId=" + $('.loginId').val();	
}

function fnFindId() {
	location.href="/user/findId.html";
}

function fnFindPw() {
	location.href="/user/findPw.html";
}
 
function fnSendCode() {
	// 입력한 이메일
	let email = $('#email').val();
	
	// ajax는 비동기식이라 각각 실행해주기 위해 Promise 사용
	new Promise(function(resolve, reject) {
		// 정규식
		let regEmail = /^[a-zA-Z0-9-_]+@[a-zA-Z0-9]{2,}(\.[a-zA-Z]{2,}){1,2}$/;
		
		// 정규식을 테스트 결과에 따른 함수 사용
		if(regEmail.test(email)) {
			resolve();
		}else{
			reject();
		}
	}).then(function() {
		$.ajax({
			type: 'get',
			url: '/user/sendCode.do',
			data: 'email=' + email,
			dataType: 'json',
			success: function(res) {
				alert('메일이 전송되었습니다.');
				$('.validate').on('click', function() {
					if(res.authCode == $('#authCode').val()) {
						alert('인증되었습니다.');
						$('#findFrm').submit();
					}else {
						alert('인증번호를 확인해주세요.');
					}
				})				
			}
		})
	}).catch(function() {
		alert('이메일을 다시 확인해주세요.');
	})

}// 함수 종료 괄호

function fnTempPw() {
	// 입력한 이메일
	let email = $('#email').val();

	// 정규식
	let regEmail = /^[a-zA-Z0-9-_]+@[a-zA-Z0-9]{2,}(\.[a-zA-Z]{2,}){1,2}$/;
	
	// 정규식을 테스트 결과에 따른 함수 사용
	if(regEmail.test(email)) {
		$.ajax({
			type: 'get',
			url: '/user/sendTempPw.do',
			data: 'email=' + email,
			dataType: 'json',
			success: function(res) {
				alert('메일이 전송되었습니다.');
				$('#tempPw').val(res.tempPw);
				$('#tempPwFrm').submit();
			},
			error: function() {
				alert('전송 실패');
			}
		})
	}else{
		alert('이메일을 다시 확인해주세요.');
	}
}// 함수 종료 괄호
 
 
 // 비번 변경 시 현재 비밀번호 인증하기
function fnConfirmPw() {
	$.ajax({
		type: 'post',
		url: '/user/confirmPw.do',
		data: 'userId=' + $('#userId').val() + '&userPw=' + $('#usingPw').val(),
		datatype: 'json',
		success: function(res) {
			if(res.confirmResult) {
				alert('인증되었습니다.');
				$('#confirmed').val('on');
			}else {
				alert('비밀번호를 확인해주세요.');
				$('#confirmed').val('off');
			}
		}
	})
}

function fnModifyPw() {
	
	if($('#confirmed').val() == 'on') {
		
		// 비밀번호
		let pw = $('#userPw').val();
	  let pwLength = $('#userPw').val().length;
		let validCnt = /[a-z]/.test(pw)
							+ /[A-Z]/.test(pw)
							+ /[0-9]/.test(pw)
							+ /[^a-zA-Z0-9]/.test(pw);
		
		verifyPw = (pwLength >=4) && (pwLength <=20) && (validCnt >=3);
		
		if(verifyPw == false) {
			alert('비밀번호는 숫자 대소문자 특수문자 포함 4~20자로 구성되어야 합니다.');
			return;
		}
		
		// 비밀번호 재입력
		if($('#userPw').val() != $('#chkUserPw').val()) {
			alert('재입력한 비밀번호를 확인해주세요.');
			return;
		}

		// 현재 비번과 같은 경우 변경 불가
		if($('#usingPw').val() == $('#userPw').val()) {
			alert('새 비밀번호는 현재 비밀번호와 다르게 설정해야 합니다.');
			return;
		}
		
		$('#modifyPwFrm').submit();

	}else {
		alert('현재 비밀번호를 인증해주세요.');
	}
}

	// 회원 탈퇴
	function fnAskDelete() {
		if(confirm('계정을 삭제하시면 포인트와 쿠폰도 사라집니다. 삭제하시겠습니까?')) {
			$('#deleteUserFrm').submit();
		}
	}

	// 쿠폰함 팝업 열기
	function openCoupon(userNo) {
	    var popupWidth = 500;
	    var popupHeight = 1000;
	    var left = Math.floor((screen.width - popupWidth) / 2);
	    var top = Math.floor((screen.height - popupHeight) / 2);
	    var popupOptions = 'width=' + popupWidth + ', height=' + popupHeight + ', left=' + left + ', top=' + top;
	    window.open('/coupon?userNo=' + userNo, '쿠폰', popupOptions);
	}
 
 	// 포인트 팝업 열기
	function openPoint() {
		var popupWidth = 500;
	    var popupHeight = 1000;
	    var left = (screen.width - popupWidth) / 2;
	    var top = (screen.height - popupHeight) / 2;
	    var popupOptions = 'width=' + popupWidth + ', height=' + popupHeight + ', left=' + left + ', top=' + top;
	    window.open('/user/point', '포인트', popupOptions);
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