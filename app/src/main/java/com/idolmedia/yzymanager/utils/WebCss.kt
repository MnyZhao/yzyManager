package com.idolmedia.yzymanager.utils

/**
 *  时间：2019/7/8-17:54
 *  公司:北京爱豆文化传媒有限公司
 *  com.idolmedia.lib_common.widget.web WebCss
 *  描述：
 */
object WebCss {

    const val css = "<style type=\"text/css\"> img {" +
            "width:100%;" +//限定图片宽度填充屏幕
            "height:auto;" +//限定图片高度自动
            "}" +
            "body {" +
            "font-size:15px;" +//限定网页中文字的大小为40px,请务必根据各种屏幕分辨率进行适配更改
            "word-wrap:break-word;" +//允许自动换行(汉字网页应该不需要这一属性,这个用来强制英文单词换行,类似于word/wps中的西文换行)
            " box-sizing: border-box;" +
            "}" +
            //视频标签
            "video {\n" +
            "      width: 100%;\n" +
            "      max-width: 100%;\n" +
            "  \n" +
            "      background-color: #ffffff;\n" +
            "    }\n" +
            "    .videoWrapper {\n" +
            "\t  position: relative;\n" +
            "\t  padding-bottom: 56.25%; /* 16:9 */\n" +
            "\t  padding-top: 25px;\n" +
            "\t  height: 0;\n" +
            "    }\n" +
            "video::-webkit-media-controls {\n" +
            "    overflow: hidden !important;\n" +
            "}\n" +
            "video::-webkit-media-controls-enclosure {\n" +
            "    width: calc(100% + 32px);\n" +
            "    margin-left: auto;\n" +
            "}\n" +
            "    .videoWrapper object,\n" +
            "    .videoWrapper embed,\n" +
            "    .videoWrapper video{\n" +
            "        position: absolute;\n" +
            "        top: 0;\n" +
            "        left: 0;\n" +
            "        width: 100%;\n" +
            "        height: 100%;\n" +
            "    }" + "   .videoWrapper .fullscreenBtn {\n" +
            "      position: absolute;\n" +
            "      bottom: 0;\n" +
            "      right: 0;\n" +
            "\n" +
            "      width: 48px;\n" +
            "      height: 48px;\n" +
            "\n" +
            "      background: none;\n" +
            "      border: 0;\n" +
            "\n" +
            "      margin: 10px;\n" +
            "      padding: 8px;\n" +
            "    }" +
            "+</style>"

    fun getHtml(str:String):String{

        return "<html><header>  <meta charset=\"utf-8\">\n" +
                "  <title>JS Bin</title>\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, minimal-ui\">" + css + "</header><body>" + str + "</body><script src=\"//api.html5media.info/1.1.8/html5media.min.js\"></script></html>"
    }

}