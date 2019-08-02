package com.zihuan.translation

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.Messages
import com.zihuan.translation.interfaces.SelectTextListener
import com.zihuan.translation.mode.TranslationBean
import com.zihuan.translation.net.TranslateCallBack
import com.zihuan.translation.net.requestNetData
import com.zihuan.translation.ui.TranslationDialog

class WordList : AnAction(), SelectTextListener {


    /***
     * 第一步 选中文本
     * 第二步 api查询
     * 第三步 弹出待选列表
     * 第四步 弹出各种按配置规则显示的列表(将来考虑一下是否可以编辑)
     * 第五步 更改选中的文本的文案
     * 其他 1.设置规则页面
     *      2.检测当前是类名还是接口名称
     *      3.如果是多个单词的话要首字母大写然后拼接
     *      4.能不能判断一下选中的文本是什么类型
     */
    private lateinit var editor: Editor
    private var latestClickTime = 0L  // 上一次的点击时间
    /**    判断用户是否打开文本编辑页和是否选中文本**/
    override fun update(e: AnActionEvent) {
        val editor = e.getData(PlatformDataKeys.EDITOR)
        e.presentation.isEnabled = editor != null
        e.presentation.isEnabled = editor?.selectionModel?.hasSelection() ?: false
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.getData(PlatformDataKeys.PROJECT)
        if (!isFastClick(1000)) {
            /* 第一步 --> 选中单词 */
            // 获取动作编辑器
            editor = e.getData(PlatformDataKeys.EDITOR) ?: return
            // 获取选择模式对象
            val model = editor.selectionModel
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
        var test = TranslationDialog(mTranslationData, this)
        test.pack()
        test.isVisible = true

//        ApplicationManager.getApplication().invokeLater {
//            JBPopupFactory.getInstance()
//                    .createHtmlTextBalloonBuilder(result.toString(), null, JBColor(Color(186, 238, 186), Color(73, 117, 73)), null)
//                    .setFadeoutTime(15000)
//                    .setHideOnAction(true)
//                    .createBalloon()
//                    .show(JBPopupFactory.getInstance().guessBestPopupLocation(editor), Balloon.Position.below)
//        }
    }

    /***
     * 选中文案回调
     */
    override fun selectTextClick(text: String) {
        ApplicationManager.getApplication().runWriteAction {
            editor.document.setText(text)
        }

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
