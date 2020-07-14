package com.zhkj.smartpolice.haircut

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunny.zy.base.BaseActivity
import com.sunny.zy.base.BaseRecycleAdapter
import com.zhkj.smartpolice.R
import com.zhkj.smartpolice.haircut.adapter.HaircutTimeAdapter
import com.zhkj.smartpolice.haircut.adapter.HaircutWeekAdapter
import com.zhkj.smartpolice.haircut.adapter.LeaderReserveTimeAdapter
import com.zhkj.smartpolice.haircut.bean.MerchantTime
import com.zhkj.smartpolice.haircut.bean.WeekDayBean
import com.zhkj.smartpolice.merchant.model.MerchantContract
import com.zhkj.smartpolice.merchant.model.MerchantPresenter
import kotlinx.android.synthetic.main.act_receive_time.*
import java.util.*


open class HaircutOrderTimeActivity : BaseActivity(), MerchantContract.IReserveTimeView {

    val presenter by lazy {
        MerchantPresenter(this)
    }

    val shopId: String by lazy {
        intent.getStringExtra("shopId")
    }

    private val calendar = Calendar.getInstance(Locale.CHINA)
    private val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    private val defaultWeeks = arrayListOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")

    var timeAdapter: BaseRecycleAdapter<MerchantTime> = HaircutTimeAdapter().apply {
        setOnItemClickListener { _, position ->
            if (getData(position).setNumber - getData(position).reserveNumber > 0) {
                index = position
                notifyDataSetChanged()
            }
        }
    }

    var weekAdapter: BaseRecycleAdapter<WeekDayBean> = HaircutWeekAdapter(currentDay, arrayListOf()).apply {
        setOnItemClickListener { _: View, position: Int ->
            this.currentDay = getData(position).day
            notifyDataSetChanged()
            presenter.loadReserveTime(getEndData(this.currentDay), shopId)
        }
    }

    override fun setLayout(): Int = R.layout.act_receive_time

    override fun initView() {

        defaultTitle("预约时间")

        val weekDayList = arrayListOf<WeekDayBean>()
        val currentWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1

        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in currentDay..maxDay) {
            calendar.set(Calendar.DAY_OF_MONTH, i)
            val week = defaultWeeks[calendar.get(Calendar.DAY_OF_WEEK) - 1]
            weekDayList.add(WeekDayBean(week, i))
        }

        weekAdapter.addData(weekDayList)

        recycler_date.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        recycler_date.adapter = weekAdapter

        recycler_time.layoutManager = GridLayoutManager(this, 4)
        recycler_time.addItemDecoration(object : RecyclerView.ItemDecoration() {
            private var order_margin = resources.getDimension(R.dimen.dp_8).toInt()
            private var margin = resources.getDimension(R.dimen.dp_4).toInt()

            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)

                val position = (parent.getChildLayoutPosition(view) + 1)

                if (position % 4 == 0) {
                    outRect.right = order_margin
                } else {
                    outRect.right = margin
                }


                if ((position - 1) % 4 == 0) {
                    outRect.left = order_margin
                } else {
                    outRect.left = margin
                }

            }
        })
        recycler_time.adapter = timeAdapter

        setOnClickListener(btn_sure)

    }

    override fun onClickEvent(view: View) {
        when (view.id) {
            btn_sure.id -> {
                val intent = Intent()
                (recycler_date.adapter as HaircutWeekAdapter).let {
                    intent.putExtra("manageTime", timeAdapter.getData(it.index).manageTime)
                    intent.putExtra("day", it.getData(it.index).day)
                    intent.putExtra("week", it.getData(it.index).week)

                    timeAdapter.getData(it.index).let { bean ->
                        intent.putExtra("beginTime", bean.beginTime)
                        intent.putExtra("endTime", bean.endTime)
                        intent.putExtra("manageId", bean.manageId)
                    }
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun loadData() {
        presenter.loadReserveTime(getEndData(currentDay), shopId)
    }

    override fun close() {

    }

    fun getEndData(day: Int): String {
        val month = calendar.get(Calendar.MONTH) + 1

        return "${calendar.get(Calendar.YEAR)}-${if (month < 10) "0$month" else month}-$day"
    }


    override fun showReserveTime(data: ArrayList<MerchantTime>) {

        val index = data.indexOf(data.find { it.setNumber - it.reserveNumber > 0 })

        if (timeAdapter is HaircutTimeAdapter) {
            (timeAdapter as HaircutTimeAdapter).index = index
        }

        if (timeAdapter is LeaderReserveTimeAdapter) {
            (timeAdapter as LeaderReserveTimeAdapter).index = index
        }

        timeAdapter.clearData()
        timeAdapter.addData(data)
        timeAdapter.notifyDataSetChanged()
    }

}