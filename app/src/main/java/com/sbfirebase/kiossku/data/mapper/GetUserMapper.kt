package com.sbfirebase.kiossku.data.mapper

import com.sbfirebase.kiossku.data.model.user.GetUserDto
import com.sbfirebase.kiossku.domain.mapper.Mapper
import com.sbfirebase.kiossku.domain.model.UserData
import javax.inject.Inject

class GetUserMapper @Inject constructor() : Mapper<GetUserDto, UserData> {
    override fun from(data: GetUserDto) =
        UserData(
            namaLengkap = data.data!!.fullname!!,
            email = data.data.email!!,
            nomorTelepon = data.data.telepon!!
        )
}