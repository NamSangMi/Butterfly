package zlc.season.bar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import zlc.season.bar.databinding.ActivityAgileTestBinding
import zlc.season.base.Schemes
import zlc.season.butterfly.Butterfly
import zlc.season.butterfly.Butterfly.carry
import zlc.season.butterfly.Butterfly.with
import zlc.season.butterfly.annotation.Agile

@Agile(Schemes.SCHEME_AGILE_TEST)
class AgileTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAgileTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startActivity.setOnClickListener {
            Butterfly.agile(Schemes.SCHEME_FOO + "?a=1&b=2")
                .with("intValue" to 1)
                .with("booleanValue" to true)
                .with("stringValue" to "test value")
                .carry()
        }

        binding.startActivityForResult.setOnClickListener {
            Butterfly.agile(Schemes.SCHEME_FOO_RESULT + "?a=1&b=2")
                .with("intValue" to 1)
                .with("booleanValue" to true)
                .with("stringValue" to "test value")
                .carry {
                    if (it.isSuccess) {
                        val data = it.getOrThrow()
                        val result = data.getStringExtra("result")
                        binding.tvResult.text = result
                    }
                }
        }

        binding.startAction.setOnClickListener {
            Butterfly.agile(Schemes.SCHEME_ACTION + "?a=1&b=2").carry()
        }
    }
}