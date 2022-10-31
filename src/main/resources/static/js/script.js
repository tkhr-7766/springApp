function likeOrUnlike(self, event, isDetail = false) {
    // 親要素のタグにクリックイベントが伝播しないように止める
    event.stopPropagation();

    // CSRFトークンを取得
    let token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    // 現在「いいね」済みかどうか取得
    let isLiked = toBoolean(self.dataset.isLiked);

    if (isLiked) {
        // いいね済みの場合はいいね解除
        unlike(self, token, isDetail);
    } else {
        // 未いいねならいいね登録
        like(self, token, isDetail);
    }
}

/**
 * いいねの登録
 */
function like(self, token, isDetail) {
    // 写真IDの取得
    const photoId = self.dataset.photoId;
    // 送信URL生成
    const url = `/api/photo/${photoId}/like`;
    // PUTメソッドでURLに送信
    fetch(url, {
        method: 'PUT',
        headers: {
            'X-CSRF-TOKEN': token
        },
    }).then((response) => { // 通信成功時
        // ステータスコードが200以外ならエラー
        if (response.status !== 200) {
            throw new Error();
        }

        // いいねした後の画面上の処理
        self.dataset.isLiked = true;
        let className = isDetail ? 'button--liked' : 'photo__action--liked';
        self.classList.toggle(className);
        let span = self.querySelector('.likes-count');
        let likesCount = parseInt(span.textContent);
        span.textContent = likesCount + 1;
    }).catch((error) => {
        console.error(error);
    });
}

/**
 * いいねの解除
 */
function unlike(self, token, isDetail) {
    const photoId = self.dataset.photoId;
    const url = `/api/photo/${photoId}/like`;
    fetch(url, {
        method: 'DELETE',
        headers: {
            'X-CSRF-TOKEN': token
        },
    }).then((response) => {
        if (response.status !== 200) {
            throw new Error();
        }

        self.dataset.isLiked = false;
        let className = isDetail ? 'button--liked' : 'photo__action--liked';
        self.classList.toggle(className);
        let span = self.querySelector('.likes-count');
        let likesCount = parseInt(span.textContent);
        span.textContent = likesCount - 1;
    }).catch((error) => {
        console.error(error);
    });
}

/**
 * 文字列true/false をBooleanに変換
 */
function toBoolean(data) {
    return data.toLowerCase() === 'true';
}
