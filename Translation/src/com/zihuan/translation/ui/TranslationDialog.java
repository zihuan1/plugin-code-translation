package com.zihuan.translation.ui;

import com.zihuan.translation.interfaces.SelectTextListener;
import com.zihuan.translation.u.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TranslationDialog extends JDialog {
    private JPanel contentPane;
    private JButton bt_translation_ok;
    private JButton bt_translation_cancel;
    private JList translation_list;
    private JButton bt_setting;

    private SelectTextListener selectTextListener;


    public TranslationDialog(List<String> data, SelectTextListener listener) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(bt_translation_ok);
        setTitle("待选翻译列表");
        setIconImage(new ImageIcon("resources/img/head_icon.png").getImage());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);//关闭时销毁对话框
        selectTextListener = listener;
        translation_list.setListData(data.toArray());
        translation_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bt_translation_ok.addActionListener(e -> onOK());
        bt_translation_cancel.addActionListener(e -> onCancel());
        bt_translation_cancel.setMnemonic(KeyEvent.VK_Q);
        bt_setting.addActionListener(e -> {
            new SettingRules();
        });
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(e -> {
            onCancel();
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        Logger.error("确定");
        dispose();
        if (translation_list.getSelectedValue() == null) return;
        RulesDialog dialog = new RulesDialog(translation_list.getSelectedValue().toString(), selectTextListener);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
//        JOptionPane.showMessageDialog(this, "选中的文本 " + translation_list.getSelectedValue().toString(), null, JOptionPane.INFORMATION_MESSAGE);
    }

    private void onCancel() {
        Logger.error("取消");
        dispose();
    }

    public TranslationDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(bt_translation_ok);
    }

    public static void main(String[] args) {
        TranslationDialog dialog = new TranslationDialog();
        dialog.setIconImage(new ImageIcon("resources/img/head_icon.png").getImage());
        dialog.pack();
        dialog.setSize(500, 300);
        dialog.setVisible(true);
//        System.exit(0);
    }

}
