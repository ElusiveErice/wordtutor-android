package com.csu.wordtutor.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.csu.wordtutor.model.Word;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public final class FileUtils {

    private static final String TAG = "FileUtils";

    public static String uriToFile(Uri uri, Context context) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        , new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA}
                        , "(" + MediaStore.Images.ImageColumns.DATA + "=" + "'" + path + "'" + ")"
                        , null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();
        }

        return path;
    }

    public static void readFile(String filename) throws IOException {
        //打开文件输入流
        FileInputStream input = new FileInputStream(filename);
        byte[] temp = new byte[1024];
        StringBuilder sb = new StringBuilder("");
        int len = 0;
        //读取文件内容:
        while ((len = input.read(temp)) > 0) {
            sb.append(new String(temp, 0, len));
        }
        //关闭输入流
        input.close();
    }

    public static List<Word> getLexiconFromExcel(String filename) {
        List<Word> wordList = new ArrayList<>();

        Workbook book;
        Sheet sheet;
        Cell cell_english, cell_chinese;
        int rows;

        File file = new File(filename);
        try {
            book = Workbook.getWorkbook(file);
        } catch (IOException | BiffException e) {
            Log.e(TAG, e.getLocalizedMessage());
            return wordList;
        }

        //获取第一个工作表对象
        sheet = book.getSheet(0);

        rows = sheet.getRows();

        for (int i = 0; i < rows; i++) {
            cell_english = sheet.getCell(1, i);
            cell_chinese = sheet.getCell(2, i);
            Word word = new Word(cell_english.getContents(), cell_chinese.getContents());
            wordList.add(word);
        }
        book.close();

        return wordList;
    }
}
