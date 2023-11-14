package com.painandpanic.blossombuddy.data.remote.model

data class Gpt3Request(
    val prompt: String,
    val maxTokens: Int
)
data class Gpt3Response(val id: String, val `object`: String, val created: Int, val model: String, val choices: List<Choice>)
data class Choice(
    val text: String,
    val finishReason: String,
    val index: Int
)
