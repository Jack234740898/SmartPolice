package com.zhkj.smartpolice.mine.bean


class UserBean {

    var authorities: String? = null
    var avatar: String? = null
    var createTime: String? = null
    var deptId: String? = null
    var deptName: String? = null
    var email: String? = null
    var isDisables: String? = null
    var isRegular: String? = null
    var loginId: String? = null
    var mobile: String? = null
    var nickName: String? = null
    var policeId: String? = null
    var policeNumber: String? = null
    var position: String? = null
    var roleIdList: List<Int>? = null
    var salt: String? = null
    var sex: String? = null
    var sign: String? = null
    var status: String? = null
    var userId: String? = null
    var userName: String? = null

    override fun toString(): String {
        return "UserBean(authorities=$authorities, avatar=$avatar, createTime=$createTime, deptId=$deptId, deptName=$deptName, email=$email, isDisables=$isDisables, isRegular=$isRegular, loginId=$loginId, mobile=$mobile, nickName=$nickName, policeId=$policeId, policeNumber=$policeNumber, position=$position, roleIdList=$roleIdList, salt=$salt, sex=$sex, sign=$sign, status=$status, userId=$userId, userName=$userName)"
    }
}