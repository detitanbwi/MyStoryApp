package com.example.myapplication

import com.example.myapplication.data.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "https://$i",
                "timeCreated $i",
                "name $i",
                "description $i",
                "114.378595",
                "$i",
                "-8.209998"

            )
            items.add(story)
        }
        return items
    }
}