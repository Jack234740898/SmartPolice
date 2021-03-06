package com.zhkj.smartpolice.meal.model

import com.sunny.zy.base.BaseModel
import com.sunny.zy.base.BasePresenter
import com.sunny.zy.base.IBaseView
import com.zhkj.smartpolice.meal.bean.MealGoodsBean
import com.zhkj.smartpolice.meal.bean.MealMenuBean
import com.zhkj.smartpolice.meal.bean.MealRecordBean
import com.zhkj.smartpolice.meal.bean.RestaurantBean

interface MealContract {

    interface IRestaurantView : IBaseView {
        fun showRestaurantList(data: ArrayList<RestaurantBean>)
    }

    interface IMealMenuView : IBaseView {
        fun loadMealMenu(data: ArrayList<MealMenuBean>)
        fun loadMealGoodsList(data: ArrayList<MealGoodsBean>)
    }

    interface IMealRecordView : IBaseView {
        fun showMealRecord(data: ArrayList<MealRecordBean>)
    }

    interface IMealPlaceAnOrderView : IBaseView {
        fun showPlaceAnOrderResult(data: BaseModel<MealRecordBean>)
    }

    abstract class Presenter(iBaseView: IBaseView) : BasePresenter<IBaseView>(iBaseView) {

        //餐厅列表
        abstract fun loadRestaurantList(page: String)

        //餐厅菜单分类
        abstract fun loadMealMenu(shopId: String)

        //餐厅菜品列表
        abstract fun loadMealGoodsList(page: String, shopId: String, labelId: String)

        //餐厅菜品列表搜索
        abstract fun searchMealGoodsList(shopId: String, searchData: String)

        //订餐记录
        abstract fun loadMealRecord(page: String)

        //下单
        abstract fun commitMealOrder(
            shopId: String, createUserName: String, mobile: String, totalPrice: String, goodsList: ArrayList<MealGoodsBean>
        )

    }
}