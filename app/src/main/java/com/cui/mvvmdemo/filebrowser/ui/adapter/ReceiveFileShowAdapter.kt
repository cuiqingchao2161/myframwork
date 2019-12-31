package com.hiscene.presentation.filebrowser.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cui.mvvmdemo.R
import com.cui.mvvmdemo.filebrowser.entity.FileEntity
import com.cui.mvvmdemo.filebrowser.utils.FileUtils
import kotlinx.android.synthetic.main.item_file_show.view.*
import java.io.File
import java.util.*

/**
 * 作者：chs on 2017-08-28 10:54
 * 邮箱：657083984@qq.com
 * 选择附件后显示
 */

class ReceiveFileShowAdapter(private val mContext: Context?, dataList: List<FileEntity>) : RecyclerView.Adapter<ReceiveFileShowAdapter.FileShowViewHolder>() {
    private val mDataList: ArrayList<FileEntity>
    private var mOnItemClickListener: OnFileItemClickListener? = null

    fun setOnItemClickListener(mOnItemClickListener: OnFileItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }

    init {
        mDataList = dataList as ArrayList<FileEntity>
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileShowViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_file_show, parent, false)
        return FileShowViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileShowViewHolder, position: Int) {
        val fileEntity = mDataList[position]
        val file = fileEntity.file
        file?.let {
            if(file.isDirectory){
                holder.itemView.iv_type.setImageResource(R.mipmap.file_picker_folder)
                holder.itemView.tv_name.text = fileEntity.name
                holder.itemView.tv_detail.visibility = View.GONE
            }else{
                holder.itemView.tv_detail.visibility = View.VISIBLE
                holder.itemView.tv_name.text = fileEntity.name
                holder.itemView.tv_detail.text = fileEntity.size
                if (fileEntity.fileType != null) {
                    when (fileEntity.fileType.title) {
                        "IMG" -> {
                            Glide.with(mContext!!).load(File(fileEntity.file.path)).placeholder(R.mipmap.placeholder_img).error(R.mipmap.default_img_failed).into( holder.itemView.iv_type)
                        }
                        "APK" -> {
                            holder.itemView.iv_type.setImageDrawable(FileUtils.getApkIcon(mContext, fileEntity.file.path))
                        }
                        else -> {
                            holder.itemView.iv_type.setImageResource(fileEntity.fileType.iconStyle)
                        }
                    }
                } else {
                    holder.itemView.iv_type.setImageResource(R.mipmap.ic_unknow)//未知文件类型
                }
            }
        }

        holder.itemView.setOnClickListener {
            if (mOnItemClickListener != null) {
                mOnItemClickListener!!.click(holder.adapterPosition)
            }
        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class FileShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    interface OnFileItemClickListener {
        fun click(position: Int)
    }
}
