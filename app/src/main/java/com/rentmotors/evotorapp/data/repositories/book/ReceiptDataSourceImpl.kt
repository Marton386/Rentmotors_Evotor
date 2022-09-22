package com.rentmotors.evotorapp.data.repositories.book

import com.rentmotors.evotorapp.R
import com.rentmotors.evotorapp.data.api.BookApi
import com.rentmotors.evotorapp.data.api.ErrorCodes
import com.rentmotors.evotorapp.data.api.entities.toReceipt
import com.rentmotors.evotorapp.domain.common.Result
import com.rentmotors.evotorapp.domain.entities.BookReceipt
import com.rentmotors.evotorapp.presentation.utils.StringHelper
import retrofit2.HttpException
import java.net.UnknownHostException

class ReceiptDataSourceImpl(
    private val bookApi: BookApi,
    private val stringHelper: StringHelper
) : ReceiptDataSource {
    override suspend fun getReceipt(resNumber: String): Result<BookReceipt> =
        try {
            val response = bookApi.getBook(resNumber)
            if (response.isSuccessful) {
                Result.Success(response.body()!!.toReceipt())
            } else {
                throw HttpException(response)
            }
        } catch (e: HttpException) {
            val errorRes = when (e.code()) {
                ErrorCodes.WRONG_TOKEN -> R.string.authorization_error
                ErrorCodes.NOT_ALLOWED -> R.string.contract_not_allowed_for_this_terminal
                ErrorCodes.NOT_FOUND -> R.string.contract_not_found
                else -> 0
            }

            val message = if (errorRes > 0) stringHelper.getString(errorRes)
            else "${e.code()} ${e.message()}"

            Result.Error(message)
        } catch (e: UnknownHostException) {
            Result.Error(stringHelper.getString(R.string.check_the_internet_connection))
        } catch (e: Exception) {
            Result.Error(e.message)
        }

    override suspend fun getRefund(resNumber: String): Result<BookReceipt> =
        try {
            val response = bookApi.getRefundBook(resNumber)
            if (response.isSuccessful) {
                Result.Success(response.body()!!.toReceipt())
            } else {
                throw HttpException(response)
            }
        } catch (e: HttpException) {
            val errorRes = when (e.code()) {
                ErrorCodes.WRONG_TOKEN -> R.string.authorization_error
                ErrorCodes.NOT_ALLOWED -> R.string.contract_not_allowed_for_this_terminal
                ErrorCodes.NOT_FOUND -> R.string.contract_not_found
                else -> 0
            }

            val message = if (errorRes > 0) stringHelper.getString(errorRes)
            else "${e.code()} ${e.message()}"

            Result.Error(message)
        } catch (e: UnknownHostException) {
            Result.Error(stringHelper.getString(R.string.check_the_internet_connection))
        } catch (e: Exception) {
            Result.Error(e.message)
        }
}