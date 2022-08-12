package com.example.go4launch.activities
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.example.go4launch.R
import com.example.go4launch.adapters.ViewPagerAdapter
import com.example.go4launch.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth:FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient= GoogleSignIn.getClient(this,gso)
        drawerViews()
        navHeader()
        val adapter= ViewPagerAdapter(supportFragmentManager,lifecycle)
        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController=binding.fragmentContainerView.getFragment<NavHostFragment>().navController
        toggle= ActionBarDrawerToggle(this,binding.mainLayout, R.string.open, R.string.close)
        binding.mainLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.bringToFront()
        binding.viewPager.adapter=adapter

       bottomNavigationView.setupWithNavController(navController)
       mAuth= Firebase.auth
        mAuth= FirebaseAuth.getInstance()
        val user=mAuth.currentUser



       Handler(Looper.getMainLooper()).postDelayed({
            if (user == null){
                val signInIntent= Intent(this,SignInActivity::class.java)
                startActivity(signInIntent)
            finish()}
        }, 2000)

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.map_view -> binding.viewPager.currentItem = 0
                R.id.list_view -> binding.viewPager.currentItem = 1
                R.id.work_mate -> binding.viewPager.currentItem = 2
            }
            return@setOnItemSelectedListener true
        }


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        when (item.itemId) {
            R.id.app_bar_search -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.search_menu,menu)
        val searchItem=menu.findItem(R.id.app_bar_search)
        val searchView=searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                intent.putExtra("query",query)
                Log.d("myQuery",query.toString())

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }
    private fun drawerViews(){
        binding.navView.setBackgroundResource(com.google.android.libraries.places.R.color.quantum_orange)
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.launch -> Toast.makeText(applicationContext,"Your Launch",Toast.LENGTH_LONG).show()
                R.id.settings -> Toast.makeText(applicationContext,"Settings",Toast.LENGTH_LONG).show()
                R.id.logout-> {
                    googleSignInClient.signOut().addOnCompleteListener {
                        val intent=Intent(this,SignInActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            true
        }
    }

    private fun navHeader(){
        mAuth= Firebase.auth
        mAuth= FirebaseAuth.getInstance()
     val headerView=binding.navView.getHeaderView(0)
       val headerImage= headerView.findViewById<ImageView>(R.id.circleImageView)
       val headerName=headerView.findViewById<TextView>(R.id.name)
       val headerEmail=headerView.findViewById<TextView>(R.id.email)
        headerImage.load(mAuth.currentUser?.photoUrl)
        headerName.text=mAuth.currentUser?.displayName
        headerEmail.text=mAuth.currentUser?.email

    }
}