package com.zhkj.wallet.activity

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.sunny.zy.base.BaseActivity
import com.sunny.zy.utils.RouterManager
import com.zhkj.wallet.R
import com.zhkj.wallet.utils.MoneyInputFilter
import kotlinx.android.synthetic.main.act_recharge.*

/**
 * 充值页面
 */
@Route(path = RouterManager.RECHARGE_ACTIVITY)
class RechargeActivity : BaseActivity() {

    var wxPay = 0
    var aliPay = 1

    var payType = wxPay

    override fun setLayout(): Int = R.layout.act_recharge

    override fun initView() {
        defaultTitle("充值")
        edt_money.filters = arrayOf(MoneyInputFilter())
    }

    override fun loadData() {
        setOnClickListener(
            view_wx_parent,
            rbtn_wx,
            view_ali_parent,
            rbtn_ali
        )
    }

    override fun onClickEvent(view: View) {
        when (view.id) {
            view_wx_parent.id, rbtn_wx.id -> {
                payType = wxPay
                singleSelect()
            }
            view_ali_parent.id, rbtn_ali.id -> {
                payType = aliPay
                singleSelect()
            }
        }
    }


    private fun singleSelect() {
        if (payType == wxPay) {
            rbtn_ali.isChecked = false
            rbtn_wx.isChecked = true
        } else {
            rbtn_wx.isChecked = false
            rbtn_ali.isChecked = true
        }
    }

    override fun close() {

    }
}