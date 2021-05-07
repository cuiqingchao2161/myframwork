package com.cui.lib.core

import android.app.Application
import com.cui.mycommonlibrary.BuildConfig
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy
import kotlin.properties.Delegates

/**
 * description : TODO:类的作用
 * author : cuiqingchao
 * date : 2019/9/19 14:40
 */
open class BaseApplication : Application() {
    companion object {

        var instance: BaseApplication by Delegates.notNull()

        fun instance() = instance
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        initLog()
    }

    fun initLog(){
        var config = LogConfiguration.Builder()
                .logLevel(if(BuildConfig.DEBUG) LogLevel.ALL else LogLevel.ALL)             // Specify log level, logs below this level won't be printed, default: LogLevel.ALL
                .tag("CUI_PRO_TAG")                                         // Specify TAG, default: "X-LOG"
                .t()                                                   // Enable thread info, disabled by default
                .st(2)                                                 // Enable stack trace info with depth 2, disabled by default
                .b()                                                   // Enable border, disabled by default
//                .jsonFormatter(new MyJsonFormatter())                  // Default: DefaultJsonFormatter
//                .xmlFormatter(new MyXmlFormatter())                    // Default: DefaultXmlFormatter
//                .throwableFormatter(new MyThrowableFormatter())        // Default: DefaultThrowableFormatter
//                .threadFormatter(new MyThreadFormatter())              // Default: DefaultThreadFormatter
//                .stackTraceFormatter(new MyStackTraceFormatter())      // Default: DefaultStackTraceFormatter
//                .borderFormatter(new MyBoardFormatter())               // Default: DefaultBorderFormatter
//                .addObjectFormatter(AnyClass.class,                    // Add formatter for specific class of object
//                        new AnyClassObjectFormatter())                     // Use Object.toString() by default
//                .addInterceptor(new BlacklistTagsFilterInterceptor(    // Add blacklist tags filter
//                        "blacklist1", "blacklist2", "blacklist3"))
//                .addInterceptor(new MyInterceptor())                   // Add a log interceptor
                .build();

        var xlogPath = getFilesDir().getAbsolutePath();
        var androidPrinter = AndroidPrinter();             // Printer that print the log using android.util.Log
//        Printer consolePrinter = new ConsolePrinter();             // Printer that print the log to console using System.out
        var filePrinter = FilePrinter                      // Printer that print the log to the file system
                .Builder(xlogPath)                              // Specify the path to save log file
//                .fileNameGenerator(new ChangelessFileNameGenerator("log"))        // Default: ChangelessFileNameGenerator("log")
                .backupStrategy(object : NeverBackupStrategy(){})             // Default: FileSizeBackupStrategy(1024 * 1024)
//                .cleanStrategy(new FileLastModifiedCleanStrategy(MAX_TIME))     // Default: NeverCleanStrategy()
//                .flattener(new MyFlattener())                          // Default: DefaultFlattener
                .build();

        if (BuildConfig.DEBUG) {
            XLog.init(                                                 // Initialize XLog
                    config,                                                // Specify the log configuration, if not specified, will use new LogConfiguration.Builder().build()
                    androidPrinter,                                        // Specify printers, if no printer is specified, AndroidPrinter(for Android)/ConsolePrinter(for java) will be used.
//                consolePrinter,
                    filePrinter)
        } else {
            XLog.init(                                                 // Initialize XLog
                    config,                                                // Specify the log configuration, if not specified, will use new LogConfiguration.Builder().build()
//                    androidPrinter,                                        // Specify printers, if no printer is specified, AndroidPrinter(for Android)/ConsolePrinter(for java) will be used.
//                consolePrinter,
                    filePrinter)
        }
    }

}