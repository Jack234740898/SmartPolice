package com.zhkj.smartpolice.maintain.activity

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.sunny.zy.base.BaseActivity
import com.sunny.zy.utils.LogUtil
import com.sunny.zy.utils.SpUtil
import com.sunny.zy.utils.ToastUtil
import com.zhkj.smartpolice.R
import com.zhkj.smartpolice.base.UserManager
import com.zhkj.smartpolice.maintain.adapter.MaintainPhotographAdapter
import com.zhkj.smartpolice.maintain.bean.FindImagePathBean
import com.zhkj.smartpolice.maintain.bean.SucceedBean
import com.zhkj.smartpolice.maintain.presenter.MaintainPresenter
import com.zhkj.smartpolice.maintain.view.IMaintainView
import kotlinx.android.synthetic.main.act_audit_info.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AuditInfoActivity : BaseActivity(), IMaintainView {
    var isType: Boolean = true
    var applyId: String? = null
    var applyDate: String? = null
    var applyContent: String? = null
    var findImagePathBean: ArrayList<FindImagePathBean> = ArrayList()

    private val maintainPresenter: MaintainPresenter by lazy {
        MaintainPresenter(this)
    }

    private val adapter: MaintainPhotographAdapter by lazy {
        MaintainPhotographAdapter(findImagePathBean)
    }

    override fun setLayout(): Int = R.layout.act_audit_info

    override fun initView() {
        defaultTitle("审批详情")
        var intent = intent
        tv_petitioner.text = intent.getStringExtra("petitioner")
        tv_petitioner_phone.text = intent.getStringExtra("petitionerPhone")
        tv_deptName.text = intent.getStringExtra("deptName")
        tv_apply_date.text = intent.getStringExtra("applyDate")
        tv_apply_content.text = intent.getStringExtra("applyContent")
        applyId = intent.getStringExtra("applyId")
        applyDate = intent.getStringExtra("applyDate")
        applyContent = intent.getStringExtra("applyContent")
        isType = intent.getBooleanExtra("isType", true)
        var groupId = intent.getStringExtra("groupId")
        groupId?.let {
            maintainPresenter.onFindImagePath(groupId)
        }
        rv_maintain_img.layoutManager = GridLayoutManager(this, 4)
        rv_maintain_img.adapter = adapter

        tv_refuse.setOnClickListener(this)
        tv_confirm.setOnClickListener(this)
        tv_task_issue.setOnClickListener(this)
        if (isType) {
            tv_refuse.visibility = View.VISIBLE
            tv_confirm.visibility = View.VISIBLE
            tv_task_issue.visibility = View.INVISIBLE
        } else {
            tv_refuse.visibility = View.INVISIBLE
            tv_confirm.visibility = View.INVISIBLE
            tv_task_issue.visibility = View.VISIBLE
        }

    }

    override fun onClickEvent(view: View) {
        when (view.id) {
            R.id.tv_refuse -> {
                getRequestConnect("2")
            }

            R.id.tv_confirm -> {
                getRequestConnect("1")
            }

            R.id.tv_task_issue -> {
                PublishTaskActivity.intent(
                    this@AuditInfoActivity,
                    applyContent, applyId, applyDate
                )
            }
        }
    }

    override fun loadData() {

    }

    override fun close() {

    }

    @SuppressLint("SimpleDateFormat")
    fun getData(): String {
        return if (Build.VERSION.SDK_INT >= 24) {
            SimpleDateFormat("yyyy-MM-dd").format(Date())
        } else {
            val tms = Calendar.getInstance()
            tms.get(Calendar.YEAR)
                .toString() + "-" + tms.get(Calendar.MONTH) + "-" + tms.get(Calendar.DAY_OF_MONTH)
                .toString()
        }
    }

    override fun onMaintainFeedback(succeedBean: SucceedBean) {
        super.onMaintainFeedback(succeedBean)
        succeedBean.code?.let {
            var code = it.toInt()
            if (code == 0) {
                ToastUtil.show("审批成功")
                val intent = intent
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    fun getRequestConnect(opinionType: String) {
        val date = getData()
        val userInfoBean = UserManager.getInfo()
        val userName = userInfoBean.roleName
        SpUtil
        LogUtil.i("date===========$date")
        maintainPresenter.onMaintainFeedback(
            intent.getStringExtra("applyContent"),
            intent.getStringExtra("applyId"),
            date, opinionType, userName
        )
    }

    override fun onFindImagePath(info: ArrayList<FindImagePathBean>) {
        LogUtil.i("图片的下载========$info")
        findImagePathBean.clear()
        findImagePathBean.addAll(info)
        adapter.notifyDataSetChanged()
    }
}