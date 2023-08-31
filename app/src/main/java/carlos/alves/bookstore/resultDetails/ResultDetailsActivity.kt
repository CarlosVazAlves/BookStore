package carlos.alves.bookstore.resultDetails

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import carlos.alves.bookstore.IntentConstants.Companion.AUTHOR
import carlos.alves.bookstore.IntentConstants.Companion.BUY_LINK
import carlos.alves.bookstore.IntentConstants.Companion.DESCRIPTION
import carlos.alves.bookstore.IntentConstants.Companion.ID
import carlos.alves.bookstore.IntentConstants.Companion.THUMBNAIL
import carlos.alves.bookstore.IntentConstants.Companion.TITLE
import carlos.alves.bookstore.R
import carlos.alves.bookstore.databinding.ActivityResultDetailsBinding
import coil.load
import androidx.activity.viewModels

class ResultDetailsActivity : AppCompatActivity() {

    private val binding: ActivityResultDetailsBinding by lazy { ActivityResultDetailsBinding.inflate(layoutInflater) }
    private val viewModel: ResultDetailsViewModel by viewModels { ResultDetailsViewModel.factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val notAvailableString = resources.getString(R.string.not_available)
        val id = intent.getStringExtra(ID)
        val thumbnailUrl = intent.getStringExtra(THUMBNAIL)

        binding.titleText.text = intent.getStringExtra(TITLE).toString()
        binding.authorText.text = intent.getStringExtra(AUTHOR).toString()
        binding.descriptionText.text = intent.getStringExtra(DESCRIPTION).toString()
        binding.buyLinkText.text = intent.getStringExtra(Html.fromHtml(BUY_LINK, Html.FROM_HTML_MODE_COMPACT).toString())

        if (thumbnailUrl != null) {
            binding.detailThumbnail.load(thumbnailUrl)
        }

        val isFavorite = viewModel.checkIfIsFavorite(id!!)

        binding.favoriteSwitch.isChecked = isFavorite

        binding.buyLinkText.setOnClickListener {
            val url = binding.buyLinkText.text.toString()
            if (url != notAvailableString) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        }

        binding.favoriteSwitch.setOnClickListener {
            if (!binding.favoriteSwitch.isChecked) {
                viewModel.removeFromFavorites(id)
            }
            else {
                viewModel.addToFavorites(id)
            }
        }

        binding.detailBackButton.setOnClickListener {
            finish()
        }
    }
}