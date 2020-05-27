package com.rtomyj.three_six_oh.dao

import com.rtomyj.three_six_oh.model.PodcastInfo

interface Dao
{
    fun getPodcastInfo(): PodcastInfo
}