/**
 * 
 */
 
$(function() {
	// 체크박스 이미지 변경
	$('input').on('change', function() {
    if ($(this).is(':checked')) {
      $(this).siblings('img').attr('src', '/images/user/check_on.png');
    } else {
      $(this).siblings('img').attr('src', '/images/user/check_off.png');
    }
  });
	
	// 전체선택 시 모든 체크박스 선택하기
	$('#checkAll').on('change', function() {
			$('.checkOne').prop('checked', $(this).is(':checked'));
			if ($('.checkOne').is(':checked')) {
      $('.checkOne').siblings('img').attr('src', '/images/user/check_on.png');
    } else {
      $('.checkOne').siblings('img').attr('src', '/images/user/check_off.png');
    }
	})
	
 $('.checkOne').on('click', function() {
		var checked = $('.checkOne:checked').length;
		var length = $('.checkOne').length;
		  
  	$('#checkAll').prop('checked', (checked == length));
  	 if (checked == length) {
      $('#checkAll').siblings('img').attr('src', '/images/user/check_on.png');
    } else {
      $('#checkAll').siblings('img').attr('src', '/images/user/check_off.png');
    }
 });

});

 
 
 
 
 
 
 
 
 
 
 
 
 
 
 