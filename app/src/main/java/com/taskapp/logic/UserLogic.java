package com.taskapp.logic;

import com.taskapp.dataaccess.UserDataAccess;
import com.taskapp.exception.AppException;
import com.taskapp.model.User;

public class UserLogic {
    private final UserDataAccess userDataAccess;

    public UserLogic() {
        userDataAccess = new UserDataAccess();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param userDataAccess
     */
    public UserLogic(UserDataAccess userDataAccess) {
        this.userDataAccess = userDataAccess;
    }

    /**
     * ユーザーのログイン処理を行います。
     *
     * @see com.taskapp.dataaccess.UserDataAccess#findByEmailAndPassword(String, String)
     * @param email ユーザーのメールアドレス
     * @param password ユーザーのパスワード
     * @return ログインしたユーザーの情報
     * @throws AppException メールアドレスとパスワードが一致するユーザーが存在しない場合にスローされます
     */
    public User login(String email, String password) {
    User user = userDataAccess.findByEmailAndPassword(email, password);
    if (user == null) {
        System.out.println("既に登録されているメールアドレス、パスワードを入力してください");
        return null; // ユーザーが見つからない場合は null を返す
    }
    return user; // ユーザー情報を返す
    }
}