package com.zhkj.wallet.activity

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sunny.zy.base.BaseActivity
import com.sunny.zy.utils.*
import com.zhkj.wallet.R
import com.zhkj.wallet.contract.WalletContract
import com.zhkj.wallet.presenter.WalletPresenter
import com.zhkj.wallet.utils.PayPasswordUtil
import kotlinx.android.synthetic.main.act_pay_code.*
import kotlinx.coroutines.cancel
import org.json.JSONObject
import java.io.File

@Route(path = RouterManager.PAY_CODE_ACTIVITY)
class PayCodeActivity : BaseActivity(), WalletContract.IPayCodeView {

    private val walletPresenter: WalletPresenter by lazy {
        WalletPresenter(this)
    }

    private val payPasswordUtil: PayPasswordUtil by lazy {
        PayPasswordUtil(iv_qr_code, this) {
            val dataObject = JSONObject()
            dataObject.put("ordersId", payPasswordUtil.ordersId)
            dataObject.put("userId", SpUtil.getString(SpUtil.userId))
            dataObject.put("payPassword", it)

            val sendResult = walletPresenter.socketResultBean.bean?.send(dataObject.toString())
            if (sendResult == true) {
                LogUtil.i("webSocket发送成功：$dataObject")
            } else {
                LogUtil.i("webSocket发送失败：$dataObject")
            }
        }
    }

    var ordersId = ""
    var totalPrice = ""

    private var isSuccess = false

    override fun setLayout(): Int = R.layout.act_pay_code

    override fun initView() {
        defaultTitle("支付码")
    }

    override fun loadData() {
        //建立长连接
        walletPresenter.connectWebSocket()
    }

    override fun onClickEvent(view: View) {

    }

    override fun close() {
        walletPresenter.stopTimer()
        walletPresenter.socketResultBean.bean?.cancel()
        walletPresenter.cancel()
    }

    override fun showPayCodeData(file: File) {
        walletPresenter.startTimer() //启动倒计时
        //加载显示付款码
        GlideApp.with(this)
            .load(file)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.svg_pay_qr_code)
            .into(iv_qr_code)
    }

    override fun showCountdownData(number: String) {
        tv_hint.text = ("$number 秒后刷新")
    }

    override fun showSocketResult(isSuccess: Boolean) {

        this.isSuccess = isSuccess

        if (isSuccess) {
            walletPresenter.generatePayQrCode()
        } else {
            walletPresenter.stopTimer()
            walletPresenter.setDefaultTime()
            iv_qr_code.postDelayed({
                loadData()
            }, 5000)
        }
    }

    override fun showSocketMessage(message: String) {

        runOnUiThread {
            val msgObj = JSONObject(message)

//        1支付成功，2请输入密码，3余额不足，4密码不正确，5未知错误

            when (msgObj.optInt("status")) {
                1 -> {
                    finish()
                    ARouter.getInstance().build(RouterManager.PAY_RESULT_ACTIVITY).withString("payResult", "1").navigation()
                }
                2 -> {
                    msgObj.optJSONObject("data")?.let {

                        ordersId = it.optInt("ordersId").toString()
                        totalPrice = it.optString("totalPrice")

                        payPasswordUtil.showPayPasswordWindow(
                            payPasswordUtil.pay,
                            ordersId,
                            totalPrice.toFloat()
                        )
                    }
                }
                3 -> {
                    ARouter.getInstance().build(RouterManager.PAY_RESULT_ACTIVITY).withString("payResult", "0").navigation()
                }

                4 -> {
                    payPasswordUtil.showPayPasswordWindow(
                        payPasswordUtil.pay,
                        ordersId,
                        totalPrice.toFloat()
                    )
                }
            }

            ToastUtil.show(msgObj.optString("message"))
        }
    }

    override fun onPause() {
        super.onPause()
        if (isSuccess) {
            walletPresenter.stopTimer()
        }

    }

    override fun onRestart() {
        super.onRestart()
        if (isSuccess) {
            walletPresenter.startTimer()
        }
    }
}