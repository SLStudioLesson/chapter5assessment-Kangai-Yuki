package com.taskapp.dataaccess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.taskapp.model.Task;
import com.taskapp.model.User;

public class TaskDataAccess {

    private final String filePath;

    private final UserDataAccess userDataAccess;

    public TaskDataAccess() {
        filePath = "app/src/main/resources/tasks.csv";
        userDataAccess = new UserDataAccess();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param filePath
     * @param userDataAccess
     */
    public TaskDataAccess(String filePath, UserDataAccess userDataAccess) {
        this.filePath = filePath;
        this.userDataAccess = userDataAccess;
    }

    /**
     * CSVから全てのタスクデータを取得します。
     *
     * @see com.taskapp.dataaccess.UserDataAccess#findByCode(int)
     * @return タスクのリスト
     */
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // ヘッダー行をスキップ
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int code = Integer.parseInt(values[0]);
                String name = values[1];
                int status = Integer.parseInt(values[2]);
                int repUserCode = Integer.parseInt(values[3]);

                User repUser = userDataAccess.findByCode(repUserCode);
                tasks.add(new Task(code, name, status, repUser));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    /**
     * タスクをCSVに保存します。
     * @param task 保存するタスク
     */
    public void save(Task task) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.csv", true))) {
            writer.write(task.getCode() + "," + task.getName() + "," + task.getStatus() + "," + task.getRepUser().getCode());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * コードを基にタスクデータを1件取得します。
     * @param code 取得するタスクのコード
     * @return 取得したタスク
     */
    public Task findByCode(int code) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // ヘッダー行をスキップ
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int taskCode = Integer.parseInt(values[0]);
                if (taskCode == code) {
                    String name = values[1];
                    int status = Integer.parseInt(values[2]);
                    int repUserCode = Integer.parseInt(values[3]);
                    User repUser = userDataAccess.findByCode(repUserCode);
                    return new Task(taskCode, name, status, repUser);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // 見つからなかった場合
    }

    /**
     * タスクデータを更新します。
     * @param updateTask 更新するタスク
     */
    public void update(Task updateTask) {
        List<Task> tasks = findAll();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("コード,名前,ステータス,担当ユーザーコード"); // ヘッダーを書き込む
            writer.newLine();
            for (Task task : tasks) {
                if (task.getCode() == updateTask.getCode()) {
                    writer.write(updateTask.getCode() + "," + updateTask.getName() + "," + updateTask.getStatus() + "," + updateTask.getRepUser().getCode());
                } else {
                    writer.write(task.getCode() + "," + task.getName() + "," + task.getStatus() + "," + task.getRepUser().getCode());
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * コードを基にタスクデータを削除します。
     * @param code 削除するタスクのコード
     */
    // public void delete(int code) {
    //     try () {

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    /**
     * タスクデータをCSVに書き込むためのフォーマットを作成します。
     * @param task フォーマットを作成するタスク
     * @return CSVに書き込むためのフォーマット文字列
     */
    // private String createLine(Task task) {
    // }
}