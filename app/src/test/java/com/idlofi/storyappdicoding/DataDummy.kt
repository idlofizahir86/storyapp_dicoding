package com.idlofi.storyappdicoding

import com.idlofi.storyappdicoding.network.StoryResponItem
import com.idlofi.storyappdicoding.service.AddNewStoryResponse
import com.idlofi.storyappdicoding.service.GetAllStoryResponse
import com.idlofi.storyappdicoding.service.LoginResponse
import com.idlofi.storyappdicoding.service.LoginResult
import com.idlofi.storyappdicoding.service.RegisterResponse

object DataDummy {

    fun generateDummyAllStories(): List<StoryResponItem>{
        val newList: MutableList<StoryResponItem> = arrayListOf()
        for (i in 0..100){
            val story = StoryResponItem(
                "1",
                "zahir",
                "descr",
                "nope",
                "2024",
                -10.0,
                8.0
            )
            newList.add(story)
        }
        return newList
    }

    fun generateDummyLoginResult(): LoginResult {
        return LoginResult(
            "user",
            "1234",
            "token"
        )
    }
    fun generateDummyLoginResponseSuccess(): LoginResponse {
        return LoginResponse(
            loginResult = generateDummyLoginResult(),
            error = false,
            message = "Success"
        )
    }

    fun generateDummyAllStoriesWithLoc(): MutableList<StoryResponItem>{
        val newList: MutableList<StoryResponItem> = arrayListOf()
        for (i in 0..10){
            val story = StoryResponItem(
                "1",
                "idlofi",
                "test",
                "nope",
                "2024",
                -8.0,
                3.0
            )
            newList.add(story)
        }
        return newList
    }

    fun generateDummyStoriesWithLoc(): GetAllStoryResponse {
        return GetAllStoryResponse(
            listStory = generateDummyAllStoriesWithLoc(),
            error = false,
            message = "Success"
        )
    }

    fun generateDummySignUpResponseSuccess(): RegisterResponse {
        return RegisterResponse(
            false,
            "success"
        )
    }

    fun generateDummyAddNewStoriesResponseSuccess(): AddNewStoryResponse {
        return AddNewStoryResponse(
            false,
            "success"
        )
    }





}