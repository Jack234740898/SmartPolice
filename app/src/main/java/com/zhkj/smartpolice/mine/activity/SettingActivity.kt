package com.zhkj.smartpolice.mine.activity

import android.view.View
import com.sunny.zy.base.BaseActivity
import com.sunny.zy.utils.ToastUtil
import com.sunny.zy.widget.dialog.ConfirmDialog
import com.zhkj.smartpolice.R
import com.zhkj.smartpolice.base.UserManager
import com.zhkj.smartpolice.login.activity.LoginActivity
import com.zhkj.smartpolice.widget.CacheClearDialog
import com.zhkj.smartpolice.widget.CacheDataManager
import kotlinx.android.synthetic.main.act_setting.*

class SettingActivity : BaseActivity() {

    private var cacheSize = ""

    private val exitDialog: ConfirmDialog by lazy {
        val dialog = ConfirmDialog(this)
        dialog.prompt = "是否确认退出应用？"
        dialog.onConfirmListener = {
            LoginActivity.intent(this, true)
        }
        dialog
    }

    private val cacheClearDialog: CacheClearDialog by lazy {
        val dialog = CacheClearDialog(this)
        dialog.cacheSize = cacheSize
        dialog.onConfirmListener = {
            CacheDataManager.clearAllCache(this)
            ToastUtil.show("缓存清理完毕")
            dialog.cacheSize = ""
            loadData()
        }
        dialog
    }

    override fun setLayout(): Int = R.layout.act_setting

    override fun initView() {

        defaultTitle("设置")

        item_name.endTextView.text = UserManager.getUserBean().userName

        item_cache.setOnClickListener(this)
        btn_logout.setOnClickListener(this)

        /*
         v_point.visibility = if (Constant.isVersionUpdate) View.VISIBLE else View.GONE
         item_version.endTextView.text = ("V " + BuildConfig.VERSION_NAME)
         item_version.setOnClickListener(this)
         tv_service.setOnClickListener(this)
         tv_privacy.setOnClickListener(this)
         */


    }

    override fun onClickEvent(view: View) {
        when (view.id) {
            R.id.item_cache -> cacheClearDialog.show()
            R.id.btn_logout -> exitDialog.show()
            /*
            R.id.item_password -> startActivity(Intent(this, ModifyPasswordActivity::class.java))
            R.id.item_feedback -> startActivity(Intent(this, UserFeedbackActivity::class.java))
            R.id.item_about_us -> startActivity(Intent(this, UserFeedbackActivity::class.java))
            R.id.item_version -> presenter.checkVersionUpdate(BuildConfig.VERSION_CODE)
            R.id.tv_service -> {
                startActivity(Intent(this, AgreementActivity::class.java).apply {
                    putExtra(IntentKey.agreementType, 2)
                })
            }
            R.id.tv_privacy -> {
                startActivity(Intent(this, AgreementActivity::class.java).apply {
                    putExtra(IntentKey.agreementType, 3)
                })
            }*/
        }
    }

    override fun loadData() {
        cacheSize = CacheDataManager.getTotalCacheSize(this)
        item_cache.endTextView.text = cacheSize
    }

    override fun close() {

    }

}