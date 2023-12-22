package com.app.skillcinema.utils

object ApiKey {
    private var index = 0
    private var currentKey = "31d58f2d-cdc5-4951-8a53-ae4c542bbd27"

    fun changeKey() {
        val listKey = listOf(
            "6a551594-6f41-4ead-9608-a0799c2f2dff",
            "f2fc5a4d-0e81-46bb-b306-cc6a5b23b214",
            "589158dd-ec0e-4efd-8ba0-0e04f8ce6c3b",
            "31d58f2d-cdc5-4951-8a53-ae4c542bbd27",
            "0cdd318d-09d9-4deb-98ca-80e93e683551",
            "71c5dd47-2ab2-40d4-bb00-4974097af5b6",
            "6a4d4463-9ff1-495b-a1d8-0fdeeafb784c")
        ++index
        if (index >= listKey.size) index = 0
        currentKey = listKey[index]
    }

    fun getKey(): String = currentKey
}