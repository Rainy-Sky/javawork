package paperio;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class Data {
    private static File record = new File("./data", "record.data");
    private static FileOutputStream fileOutputStream;
    private static FileInputStream fileInputStream;
    private static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;
    private static HashMap<String, Double> scores;

    static {
        if (!record.exists()) {
            try {
                record.createNewFile();
                fileOutputStream = new FileOutputStream(record);
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(new HashMap<String,Double>());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 获得文件中存储的历史分数
     * @return 玩家的历史分数
     */

    public static HashMap<String, Double> getScores() {
        try {
            fileInputStream = new FileInputStream(record);
            objectInputStream = new ObjectInputStream(fileInputStream);
            scores = (HashMap<String, Double>) objectInputStream.readObject();
            return scores;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 添加一份历史记录，如果不高于玩家名对应的最高分则不更新
     * @param name 玩家名
     * @param score 获得的分数
     */
    public static void addScores(String name, double score) {
        try {
            fileInputStream = new FileInputStream(record);
            objectInputStream = new ObjectInputStream(fileInputStream);
            scores = (HashMap<String, Double>) objectInputStream.readObject();
            if (!scores.containsKey(name) || score > scores.get(name)) {
                scores.put(name, score);
            }
            fileOutputStream = new FileOutputStream(record);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(scores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 清除玩家的历史记录
     */
    public static void clearScores() {
        try {
            fileInputStream = new FileInputStream(record);
            objectInputStream = new ObjectInputStream(fileInputStream);
            scores = (HashMap<String, Double>) objectInputStream.readObject();
            scores.clear();
            fileOutputStream = new FileOutputStream(record);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(scores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
