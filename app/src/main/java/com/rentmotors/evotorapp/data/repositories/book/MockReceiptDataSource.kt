package com.rentmotors.evotorapp.data.repositories.book

import android.content.Context
import com.rentmotors.evotorapp.domain.common.Result
import com.rentmotors.evotorapp.domain.entities.BookReceipt
import kotlinx.coroutines.delay

class MockReceiptDataSource : ReceiptDataSource {
    override suspend fun getReceipt(resNumber: String): Result<BookReceipt> {
        delay(2000)
        return Result.Success(
            BookReceipt(
                "143",
                "32751",
                "137649",
                "89189",
                "add 1 days (26.10 - 27.10)",
                "anglileon021@gmail.com",
                "",
                "osn",
                "https://rentmotors.ru/appconfig/rmadmin/php/tks/notify/",
                listOf(
                    BookReceipt.Item("Аренда ТС. Продление", 2.0, 1.00.toInt(), 2.0, "vat20"),
                    BookReceipt.Item(
                        "Полное страховое покрытие. Продление",
                        1.0,
                        1.00.toInt(),
                        1.0,
                        "vat20"
                    )
                )
            )
        )
    }
}

/* "TerminalKey": "1626078414401",
    "Amount": "300000",
    "OrderId": "143",
    "Description": "Аренда ТС",
    "Language": "ru",
    "NotificationURL": "https://rentmotors.ru/appconfig/rmadmin/php/tks/notify/",
    "RedirectDueDate":"2021-11-30T00:00:00+03:00",
    "DATA": {
        "Phone": "",
        "Email": "efrembus95@mail.ru",
        "ApplePayWeb": "true",
        "aprok_id": "32751",
        "res_id": "137649",
        "offer_id": "89189",
        "descr": "fine"
    },
    "Receipt": {
        "Email": "efrembus95@mail.ru",
        "Phone": "",
        "Taxation": "osn",
        "Items": [
            {
                "Name": "Компенсация штрафа за нарушение ПДД",
                "Price": 300000,
                "Quantity": 1.00,
                "Amount": 300000,
                "Tax": "none",
                "PaymentMethod": "full_prepayment",
                "PaymentObject": "service"
            }
        ]
    }
}
{
    "TerminalKey": "1626078414401",
    "Amount": "220000",
    "OrderId": "139",
    "Description": "Аренда ТС",
    "Language": "ru",
    "NotificationURL": "https://rentmotors.ru/appconfig/rmadmin/php/tks/notify/",
    "RedirectDueDate":"2021-11-30T00:00:00+03:00",
    "DATA": {
        "Phone": "",
        "Email": "evgenia01@ngs.ru",
        "ApplePayWeb": "true",
        "aprok_id": "33284",
        "res_id": "152314",
        "offer_id": "97760",
        "descr": "add 1 days (26.10 - 27.10)"
    },
    "Receipt": {
        "Email": "evgenia01@ngs.ru",
        "Phone": "",
        "Taxation": "osn",
        "Items": [
            {
                "Name": "Аренда ТС. Продление",
                "Price": 220000,
                "Quantity": 2.00,
                "Amount": 440000,
                "Tax": "vat20",
                "PaymentMethod": "full_prepayment",
                "PaymentObject": "service"
            },
            {
                "Name": "Аэропортовый сбор.",
                "Price": 220000,
                "Quantity": 1.00,
                "Amount": 220000,
                "Tax": "vat20",
                "PaymentMethod": "full_prepayment",
                "PaymentObject": "service"
            }
        ]
    }
}
*/