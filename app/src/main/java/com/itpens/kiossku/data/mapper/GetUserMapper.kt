package com.itpens.kiossku.data.mapper

import com.itpens.kiossku.data.model.user.GetUserDto
import com.itpens.kiossku.domain.mapper.Mapper
import com.itpens.kiossku.domain.model.UserData
import javax.inject.Inject

class GetUserMapper @Inject constructor() : Mapper<GetUserDto, UserData> {
    override fun from(data: GetUserDto) =
        UserData(
            namaLengkap = data.data!!.fullname!!,
            email = data.data.email!!,
            nomorTelepon = data.data.telepon!!
        )
}