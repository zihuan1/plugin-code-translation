package com.zihuan.translation

import org.apache.commons.lang.StringUtils
import org.jetbrains.annotations.NonNls
import java.awt.Toolkit
import java.util.*


object LocalData {
	const val PREFIX_NAME = "A8Translate"
    val width = Toolkit.getDefaultToolkit().screenSize.width
    val height = Toolkit.getDefaultToolkit().screenSize.height
    val DIALOG_WIDTH = 500
    val DIALOG_HEIGHT = 300

    fun XLocation():Int{
        return width.div(2).minus(DIALOG_WIDTH.div(2))
    }
    fun YLocation():Int{
        return height.div(2).minus(DIALOG_HEIGHT.div(2))
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