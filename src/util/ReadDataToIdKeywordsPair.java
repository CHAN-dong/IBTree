package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadDataToIdKeywordsPair {
    public static List<List<Integer>> readData(String path){
        List<List<Integer>> idKeywordsPair = new ArrayList<>();
        File file = new File(path);
        try {
            if(file.isFile()&&file.exists()){
                InputStreamReader fla = new InputStreamReader(new FileInputStream(file));
                BufferedReader scr = new BufferedReader(fla);
                String str = null;
                while((str = scr.readLine()) != null){
                    String[] data = str.split(" ");
                    List<Integer> keywordsList = new ArrayList<Integer>();
                    //第一个为文档id，第二个第三个为坐标，从第四个开始读取关键字。关键字从1开始,文档从0开始
//                    for(int i = 3;i < data.length;i++){
//                        keywordsList.add(Integer.valueOf(data[i]));
//                    }
                    //第一个为文档id，第二个开始为关键字
                    for(int i = 1;i < data.length;i++){
                        keywordsList.add(Integer.valueOf(data[i]));
                    }
                    idKeywordsPair.add(keywordsList);
                }
                scr.close();
                fla.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return idKeywordsPair;
    }
}
