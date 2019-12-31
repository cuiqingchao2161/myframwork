package com.cui.mvvmdemo.bean

/**
 * description : 公共模块接口返回结果
 * author : cuiqingchao
 * date : 2019/9/20 10:29
 */
class ReqResult<T> {
    var status: Int = 0         //0为失败 1为成功
    var data: T? = null         //成功时返回的数据体,如果没有返回此数据体T即为String填充返回成功的自定义提示信息
    var errorMsg: String = ""   //失败时返回的信息
}