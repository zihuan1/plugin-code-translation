package com.zihuan.translation.ui;

import com.zihuan.translation.interfaces.SelectTextListener;

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
        setLocationRelativeTo(null);
        selectTextListener = listener;
        // 得到显示器屏幕的宽高
//        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
//        int height = Toolkit.getDefaultToolkit().getScreenSize().height;

        translation_list.setListData(data.toArray());
        translation_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bt_translation_ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        bt_translation_cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        dispose();
        if (translation_list.getSelectedValue() == null) return;
        RulesDialog dialog = new RulesDialog(translation_list.getSelectedValue().toString(), selectTextListener);
        dialog.pack();
        dialog.setVisible(true);
//        JOptionPane.showMessageDialog(this, "选中的文本 " + translation_list.getSelectedValue().toString(), null, JOptionPane.INFORMATION_MESSAGE);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public TranslationDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(bt_translation_ok);
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
//        居中显示
        setBounds(width / 2 - 250, height / 2 - 150, 500, 300);
    }

    public static void main(String[] args) {
        TranslationDialog dialog = new TranslationDialog();
        dialog.pack();
        dialog.setVisible(true);
//        System.exit(0);
    }

}
