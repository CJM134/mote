package com.example.test01;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class Test01ApplicationTests {

    static List<String> fileNamesList = new ArrayList<>();
    @Test
    void contextLoads() {
        List<String> pdfUrlsList = new ArrayList<>();

        String pdfUrl = null; // 替换为您要下载的PDF的URL
        String filePath = "D:\\test\\terst.xlsx";
        String outputFolderPath = "D:/test/downloads/"; // 替换为您要保存PDF的文件夹路径（注意末尾斜杠）

        try {
            //创建工作簿对象
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(filePath));
            //获取工作簿下sheet的个数
            //int sheetNum = xssfWorkbook.getNumberOfSheets();
            //System.out.println("该excel文件中总共有："+sheetNum+"个sheet");
            //遍历工作簿中的所有数据
//            for(int i = 0;i<sheetNum;i++) {
//                //读取第i个工作表
//                System.out.println("读取第"+(i+1)+"个sheet");
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            //获取最后一行的num，即总行数。此处从0开始
            int maxRow = sheet.getLastRowNum();
            for (int row = 1; row <= maxRow; row++) {
                //获取最后单元格num，即总单元格数 ***注意：此处从1开始计数***
                int maxRol = sheet.getRow(row).getLastCellNum();
                //System.out.println("--------第" + row + "行的数据如下--------");
                for (int rol = 0; rol < maxRol; rol++){
                    //System.out.print(sheet.getRow(row).getCell(rol) + "  ");
                    XSSFCell cell = sheet.getRow(row).getCell(rol);
//                        System.out.print(cell);
                    String s1 = cell.toString();
//                        System.out.println(substring);
//                        System.out.println(s1);
                    if(rol == 0){
                        //文件名
                        fileNamesList.add(s1);
                    }
                    else {
                        //链接
                        pdfUrlsList.add(s1);
                    }
                }
                //System.out.println();
            }
            //   }

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < pdfUrlsList.size(); i++) {
            pdfUrl = pdfUrlsList.get(i);
            try {
                downloadPDF(pdfUrl, outputFolderPath,i);
                System.out.println("PDF文件下载完成。");
            } catch (IOException e) {
                System.out.println("下载PDF文件时发生错误：" + e.getMessage());
            }
        }
    }
    private static void downloadPDF(String pdfUrl, String outputFolderPath,int index) throws IOException {
        URL url = new URL(pdfUrl);

        // 从URL打开连接并读取PDF内容
        try (InputStream inputStream = url.openStream();
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            // 使用输出文件夹路径和文件名创建目标文件的Path对象
            String fileName = getFileNameFromUrl(pdfUrl,index);
            Path outputPath = Path.of(outputFolderPath, fileName);

            // 使用Java NIO的文件复制功能将下载的PDF写入目标文件
            Files.copy(bufferedInputStream, outputPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static String getFileNameFromUrl(String fileUrl,int index) {
        // 从文件URL中提取文件名
        int lastSlashIndex = fileUrl.lastIndexOf('/');
        //String substring1 = fileUrl.substring(lastSlashIndex + 1);
        int lastPotIndex = fileUrl.lastIndexOf('.');
        String substring = fileUrl.substring(lastPotIndex + 1);
        //String substring2 = fileUrl.substring(lastSlashIndex, lastPotIndex);
        if ( lastSlashIndex < fileUrl.length() - 1) {
            //String s = substring2 + index;
            return fileNamesList.get(index) +'.'+substring;
        }
        return "file.pdf"; // 如果无法从URL中提取文件名，则使用默认文件名
    }
}
