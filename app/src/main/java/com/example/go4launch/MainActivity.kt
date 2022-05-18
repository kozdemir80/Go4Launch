package com.example.go4launch
import adapters.ViewPagerAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.go4launch.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        drawerViews()
        val adapter= ViewPagerAdapter(supportFragmentManager,lifecycle)
        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController=binding.fragmentContainerView.getFragment<NavHostFragment>().navController
        toggle= ActionBarDrawerToggle(this,binding.mainLayout,R.string.open,R.string.close)
        binding.mainLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.bringToFront()
        binding.viewPager.adapter=adapter

       bottomNavigationView.setupWithNavController(navController)
      //  mAuth=Firebase.auth
        //mAuth= FirebaseAuth.getInstance()
        //val user=mAuth.currentUser
       //Handler(Looper.getMainLooper()).postDelayed({
          //  if (user == null){
               // val signInIntent= Intent(this,SignInActivity::class.java)
                //startActivity(signInIntent) }
       // }, 2000)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.map_view ->binding.viewPager.currentItem=0
                R.id.list_view->binding.viewPager.currentItem=1
                R.id.work_mate->binding.viewPager.currentItem=2
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
        return super.onCreateOptionsMenu(menu)
    }
    private fun drawerViews(){
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item1 -> Toast.makeText(applicationContext,"Your Launch",Toast.LENGTH_LONG).show()
                R.id.item2 -> Toast.makeText(applicationContext,"Settings",Toast.LENGTH_LONG).show()
                R.id.item3-> Toast.makeText(applicationContext,"Logout",Toast.LENGTH_LONG).show()
            }
            true
        }
    }
}