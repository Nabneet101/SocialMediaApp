package com.example.socialapp
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialapp.Dao.PostDao
import com.example.socialapp.models.post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), IPostAdapter {

    private lateinit var postDao: PostDao
    private lateinit var adapter: PostAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var mgoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        supportActionBar?.setTitle("Gossip")
        auth = FirebaseAuth.getInstance()

        fab.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }

        setUpRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
     val inflater: Unit =menuInflater.inflate(R.menu.menuhead,menu)

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                signOut()
                true
            }
            R.id.Github -> {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Nabneet101"))
                startActivity(browserIntent)
                true
            }

            else ->    return super.onOptionsItemSelected(item)
        }

    }
    private fun signOut() {

                 Toast.makeText (this, "Logged Out", Toast.LENGTH_LONG).show()
                  auth.signOut()
                val intent=Intent(this,SignInActivity::class.java)
                startActivity(intent)
                finish()



    }

    private fun setUpRecyclerView() {
        postDao = PostDao()
        val postsCollections = postDao.postcollection
        val query = postsCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions =
            FirestoreRecyclerOptions.Builder<post>().setQuery(query, post::class.java).build()

        adapter = PostAdapter(recyclerViewOptions, this)

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)

    }
}

