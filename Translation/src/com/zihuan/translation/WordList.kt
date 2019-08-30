package com.zihuan.translation

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.SelectionModel
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.zihuan.translation.interfaces.SelectTextListener
import com.zihuan.translation.mode.TranslationBean
import com.zihuan.translation.net.TranslateCallBack
import com.zihuan.translation.net.requestNetData
import com.zihuan.translation.ui.TranslationDialog
import java.awt.Toolkit
import javax.swing.ImageIcon


class WordList : AnAction(), SelectTextListener {


    /***
     * 第一步 选中文本
     * 第二步 api查询
     * 第三步 弹出待选列表
     * 第四步 弹出各种按配置规则显示的列表(将来考虑一下是否可以编辑)
     * 第五步 更改选中的文本的文案
     * 其他   设置规则页面
     */
    private lateinit var editor: Editor
    private var latestClickTime = 0L  // 上一次的点击时间

    /**    判断用户是否打开文本编辑页和是否选中文本**/
    override fun update(e: AnActionEvent) {
        val editor = e.getData(PlatformDataKeys.EDITOR)
        e.presentation.isEnabled = editor != null
        e.presentation.isEnabled = editor?.selectionModel?.hasSelection() ?: false
    }

    //    private val icon = IconLoader.getIcon("/icons/a8.png")
    lateinit var model: SelectionModel
    lateinit var project: Project
    override fun actionPerformed(e: AnActionEvent) {
        isDefaultIcon = true
        project = e.getData(PlatformDataKeys.PROJECT) ?: return
        if (!isFastClick(1000)) {
            /* 第一步 --> 选中单词 */
            // 获取动作编辑器
            editor = e.getData(PlatformDataKeys.EDITOR) ?: return
            // 获取选择模式对象
            model = editor.selectionModel
//            val startOffset = model.selectionStart
//            println("类型" + getSelectedType(editor.document, startOffset))
            // 选中文字
            val selectedText = model.selectedText ?: return
            if (selectedText.isBlank()) return
            /* 第二步 ---> API查询 */
            requestNetData(selectedText, object : TranslateCallBack<TranslationBean>() {
                override fun onSuccess(result: TranslationBean) = showPopupWindow(result)
                override fun onFailure(message: String) = Messages.showMessageDialog(project, message, "错误", Messages.getInformationIcon())
                override fun onError(message: String) = Messages.showMessageDialog(project, message, "错误", Messages.getInformationIcon())
            })

        }
    }

    private fun getSelectedType(document: Document, startOffset: Int): String {
        val text = document.text.substring(0, startOffset).trim()
        val startIndex = text.lastIndexOf(' ')
        return text.substring(startIndex + 1)
    }

    var mTranslationData = ArrayList<String>()
    /**
     * 第三步 --> 弹出对话框
     * @param result string result
     */
    private fun showPopupWindow(result: TranslationBean) {
        mTranslationData.clear()
        result.translation?.let { mTranslationData.addAll(it) }
        result.web?.forEach {
            if (it.key == result.query) {
                it.value?.forEach { v_it ->
                    if (!mTranslationData.contains(v_it)) {
                        mTranslationData.add(v_it)
                    }
                }
            }
        }
        var dialog = TranslationDialog(mTranslationData, this)
        dialog.pack()
//        dialog.setIconImage(ImageIcon("resources/img/head_icon.png").image)
//        val toolkit = Toolkit.getDefaultToolkit()
//        val icon = toolkit.getImage("resources/img/head_icon.png")
//        dialog.isResizable=false//禁止调整大小
        dialog.setLocationRelativeTo(null)
        dialog.isVisible = true
    }

    /***
     * 选中文案回调
     */
    override fun selectTextClick(text: String) {
        var runnable = Runnable {
            val startOffset = model.selectionStart
            val endOffset = model.selectionEnd
            editor.document.replaceString(startOffset, endOffset, text)
        }
        WriteCommandAction.runWriteCommandAction(project, runnable);
    }

    /**
     * 屏蔽多次选中
     */
    private fun isFastClick(timeMillis: Long): Boolean {
        val begin = System.currentTimeMillis()
        val end = begin - latestClickTime
        if (end in 1 until timeMillis) return true
        latestClickTime = begin
        return false
    }
}
