package com.amk.market.model.repository.comment

import com.amk.market.model.data.Comment

interface CommentRepository {

    suspend fun getAllComments(productId: String): List<Comment>

    suspend fun addNewComment(productId: String, comment: String, isSuccess: (String) -> Unit)
}