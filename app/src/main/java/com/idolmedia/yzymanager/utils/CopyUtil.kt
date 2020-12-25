package com.idolmedia.yzymanager.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.idolmedia.yzymanager.base.BaseApplication
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.net.URL

/**
 *  时间：2019/9/10-18:08
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.lib_common.utils CopyUtil
 *  描述：
 */
object CopyUtil {

    fun copy(context: Context,copyStr:String?){
        val copy = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        copy.setPrimaryClip(ClipData.newPlainText("Label", copyStr))
        ToastUtil.show("已复制到粘贴板")
    }

    fun downImage(path:String?){
        var mBitmap:Bitmap
        Thread(Runnable {
            try {
                if (!path.isNullOrEmpty()){
                    val url = URL(path)
                    val inputStream = url.openStream()
                    mBitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream.close()
                    saveBitmap(mBitmap,System.currentTimeMillis().toString())
                }

            }catch (e:Exception){
                ToastUtil.show("保存失败")
            }finally {

            }
        }).start()
    }


    /*** 将图片存到本地 */
    private fun saveBitmap(bm: Bitmap, picName: String): Uri? {
        try {
            val dir: String = Environment.getExternalStorageDirectory().absolutePath + "/idolmedia/" + picName + ".png"
            val file = File(dir)
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            val out = FileOutputStream(file)
            bm.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()

            //通知相册更新
            MediaStore.Images.Media.insertImage(
                BaseApplication.instance.contentResolver,
                BitmapFactory.decodeFile(file.absolutePath), file.name,
                null
            )
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val uri = Uri.fromFile(file)
            intent.data = uri
            BaseApplication.instance.sendBroadcast(intent)

            ToastUtil.show("成功保存到相册")
            return uri
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace();
        } finally {
            if (!bm.isRecycled){
                bm.recycle()
            }
        }
        return null
    }

}