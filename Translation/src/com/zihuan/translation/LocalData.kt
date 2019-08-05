package com.zihuan.translation

import org.apache.commons.lang.StringUtils
import org.jetbrains.annotations.NonNls
import java.awt.Toolkit
import java.util.*


object LocalData {
    val width = Toolkit.getDefaultToolkit().screenSize.width
    val height = Toolkit.getDefaultToolkit().screenSize.height
    val DIALOG_WIDTH = 500
    val DIALOG_HEIGHT = 300
    //	变量类型
    var SELECTED_TEXT_VARIABLE_TYPES = arrayOf("String", "int", "Int", "Integer", "boolean", "Boolean", "float", "Float", "double", "Double")
    //	类类型
    var SELECTED_TEXT_CLASS_TYPES = arrayOf("class", "interface", "Enum")

    fun XLocation(width: Int = DIALOG_WIDTH): Int {
        return width.div(2).minus(width.div(2))
    }

    fun YLocation(height: Int = DIALOG_HEIGHT): Int {
        return height.div(2).minus(height.div(2))
    }


    private val p = Properties()
    // private val f = File(System.getProperty("user.home") + "/a8temp.properties")

    init {
        // if (!f.exists()) f.createNewFile()
        // p.load(FileReader(f))
    }

    fun store(@NonNls key: String, @NonNls value: String) {
        p[StringUtils.uncapitalize(key)] = value
        save()
    }

    fun clear() {
        p.clear()
        save()
    }

    private fun save() = Unit // p.store(FileWriter(f), "Created by a8translate")

    fun read(@NonNls key: String): String? = p.getProperty(StringUtils.uncapitalize(key))
}