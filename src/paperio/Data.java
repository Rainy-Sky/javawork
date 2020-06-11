package paperio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeSet;


public class Data {
    private static File record = new File("./", "record.data");
    private static FileOutputStream fileOutputStream;
    private static FileInputStream fileInputStream;
    private static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;
    private static TreeSet<Double> scores;
    
    static {
        if (!record.exists()) {
            try {
                record.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void init() {

    }
    public static TreeSet<Double> getScores() {
        try {
            fileInputStream = new FileInputStream(record);
            objectInputStream = new ObjectInputStream(fileInputStream);
            scores =(TreeSet<Double>)objectInputStream.readObject();
            return scores;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addScores(double score) {
        try {
            fileInputStream = new FileInputStream(record);
            objectInputStream = new ObjectInputStream(fileInputStream);
            scores =(TreeSet<Double>)objectInputStream.readObject();
            scores.add(score);
            fileOutputStream = new FileOutputStream(record);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(scores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void clearScores() {
        try {
            fileInputStream = new FileInputStream(record);
            objectInputStream = new ObjectInputStream(fileInputStream);
            scores =(TreeSet<Double>)objectInputStream.readObject();
            scores.clear();
            fileOutputStream = new FileOutputStream(record);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(scores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}