package com.zihuan.translation.ui;

import com.zihuan.translation.LocalData;
import com.zihuan.translation.interfaces.SelectTextListener;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RulesDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList rules_list;
    private SelectTextListener selectTextListener;

    public RulesDialog(String candidate, SelectTextListener listener) {
        setContentPane(contentPane);
        setModal(true);
        setLocation(LocalData.INSTANCE.XLocation(), LocalData.INSTANCE.YLocation());
        selectTextListener = listener;
        getRootPane().setDefaultButton(buttonCancel);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonOK);
//        此处要判断一下文件文件类型
//        然后再获取当前行文本类型
//        最后根据类型再设置命名规则
        List<String> rules = Arrays.asList("", "m", "m_");
        List<String> data = new ArrayList();

        for (String ru : rules) {
            data.add(ru + candidate);
        }
        rules_list.setListData(data.toArray());
        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
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
