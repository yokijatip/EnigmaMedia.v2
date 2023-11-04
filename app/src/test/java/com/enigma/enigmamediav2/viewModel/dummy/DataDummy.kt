package com.enigma.enigmamediav2.viewModel.dummy

import com.enigma.enigmamediav2.data.remote.response.ListStoryItem
import com.enigma.enigmamediav2.data.remote.response.StoryResponse

object DataDummy {

    fun generateDummyStoryResponse(): StoryResponse {
        val listStory = ArrayList<ListStoryItem>()
        for (i in 1..20) {
            val story = ListStoryItem(
                createdAt = "2023-02-09 06:58:13",
                description = "Description $i",
                id = "id_$i",
                lat = i.toDouble() * 10,
                lon = i.toDouble() * 10,
                name = "Name $i",
                photoUrl = "https://source.unsplash.com/gySMaocSdqs"
            )
            listStory.add(story)
        }

        return StoryResponse(
            error = false,
            message = "Stories fetched successfully",
            listStory = listStory
        )
    }

}