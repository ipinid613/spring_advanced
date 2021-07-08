// 미리 작성된 영역 - 수정하지 않으셔도 됩니다.
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
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 여기서부터 코드를 작성해주시면 됩니다.

$(document).ready(function () {
    // HTML 문서를 로드할 때마다 실행합니다.
    getMessages();
})

// 메모를 불러와서 보여줍니다.
function getMessages() {
    // 1. 기존 메모 내용을 지웁니다.
    // $('#cards-box').empty();
    // 2. 메모 목록을 불러와서 HTML로 붙입니다.
    $.ajax({
        type: 'GET',
        url: '/api/cheers',
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                let cheer = response[i];
                let id = cheer.id;
                let title = cheer.title;
                let username = cheer.username;
                let modifiedAt_front = cheer.modifiedAt.split("T")[0];
                let modifiedAt_back = cheer.modifiedAt.split("T")[1].split(".")[0];
                let modifiedAt = modifiedAt_front +" | "+ modifiedAt_back
                addHTML(id, username, title, modifiedAt);
            }
        }
    })
}


function moveToDetail(id) {
    window.location.href = "detail.html?id=" + id;
}

// 메모 하나를 HTML로 만들어서 body 태그 내 원하는 곳에 붙입니다.
function addHTML(id, username, title, modifiedAt) {
    // 1. HTML 태그를 만듭니다.
    let tempHtml = `<div class="card">
                                <!-- date/username 영역 -->
                      <div class="metadata">
                      <div class="date">
                      ${modifiedAt}
                      </div>
                      <div id="${id}-username" class="username">
                      ${username}
                      </div>
                      </div>
                              <!-- contents 조회/수정 영역-->
                      <div class="contents" onclick=moveToDetail(${id})>
                      <div id="${id}-title" class="index-text">${title}</div>
                      </div>
                      </div>
                      </div>
                      </div>`;
    // 2. #cards-box 에 HTML을 붙인다.
    $('#cards-box').append(tempHtml);
}