package zlc.season.butterflydemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import zlc.season.base.Schemes
import zlc.season.bracer.params
import zlc.season.butterfly.Butterfly
import zlc.season.butterfly.annotation.Agile
import zlc.season.butterflydemo.databinding.ActivityAgileTestBinding

@Agile(Schemes.SCHEME_AGILE_TEST)
class AgileTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAgileTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startActivity.setOnClickListener {
            Butterfly.agile(Schemes.SCHEME_FOO + "?a=1&b=2")
                .params(
                    "intValue" to 1,
                    "booleanValue" to true,
                    "stringValue" to "test value"
                )
                .carry()
        }

        binding.startActivityForResult.setOnClickListener {
            Butterfly.agile(Schemes.SCHEME_FOO_RESULT + "?a=1&b=2")
                .params(
                    "intValue" to 1,
                    "booleanValue" to true,
                    "stringValue" to "test value"
                )
                .carry {
                    val result by it.params<String>()
                    binding.tvResult.text = result
                }
        }

        binding.startAction.setOnClickListener {
            Butterfly.agile(Schemes.SCHEME_ACTION + "?a=1&b=2").carry()
        }

        binding.startFragment.setOnClickListener {
            Butterfly.agile(Schemes.SCHEME_FRAGMENT)
                .carry {
                    val abc by it.params<String>()
                    println(abc)
                    binding.tvResult.text = abc
                }

            binding.startFragment.postDelayed({

            }, 3000)
        }

        binding.startDialogFragment.setOnClickListener {
            Butterfly.agile(Schemes.SCHEME_DIALOG_FRAGMbENT).carry()
        }

        binding.startBottomSheetDialogFragment.setOnClickListener {
            Butterfly.agile(Schemes.SCHEME_BOTTOM_SHEET_DIALOG_FRAGMENT).carry()
        }
    }
}