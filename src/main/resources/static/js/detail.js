$(document).ready(function () {
    getPostId()
})

function getPostId() {
    let param = document.location.href.split("?id=")
    let id = param[1]
    getPostDetail(id)
}

//CheerController와 ajax통신하여 받은 특정 게시물(ex 1번 게시물이면 1번 게시물의 title, username 등 정보)의 정보를 받아와서
//getDetailHtml을 통해 html을 만들어 웹에 표시해줌!!
function getPostDetail(id) {
    $.ajax({
        type: 'GET',
        url: `/api/cheers/${id}`,
        success: function (response) {
            let tempHtml = getDetailHtml(response)
            $('#cards-box').append(tempHtml)
        },
        error: function () {
            alert("데이터를 조회하는데 실" +
                "패 했습니다.")
        }
    })
}

function getDetailHtml(cheer) {
    return `<div class="card">
              <!-- date/username 영역 -->
              <div class="metadata">
              <div class="date">
              ${cheer.modifiedAt.split("T")[0]+" | "+cheer.modifiedAt.split("T")[1].split(".")[0]}
              </div>
              </div>
                      <!-- contents 조회/수정 영역-->
              <div class="contents">
              <table class="type02">
                  <tr>
                    <th scope="row">작성자</th>  <!--타임리프 써보자...-->
                    <td id="${cheer.id}-username">${cheer.username}</td>
                  </tr>
                  <tr>
                    <th scope="row">제목</th>
                    <td id="${cheer.id}-title">${cheer.title}</td>
                  </tr>
                  <tr>
                    <th scope="row">내용</th>
                    <td id="${cheer.id}-contents">${cheer.contents}</td>
                  </tr>
                  <div id="${cheer.id}-editarea" class="edit">
                  <p>수정할 내용을 입력해주세요!</p>
              <textarea id="${cheer.id}-textarea" class="te-edit" name="" id="" cols="30" rows="5"></textarea>
              </div>
                </table>
                      <!-- 버튼 영역-->
        <div class="footer">
              <img id="${cheer.id}-back" class="icon-back" src="image/send.png" alt="" onclick="history.back()">
              <img id="${cheer.id}-back2" class="icon-back2" src="image/send2.png" alt="" onclick="hideEdits('${cheer.id}')">
              <img id="${cheer.id}-edit" class="icon-start-edit" src="image/edit.png" alt="" onclick="editPost('${cheer.id}')">
              <img id="${cheer.id}-delete" class="icon-delete" src="image/delete.png" alt="" onclick="deleteOne('${cheer.id}')">
              <img id="${cheer.id}-submit" class="icon-end-edit" src="image/done.png" alt="" onclick="submitEdit('${cheer.id}')">
              </div>
              </div>`;
}

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

// 메모를 수정합니다.
function submitEdit(id) {
    // 1. 작성 대상 메모의 username과 contents 를 확인합니다.
    let username = $(`#${id}-username`).text().trim();
    let title = $(`#${id}-title`).text().trim();
    let contents = $(`#${id}-textarea`).val().trim();
    // 2. 작성한 메모가 올바른지 isValidContents 함수를 통해 확인합니다.
    if (isValidContents(contents) == false) {
        return;
    }
    // 3. 전달할 data JSON으로 만듭니다.
    let data = {'username': username, 'title': title, 'contents': contents};
    // 4. PUT /api/cheers/{id} 에 data를 전달합니다.
    $.ajax({
        type: "PUT",
        url: `/api/cheers/${id}`,
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function (response) {
            alert('변경이 완료되었습니다.');
            window.location.reload();
        }
    });
}

// 메모를 삭제합니다.
function deleteOne(id) {
    // 1. DELETE /api/cheers/{id} 에 요청해서 메모를 삭제합니다.
    $.ajax({
        type: "DELETE",
        url: `/api/cheers/${id}`,
        success: function () {
            alert('삭제가 완료되었습니다.');
            window.location.replace("index.html");
        }
    })
}

// 수정 버튼을 눌렀을 때, 기존 작성 내용을 textarea 에 전달합니다.
// 숨길 버튼을 숨기고, 나타낼 버튼을 나타냅니다.
function editPost(id) {
    showEdits(id);
    let contents = $(`#${id}-contents`).text().trim();
    $(`#${id}-textarea`).val(contents);
}

function showEdits(id) {
    $(`#${id}-editarea`).show();
    $(`#${id}-submit`).show();
    $(`#${id}-delete`).show();
    $(`#${id}-back2`).show();

    $(`#${id}-back`).hide();
    $(`#${id}-contents`).hide();
    $(`#${id}-edit`).hide();
}

function hideEdits(id) {
    $(`#${id}-editarea`).hide();
    $(`#${id}-submit`).hide();
    $(`#${id}-back2`).hide();

    $(`#${id}-back`).show();
    $(`#${id}-contents`).show();
    $(`#${id}-edit`).show();
}

///////////////////댓글관련///////////////////////
