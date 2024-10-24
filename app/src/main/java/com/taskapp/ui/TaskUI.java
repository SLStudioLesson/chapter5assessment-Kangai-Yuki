package com.taskapp.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.taskapp.exception.AppException;
import com.taskapp.logic.TaskLogic;
import com.taskapp.logic.UserLogic;
import com.taskapp.model.Task;
import com.taskapp.model.User;

public class TaskUI {
    private final BufferedReader reader;

    private final UserLogic userLogic;

    private final TaskLogic taskLogic;

    private User loginUser;

    public TaskUI() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        userLogic = new UserLogic();
        taskLogic = new TaskLogic();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param reader
     * @param userLogic
     * @param taskLogic
     */
    public TaskUI(BufferedReader reader, UserLogic userLogic, TaskLogic taskLogic) {
        this.reader = reader;
        this.userLogic = userLogic;
        this.taskLogic = taskLogic;
    }

    /**
     * メニューを表示し、ユーザーの入力に基づいてアクションを実行します。
     *
     * @see #inputLogin()
     * @see com.taskapp.logic.TaskLogic#showAll(User)
     * @see #selectSubMenu()
     * @see #inputNewInformation()
     */
    public void displayMenu() {
        System.out.println("タスク管理アプリケーションにようこそ!!");
        inputLogin();
        // メインメニュー
        boolean flg = true;
        while (flg) {
            try {
                System.out.println("以下1~3のメニューから好きな選択肢を選んでください。");
                System.out.println("1. タスク一覧, 2. タスク新規登録, 3. ログアウト");
                System.out.print("選択肢：");
                String selectMenu = reader.readLine();

                System.out.println();

                switch (selectMenu) {
                    case "1":
                    taskLogic.showAll(loginUser);
                    selectSubMenu();
                        break;
                    case "2":
                    inputNewInformation();
                        break;
                    case "3":
                        System.out.println("ログアウトしました。");
                        flg = false;
                        break;
                    default:
                        System.out.println("選択肢が誤っています。1~3の中から選択してください。");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    /**
     * ユーザーからのログイン情報を受け取り、ログイン処理を行います。
     *
     * @see com.taskapp.logic.UserLogic#login(String, String)
     */
    public void inputLogin() {
        while (true) {
            try {
                System.out.print("メールアドレスを入力してください：");
                String email = reader.readLine();
                System.out.print("パスワードを入力してください：");
                String password = reader.readLine();

                loginUser = userLogic.login(email, password);
                System.out.println("ユーザー名：" + loginUser.getName() + "でログインしました。");
                break; // ログイン成功でループを抜ける
            } catch (IOException e) {
            System.out.println("入力エラーが発生しました。もう一度試してください。");
        } catch (Exception e) {
            System.out.println("ログインに失敗しました。メールアドレスまたはパスワードを確認してください。");
        }
        }
    }



    /**
     * ユーザーからの新規タスク情報を受け取り、新規タスクを登録します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#save(int, String, int, User)
     */
    public void inputNewInformation() {
        int taskCode = 0;
        String taskName = "";
        int repUserCode = 0;
    
        List<Integer> validUserCodes = List.of(1, 2);
    
        while (true) {
            try {
                System.out.print("タスクコードを入力してください：");
                String codeInput = reader.readLine();
                if (codeInput.isEmpty() || !codeInput.matches("\\d+")) {
                    throw new AppException("コードは半角の数字で入力してください");
                }
                taskCode = Integer.parseInt(codeInput);
    
                System.out.print("タスク名を入力してください：");
                taskName = reader.readLine();
                if (taskName.isEmpty() || taskName.length() > 10) {
                    throw new AppException("タスク名は1文字以上10文字以内で入力してください");
                }
    
                System.out.print("担当するユーザーのコードを選択してください：");
                String userCodeInput = reader.readLine();
                if (userCodeInput.isEmpty() || !userCodeInput.matches("\\d+")) {
                    throw new AppException("ユーザーのコードは半角の数字で入力してください");
                }
                repUserCode = Integer.parseInt(userCodeInput);
    
                if (!validUserCodes.contains(repUserCode)) {
                    throw new AppException("存在するユーザーコードを入力してください");
                }
    
                taskLogic.save(taskCode, taskName, repUserCode, loginUser);
                System.out.printf("%sの登録が完了しました。%n", taskName);
                break; // 登録が完了したらループを抜ける
            } catch (AppException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("入力エラーが発生しました。もう一度試してください。");
            }
        }
    }
    /**
     * タスクのステータス変更または削除を選択するサブメニューを表示します。
     *
     * @see #inputChangeInformation()
     * @see #inputDeleteInformation()
     */
    public void selectSubMenu() {
        boolean flg = true; // サブメニュー
        while (flg) {
            try {
                System.out.println("以下1~2のメニューから好きな選択肢を選んでください。");
                System.out.println("1. タスクのステータス変更, 2. メインメニューに戻る");
                System.out.print("選択肢：");
                String selectMenu = reader.readLine();
                System.out.println();
    
                switch (selectMenu) {
                    case "1":
                        inputChangeInformation();
                        break;
                    case "2":
                        flg = false; // メインメニューに戻る
                        break;
                    default:
                        System.out.println("選択肢が誤っています。1~2の中から選択してください。");
                        break;
                }
            } catch (IOException e) {
                System.out.println("入力エラーが発生しました。もう一度試してください。");
            }
        }
    }

    /**
     * ユーザーからのタスクステータス変更情報を受け取り、タスクのステータスを変更します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#changeStatus(int, int, User)
     */
    public void inputChangeInformation() {
        while (true) {
            try {
                System.out.print("ステータスを変更するタスクコードを入力してください：");
                String codeInput = reader.readLine();
                if (!isNumeric(codeInput)) {
                    throw new AppException("コードは半角の数字で入力してください");
                }
                int taskCode = Integer.parseInt(codeInput);
    
                //ステータス変更の選択肢を表示
                System.out.println("どのステータスに変更するか選択してください。");
                System.out.println("1. 着手中, 2. 完了");
                String statusInput = reader.readLine();
                if (!isNumeric(statusInput)) {
                    throw new AppException("ステータスは半角の数字で入力してください");
                }
                int newStatus = Integer.parseInt(statusInput);
                if (newStatus != 1 && newStatus != 2) {
                    throw new AppException("ステータスは1・2の中から選択してください");
                }
    
                // ステータス変更
                taskLogic.changeStatus(taskCode, newStatus, loginUser);
                System.out.println("ステータスの変更が完了しました。");
                break;
            } catch (AppException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("入力エラーが発生しました。もう一度試してください。");
            }
        }
    }

    /**
     * ユーザーからのタスク削除情報を受け取り、タスクを削除します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#delete(int)
     */
    // public void inputDeleteInformation() {
    // }

    /**
     * 指定された文字列が数値であるかどうかを判定します。
     * 負の数は判定対象外とする。
     *
     * @param inputText 判定する文字列
     * @return 数値であればtrue、そうでなければfalse
     */
    public boolean isNumeric(String inputText) {
        if (inputText == null || inputText.isEmpty()) {
            return false;
        }

        for (char c : inputText.toCharArray()) {
            if (c < '0' || c > '9') {
                return false;
            }
        }

        return true;
    }
}