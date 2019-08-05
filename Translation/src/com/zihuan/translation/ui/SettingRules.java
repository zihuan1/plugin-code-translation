package com.zihuan.translation.ui;

import javax.swing.*;
import java.io.*;

import static com.sun.org.apache.bcel.internal.util.SecuritySupport.getResourceAsStream;

public class SettingRules extends JFrame {
    private JTextField tf_global_var;
    private JTextField tf_local_var;
    private JPanel jp_root;
    private JTextField tf_class_name;
    private JTextField tf_fun_name;
    private JButton bt_save;
    private JTextField tf_cancel_key;

    public SettingRules() {
        initView();
    }

    private void initView() {
        setLocationRelativeTo(null);
        setTitle("规则设置");
        setContentPane(jp_root);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
//        居中显示
//        setLocation(LocalData.INSTANCE.XLocation(), LocalData.INSTANCE.YLocation());
        setVisible(true);
//        loadFile();
//        setListener();
    }

    private void setListener() {
        bt_save.addActionListener(e -> {
            saveFile();
        });
    }

    private void loadFile() {
        InputStream is = getResourceAsStream("config/config.txt");
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] arr = new byte[1024 * 8];
        int len;
        while (true) {
            try {
                if (!((len = bis.read(arr)) != -1)) break;
                System.out.println(new String(arr, "utf8"));
            } catch (IOException e) {

            }
        }
    }

    private void saveFile() {
        InputStream inputStream = getResourceAsStream("config/config.txt");
        OutputStream os = null;
        try {
            String path = "resources/config/";
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流保存到本地文件
            File tempFile = new File(path);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            os = new FileOutputStream(tempFile.getPath() + File.separator + "config.txt");
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            System.out.println("保存完毕");
        } catch (IOException e) {
            System.out.println("异常1" + e.toString());
        } catch (Exception e) {
            System.out.println("异常2" + e.toString());
        } finally {
            // 完毕，关闭所有链接
            try {
                os.close();
                inputStream.close();
            } catch (IOException e) {
                System.out.println("异常3" + e.toString());
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {

        new SettingRules();
    }


}
