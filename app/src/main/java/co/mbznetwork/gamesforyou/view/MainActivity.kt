package co.mbznetwork.gamesforyou.view

import co.mbznetwork.gamesforyou.R
import co.mbznetwork.gamesforyou.databinding.ActivityMainBinding
import co.mbznetwork.gamesforyou.view.fragment.FavoriteGameFragment
import co.mbznetwork.gamesforyou.view.fragment.GameListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId: Int = R.layout.activity_main

    override fun initView() {
        binding.bnvMain.setOnItemSelectedListener { item ->
            supportFragmentManager.beginTransaction().replace(
                R.id.fc_main,
                when(item.itemId) {
                    R.id.menu_favorite -> FavoriteGameFragment()
                    else -> GameListFragment()
                }
            ).commit()
            true
        }
    }
}