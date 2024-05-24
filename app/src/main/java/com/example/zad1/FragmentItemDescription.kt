import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.TextView
import com.example.zad1.R
import com.example.zad1.Trail

class TrailDetailFragment : Fragment() {

    private var trail: Trail? = null

    companion object {
        private const val ARG_TRAIL = "trail"

        fun newInstance(trail: Trail?): TrailDetailFragment {
            val fragment = TrailDetailFragment()
            val args = Bundle()
            args.putParcelable(ARG_TRAIL, trail)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            trail = it.getParcelable<Trail>(ARG_TRAIL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleTextView: TextView = view.findViewById(R.id.trailTitle)
        val difficultyTextView: TextView = view.findViewById(R.id.trailDifficulty)
        val lengthTextView: TextView = view.findViewById(R.id.trailLength)
        val descriptionTextView: TextView = view.findViewById(R.id.trailDescription)

        trail?.let {
            titleTextView.text = it.name
            difficultyTextView.text = it.difficulty
            lengthTextView.text = it.length.toString()
            descriptionTextView.text = it.description
        }
    }
}