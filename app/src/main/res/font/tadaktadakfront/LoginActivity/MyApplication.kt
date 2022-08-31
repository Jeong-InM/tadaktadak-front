package ds.project.tadaktadakfront.LoginActivity

import android.app.Application
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "{eed822d5658b122d1e6f957d554e620e}")

    }
}
