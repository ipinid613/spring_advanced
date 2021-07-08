// 사용자가 내용을 올바르게 입력하였는지 확인합니다.
function isValidContents(contents) {
    if (contents == '') {
        alert('내용을 입력해주세요');
        return false;
    }
    if (contents.trim().length > 140) {
        alert('공백 포함 140자 이하로 입력해주세요');
        return false;
    }
    return true;
}

// 메모를 생성합니다.
function writePost() {
    // 1. 작성한 메모를 불러옵니다.
    // 2. 작성한 메모가 올바른지 isValidContents 함수를 통해 확인합니다.
    // 3. genRandomName 함수를 통해 익명의 username을 만듭니다.
    // 4. 전달할 data JSON으로 만듭니다.
    // 5. POST /api/cheers 에 data를 전달합니다.
    let contents = $('#exampleFormControlTextarea1').val();
    if (isValidContents(contents) == false) {
        return;
    }
    let username = $('.cur_username').text();
    let title = $('#input2').val();
    let data = {'username':username, 'title':title, 'contents': contents};
    $.ajax({
        type: "POST",
        url: "/api/cheers",
        contentType: "application/json", // JSON 형식으로 전달함을 알리기
        data: JSON.stringify(data),
        success: function (response) {
            alert('메시지가 성공적으로 작성되었습니다.');
            window.location.replace("/")
        }
    });
}