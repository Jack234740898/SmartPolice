package com.zhkj.smartpolice.maintain.activity

import android.view.View
import androidx.fragment.app.Fragment
import com.sunny.zy.base.BaseActivity
import com.zhkj.smartpolice.R
import com.zhkj.smartpolice.maintain.adapter.MyFragmentAdapter
import com.zhkj.smartpolice.maintain.fragment.AccomplishFragment
import com.zhkj.smartpolice.maintain.fragment.MaintainUntreatedFragment
import kotlinx.android.synthetic.main.act_apply_maintain_list.*


class PropertyManageActivity: BaseActivity() {
    var fragmentList: ArrayList<Fragment> = ArrayList()
    var mTitle: ArrayList<String> = ArrayList()
    override fun setLayout(): Int = R.layout.act_apply_maintain_list

    override fun initView() {
        defaultTitle("维修列表")
    }

    override fun onClickEvent(view: View) {

    }

    override fun loadData() {
        fragmentList.add(MaintainUntreatedFragment())
        fragmentList.add(AccomplishFragment())
        mTitle.add("未处理")
        mTitle.add("已处理")
        vp_maintain.adapter = MyFragmentAdapter(supportFragmentManager, fragmentList, mTitle)
        tab_layout.setupWithViewPager(vp_maintain)
    }

    override fun close() {

    }
}