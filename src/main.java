import dataOwner.DO;
import dataOwner.Trapdoor;
import server.SP;
import util.queryAndVerifyRes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class main {
    public static HashMap readForwardData(String path) throws Exception{
        HashMap<Integer, List<Integer>> forwardIndexMap = new HashMap<>();
        File file = new File(path);
        if(file.isFile()&&file.exists()){
            InputStreamReader fla = new InputStreamReader(new FileInputStream(file));
            BufferedReader scr = new BufferedReader(fla);
            String str = null;
            while((str = scr.readLine()) != null){
                String[] data = str.split(" ");
                List<Integer> keywords = new ArrayList<Integer>();
                for(int i = 1;i < data.length;i++){//第三个才是文档id
                    keywords.add(Integer.valueOf(data[i]));
                }
                keywords.sort(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1 - o2;
                    }
                });
                forwardIndexMap.put(Integer.valueOf(data[0]), keywords);
            }
            scr.close();
            fla.close();
        }
        return forwardIndexMap;
    }
    static HashSet<Integer> queryKeywords = new HashSet<>();
    public static int[] getQuery (HashMap<Integer, List<Integer>> allForwardIndex, boolean hasRes, int keywordSize) {
        int idSize = allForwardIndex.size();
        int[] res = new int[keywordSize];
        int id = (int)(Math.random() * idSize);
        List<Integer> keywordList = allForwardIndex.get(id);
        if (hasRes) {
            while(keywordList.size() < keywordSize) {
                id = (int)(Math.random() * idSize);
                keywordList = allForwardIndex.get(id);
            }
            for (int i = 0; i < keywordSize; i++) {
                res[i] = keywordList.get(i);
                queryKeywords.add(res[i]);
            }
        } else {
            for (int i = 0; i < keywordSize; i++) {
                int rand;
                do {
                    rand = (int)(Math.random() * 2000) + 1;
                } while (queryKeywords.contains(rand));
                queryKeywords.add(rand);
                res[i] = rand;
            }
        }
        return res;
    }
    public static void printTime(int round, int keywordSize, HashMap<Integer, List<Integer>> allForwardIndex, DO dataOwner, SP serviceProvider) {
        queryAndVerifyRes[] query = new queryAndVerifyRes[round];
        for (int i = 0; i < query.length; i += 2) {
            query[i] = queryAndVerifyTime(getQuery(allForwardIndex, true, keywordSize), dataOwner, serviceProvider);
//            while (!query[i].isPass) {
//                query[i] = queryAndVerifyTime(getQuery(allForwardIndex, true, keywordSize), dataOwner, serviceProvider);
//            }
            query[i + 1] = queryAndVerifyTime(getQuery(allForwardIndex, false, keywordSize), dataOwner, serviceProvider);
//            while (!query[i + 1].isPass) {
//                query[i + 1] = queryAndVerifyTime(getQuery(allForwardIndex, false, keywordSize), dataOwner, serviceProvider);
//            }
        }
        long sumQuery = 0;
//        long sumVerify = 0;
//        long sumVOSize = 0;
        double l = query.length;
        for (int i = 0; i < l; i++) {
            sumQuery += query[i].queryTime;
//            sumVerify += query[i].verifyTime;
//            sumVOSize += query[i].VOSize;
        }
        System.out.println("查询关键字" + keywordSize + ": " + "查询时间：" + (sumQuery / l) / 1000000 + "ms");
    }

    public static void main(String[] args) throws Exception {
//        test("D:\\mycode\\mywork\\LiRui22\\src\\test2.txt");
        test("D:\\mycode\\mywork\\Dataset_processing\\test_dataset\\forwardIndex40000id2000keyword.txt");
//        test("D:\\mycode\\mywork\\Dataset_processing\\test_dataset\\forwardIndex40000id2000keyword_Uniform_new_k2000.txt");
//        test("D:\\mycode\\mywork\\Dataset_processing\\test_dataset\\forwardIndex20000id2000keyword.txt");
//        test("D:\\mycode\\mywork\\Dataset_processing\\test_dataset\\forwardIndex60000id2000keyword_Uniform_new_k2000.txt");
//        test("D:\\mycode\\mywork\\Dataset_processing\\test_dataset\\forwardIndex80000id2000keyword_Uniform_new_k2000.txt");
    }

    public static void test(String filePath) throws Exception {
        DO dataOwner = new DO(filePath, 2000);
//        dataOwner = new DO(filePath, 2000);
        SP serviceProvider = new SP(dataOwner.getBFTreeRoot(), dataOwner.getHead());
        HashMap<Integer, List<Integer>> allForwardIndex = readForwardData(filePath);


        System.out.println("test-------------------------------------");
        int[] keywords2 = {1,2};
        int[] keywords3 = {1,2,3};
        int[] keywords4 = {1,2,3,4};
        int[] keywords5 = {1,2,3,4,5};
        int[] keywords6 = {1,2,3,4,5,6};
        int[] keywords7 = {1,2,3,4,5,6,7};
        int[] keywords8 = {1,2,3,4,5,6,7,8};
        int[] keywords9 = {1,2,3,4,5,6,7,8,9};
        int[] keywords10 = {1,2,3,4,5,6,7,8,9,10};

        for (int i = 0; i < 10; ++i) {
            dataOwner.getTrapdoor(keywords10);
        }

        int allTime = 0;
        for (int i = 0; i < 10; ++i) {
            long s1 = System.nanoTime();
            dataOwner.getTrapdoor(keywords2);
            long s2 = System.nanoTime();
            allTime += s2 - s1;
        }
        System.out.println("2个关键字令牌生成时间：" + allTime / 10.0 + "ns");

        allTime = 0;
        for (int i = 0; i < 10; ++i) {
            long s1 = System.nanoTime();
            dataOwner.getTrapdoor(keywords3);
            long s2 = System.nanoTime();
            allTime += s2 - s1;
        }
        System.out.println("3个关键字令牌生成时间：" + allTime / 10.0 + "ns");

        allTime = 0;
        for (int i = 0; i < 10; ++i) {
            long s1 = System.nanoTime();
            dataOwner.getTrapdoor(keywords4);
            long s2 = System.nanoTime();
            allTime += s2 - s1;
        }
        System.out.println("4个关键字令牌生成时间：" + allTime / 10.0 + "ns");

        allTime = 0;
        for (int i = 0; i < 10; ++i) {
            long s1 = System.nanoTime();
            dataOwner.getTrapdoor(keywords5);
            long s2 = System.nanoTime();
            allTime += s2 - s1;
        }
        System.out.println("5个关键字令牌生成时间：" + allTime / 10.0 + "ns");

        allTime = 0;
        for (int i = 0; i < 10; ++i) {
            long s1 = System.nanoTime();
            dataOwner.getTrapdoor(keywords6);
            long s2 = System.nanoTime();
            allTime += s2 - s1;
        }
        System.out.println("6个关键字令牌生成时间：" + allTime / 10.0 + "ns");

        allTime = 0;
        for (int i = 0; i < 10; ++i) {
            long s1 = System.nanoTime();
            dataOwner.getTrapdoor(keywords7);
            long s2 = System.nanoTime();
            allTime += s2 - s1;
        }
        System.out.println("7个关键字令牌生成时间：" + allTime / 10.0 + "ns");

        allTime = 0;
        for (int i = 0; i < 10; ++i) {
            long s1 = System.nanoTime();
            dataOwner.getTrapdoor(keywords8);
            long s2 = System.nanoTime();
            allTime += s2 - s1;
        }
        System.out.println("8个关键字令牌生成时间：" + allTime / 10.0 + "ns");

        allTime = 0;
        for (int i = 0; i < 10; ++i) {
            long s1 = System.nanoTime();
            dataOwner.getTrapdoor(keywords9);
            long s2 = System.nanoTime();
            allTime += s2 - s1;
        }
        System.out.println("9个关键字令牌生成时间：" + allTime / 10.0 + "ns");

        allTime = 0;
        for (int i = 0; i < 10; ++i) {
            long s1 = System.nanoTime();
            dataOwner.getTrapdoor(keywords10);
            long s2 = System.nanoTime();
            allTime += s2 - s1;
        }
        System.out.println("10个关键字令牌生成时间：" + allTime / 10.0 + "ns");



//        System.out.println("test-------------------------------------");
//        printTime(10, 2, allForwardIndex, dataOwner, serviceProvider);
//        System.out.println("real-------------------------------------");
//        printTime(10, 2, allForwardIndex, dataOwner, serviceProvider);
//        printTime(10, 3, allForwardIndex, dataOwner, serviceProvider);
//        printTime(10, 4, allForwardIndex, dataOwner, serviceProvider);
//        printTime(10, 5, allForwardIndex, dataOwner, serviceProvider);
//        printTime(10, 6, allForwardIndex, dataOwner, serviceProvider);
//        printTime(10, 7, allForwardIndex, dataOwner, serviceProvider);
//        printTime(10, 8, allForwardIndex, dataOwner, serviceProvider);
//        printTime(10, 9, allForwardIndex, dataOwner, serviceProvider);
//        printTime(10, 10, allForwardIndex, dataOwner, serviceProvider);
    }
    public static queryAndVerifyRes queryAndVerifyTime(int[] keywords, DO dataOwner, SP serviceProvider) {
        Trapdoor trapdoor = dataOwner.getTrapdoor(keywords);
        long start = System.nanoTime();
        List<Integer> searchRes = serviceProvider.search(trapdoor);
        long end = System.nanoTime();

        queryAndVerifyRes queryAndVerifyRes = new queryAndVerifyRes((end - start));
        System.out.println(queryAndVerifyRes.toString());
        System.out.println();
        return queryAndVerifyRes;
    }
}
