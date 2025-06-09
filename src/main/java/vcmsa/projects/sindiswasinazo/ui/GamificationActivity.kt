package vcmsa.projects.sindiswasinazo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import vcmsa.projects.sindiswasinazo.databinding.ActivityGamificationBinding

class GamificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGamificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nothing else needed here since we always show the image and message
    }
}
