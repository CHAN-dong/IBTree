package util;


import java.io.*;
import java.util.List;

public class WriteVO {




    public static long writeVOToLocal(String vo) {
        File file = new File("D:\\mycode\\mywork\\LiRui22\\src\\vo.txt");
        // 在文件夹目录下新建文件
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            fos = new FileOutputStream(file, true);
            osw = new OutputStreamWriter(fos, "utf-8");
            // 写入内容
            osw.write(vo);
            // 换行
            osw.write("\r\n");
        } catch (Exception e) {
        } finally {
            // 关闭流
            try {
                if (osw != null) {
                    osw.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
            }
        }
        return 0;
    }

//        try {
//            File writeName = new File("D:\\mycode\\mywork\\LiRui22\\src\\vo.txt");
//            writeName.createNewFile();
//            try (FileWriter writer = new FileWriter(writeName);
//                 BufferedWriter out = new BufferedWriter(writer)
//            ) {
//                out.write(vo);
//            }
//            return writeName.length();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
}
