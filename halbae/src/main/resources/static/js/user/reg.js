/**
 * 
 */
 
$(function() {
	$('#reg').text('');
	
	if($('#unqMsg').val() != '') {
		alert($('#unqMsg').val());
	}
	
});

// 로그인 시 이메일 비밀번호 확인하는 정규식
function checkLogin() {
	
	let regId = /^[a-zA-Z0-9-_]+@[a-zA-Z0-9]{2,}(\.[a-zA-Z]{2,6}){1,2}$/;
	let verifyEmail = regId.test($('#userId').val());

	let pw = $('#userPw').val();
  let pwLength = $('#userPw').val().length;
	let validCnt = /[a-z]/.test(pw)
						+ /[A-Z]/.test(pw)
						+ /[0-9]/.test(pw)
						+ /[^a-zA-Z0-9]/.test(pw);
	
	verifyPw = (pwLength >=4) && (pwLength <=20) && (validCnt >=3);
	
	if(verifyEmail == false && verifyPw == false) {
		$('#reg').text('이메일과 비밀번호를 확인해주세요.');
	}else if(verifyEmail == false) {
		$('#reg').text('이메일을 확인해주세요.');
	}else if(verifyPw == false) {
		$('#reg').text('비밀번호를 확인해주세요.');		
	}else {
	$('#loginFrm').submit();
	}
}; 

// 회원 가입 시 입력 정보 정규식
function checkJoin() {
	$('#reg').empty();
	// 이름
	if($('#userName').val().length == 0) {
		$('#reg').text('이름(별명)을 확인해주세요.');
		return;
	}
	// 연락처
	let regTel = /^010[0-9]{8}$/;
	let verifyTel = regTel.test($('#userTel').val());
	if(verifyTel == false) {
		$('#reg').text('연락처를 확인해주세요.');
		return;
	}
	
	// 아이디(이메일)
	let regId = /^[a-zA-Z0-9-_]+@[a-zA-Z0-9]{2,}(\.[a-zA-Z]{2,6}){1,2}$/;
	let verifyEmail = regId.test($('#userId').val());
	
	if(verifyEmail == false) {
		$('#reg').text('이메일을 확인해주세요.');
		return;
	}

	// 비밀번호
	let pw = $('#userPw').val();
  let pwLength = $('#userPw').val().length;
	let validCnt = /[a-z]/.test(pw)
						+ /[A-Z]/.test(pw)
						+ /[0-9]/.test(pw)
						+ /[^a-zA-Z0-9]/.test(pw);
	
	verifyPw = (pwLength >=4) && (pwLength <=20) && (validCnt >=3);
	
	if(verifyPw == false) {
		$('#reg').text('비밀번호를 확인해주세요.');
		return;
	}
	
	// 비밀번호 재입력
	if($('#userPw').val() != $('#chkUserPw').val()) {
		$('#reg').text('재입력한 비밀번호를 확인해주세요.');
		return;
	}
		
	$('#joinFrm').submit();
}

// 비밀번호 재입력 정규식
function checkRePw() {
	
}
 
 // 눈 모양 아이콘 클릭 시 input타입 password -> text로 변경
 function togglePw() {
	if($('#userPw').attr("type") === "text") {
		$('#userPw').attr("type", "password");
		$('.pwIcon1').removeClass('fa-eye');
		$('.pwIcon1').addClass('fa-eye-slash');
	}else {
		$('#userPw').attr("type", "text");
		$('.pwIcon1').removeClass('fa-eye-slash');
		$('.pwIcon1').addClass('fa-eye');
	}
}
// 가입 시 비밀 번호 확인 칸도 보이게 하기
 function toggleChkPw() {
	if($('#chkUserPw').attr("type") === "text") {
		$('#chkUserPw').attr("type", "password");
		$('.pwIcon2').removeClass('fa-eye');
		$('.pwIcon2').addClass('fa-eye-slash');
	}else {
		$('#chkUserPw').attr("type", "text");
		$('.pwIcon2').removeClass('fa-eye-slash');
		$('.pwIcon2').addClass('fa-eye');
	}
}
 

 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 