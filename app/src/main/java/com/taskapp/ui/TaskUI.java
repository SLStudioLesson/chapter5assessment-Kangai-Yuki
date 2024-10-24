package com.taskapp.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.taskapp.logic.TaskLogic;
import com.taskapp.logic.UserLogic;
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
        } catch (Exception e) { // 例外処理を一般化
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
                    System.out.println("コードは半角の数字で入力してください");
                    continue;
                }
                taskCode = Integer.parseInt(codeInput);
    
                System.out.print("タスク名を入力してください：");
                taskName = reader.readLine();
                // 修正：タスク名の長さをチェック
                if (taskName.isEmpty() || taskName.length() > 10) {
                    System.out.println("タスク名は1文字以上10文字以内で入力してください");
                    continue;
                }
    
                System.out.print("担当するユーザーのコードを選択してください：");
                String userCodeInput = reader.readLine();
                if (userCodeInput.isEmpty() || !userCodeInput.matches("\\d+")) {
                    System.out.println("ユーザーのコードは半角の数字で入力してください");
                    continue;
                }
                repUserCode = Integer.parseInt(userCodeInput);
    
                if (!validUserCodes.contains(repUserCode)) {
                    System.out.println("存在するユーザーコードを入力してください");
                }
    
                taskLogic.save(taskCode, taskName, repUserCode, loginUser);
                System.out.printf("%sの登録が完了しました。%n", taskName);
                break;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * タスクのステータス変更または削除を選択するサブメニューを表示します。
     *
     * @see #inputChangeInformation()
     * @see #inputDeleteInformation()
     */
    // public void selectSubMenu() {
    // }

    /**
     * ユーザーからのタスクステータス変更情報を受け取り、タスクのステータスを変更します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#changeStatus(int, int, User)
     */
    public boolean isNumeric(String inputText) {
        if (inputText == null || inputText.isEmpty()) {
            return false;
        }
        return inputText.chars().allMatch(Character::isDigit);
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
    // public boolean isNumeric(String inputText) {
    //     return false;
    // }
}