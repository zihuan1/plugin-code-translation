package com.zihuan.translation.ui;

import com.zihuan.translation.LocalData;
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
//        setLocationRelativeTo(null);
        selectTextListener = listener;
        translation_list.setListData(data.toArray());
        translation_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bt_translation_ok.addActionListener(e -> onOK());

        bt_translation_cancel.addActionListener(e -> onCancel());
        bt_setting.addActionListener(e -> {
            new SettingRules();

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
        dialog.setSize(LocalData.INSTANCE.getDIALOG_WIDTH(), LocalData.INSTANCE.getDIALOG_HEIGHT());
        dialog.pack();
        dialog.setVisible(true);
//        JOptionPane.showMessageDialog(this, "选中的文本 " + translation_list.getSelectedValue().toString(), null, JOptionPane.INFORMATION_MESSAGE);
    }

    private void onCancel() {
        dispose();
    }

    public TranslationDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(bt_translation_ok);
//        居中显示
        setLocation(LocalData.INSTANCE.XLocation(), LocalData.INSTANCE.YLocation());
//        setBounds(width / 2 - (LocalData.INSTANCE.getDIALOG_WIDTH() / 2), height / 2 - (LocalData.INSTANCE.getDIALOG_HEIGHT() / 2), LocalData.INSTANCE.getDIALOG_WIDTH(), LocalData.INSTANCE.getDIALOG_HEIGHT());
    }

    public static void main(String[] args) {
        TranslationDialog dialog = new TranslationDialog();
        dialog.pack();
        dialog.setSize(500, 300);
        dialog.setVisible(true);
//        System.exit(0);
    }

}
