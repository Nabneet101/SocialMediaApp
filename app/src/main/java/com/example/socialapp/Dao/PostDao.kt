package com.example.socialapp.Dao

import com.example.socialapp.models.User
import com.example.socialapp.models.post
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {
    val db = FirebaseFirestore.getInstance()
    val postcollection = db.collection("posts")
    val auth = Firebase.auth
    fun addpost(text: String) {
        GlobalScope.launch {
            val currentuserid = auth.currentUser!!.uid
            val usedao = Userdao()
            val user = usedao.getUserById(currentuserid).await().toObject(User::class.java)!!
            val currenttime = System.currentTimeMillis()
            val post = post(text, user, currenttime)
            postcollection.document().set(post)
        }
    }
        fun getPostById(postId: String): Task<DocumentSnapshot> {
            return postcollection.document(postId).get()
        }

        fun updateLikes(postId: String) {
            GlobalScope.launch {
                val currentUserId = auth.currentUser!!.uid
                val post = getPostById(postId).await().toObject(post::class.java)!!
                val isLiked = post.likedBy.contains(currentUserId)

                if (isLiked) {
                    post.likedBy.remove(currentUserId)
                } else {
                    post.likedBy.add(currentUserId)
                }
                postcollection.document(postId).set(post)
            }

        }
    }
