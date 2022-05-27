package adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.go4launch.fragments.ListViewFragment
import com.example.go4launch.fragments.MapViewFragment
import com.example.go4launch.fragments.WorkMateFragment

class ViewPagerAdapter(FragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(FragmentManager,lifecycle){
    override fun getItemCount(): Int {
        return 3
    }
    //viewPager for fragments
    override fun createFragment(position: Int): Fragment {
        return  when(position){
            0->{
                MapViewFragment()
            }
            1->{
                ListViewFragment()
            }
            2-> {
                WorkMateFragment()
            }

            else->{
                Fragment()
            }
        }
    }
}