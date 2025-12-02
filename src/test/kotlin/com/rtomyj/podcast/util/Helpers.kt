package com.rtomyj.podcast.util

import tools.jackson.databind.json.JsonMapper

class Helpers {
    companion object {
        private fun configureMapper(): JsonMapper {
            val mapper = JsonMapper()
            return mapper
        }

        val mapper = configureMapper()
    }
}