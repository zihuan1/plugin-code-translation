package com.zihuan.translation.ui;

import com.zihuan.translation.interfaces.SelectTextListener;
import com.zihuan.translation.u.Logger;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RulesDialog extends JDialog {
    private JPanel contentPane;
    private JButton bt_selected_ok;
    private JButton bt_selected_cancel;
    private JList rules_list;
    private SelectTextListener selectTextListener;

    public RulesDialog(String candidate, SelectTextListener listener) {
        setContentPane(contentPane);
        setModal(true);
        selectTextListener = listener;
        getRootPane().setDefaultButton(bt_selected_ok);
        setTitle("目标翻译");
//        Toolkit toolkit = Toolkit.getDefaultToolkit();
//        Image icon = toolkit.getImage("/icon/head_icon.png");
//        setIconImage(icon);
        setIconImage(new ImageIcon("resources/img/head_icon.png").getImage());

        List<String> rules = Arrays.asList("", "", "m", "m_");
        List<String> data = new ArrayList();
        StringBuilder builder = caseFirstLetter(candidate);
        bt_selected_cancel.setMnemonic(KeyEvent.VK_Q);
        for (int i = 0, size = rules.size(); i < size; i++) {
            if (i != 0) {//首字母大写
                candidate = builder.replace(0, 1, String.valueOf(builder.charAt(0)).toUpperCase()).toString();
            } else
                candidate = builder.toString();
            data.add(rules.get(i) + candidate);
        }
        rules_list.setListData(data.toArray());
        bt_selected_ok.addActionListener(e -> onOK());
        bt_selected_cancel.addActionListener(e -> onCancel());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        //esc键监听
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    //    将首字母转换为大写
    private StringBuilder caseFirstLetter(String text) {
        StringBuilder builder = new StringBuilder(text);
        char str[] = text.toCharArray();
        for (int i = 0, size = str.length; i < size; i++) {
            if (" ".equals(String.valueOf(str[i]))) {
                String toUpperCase = String.valueOf(str[i + 1]).toUpperCase();
                Logger.error("转换中 " + toUpperCase);
                builder.replace(i + 1, i + 2, toUpperCase);
            }
        }
        String string = builder.toString().replace(" ", "");
        builder.delete(0, builder.length()).append(string);
        Logger.error("转换后 " + builder);
        return builder;
    }

    private void onOK() {
        dispose();
        if (rules_list.getSelectedValue() != null) {
            selectTextListener.selectTextClick(rules_list.getSelectedValue().toString());
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
//        RulesDialog dialog = new RulesDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
    }
}
