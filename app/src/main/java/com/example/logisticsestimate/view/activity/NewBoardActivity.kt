package com.example.logisticsestimate.view.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Base64.NO_WRAP
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.logisticsestimate.R
import com.example.logisticsestimate.base.App
import com.example.logisticsestimate.data.remote.model.board.BoardDto
import com.example.logisticsestimate.data.remote.model.board.BoardUpdateDto
import com.example.logisticsestimate.databinding.ActivityNewBoardBinding
import com.example.logisticsestimate.data.db.AppDatabase
import com.example.logisticsestimate.data.db.entity.TemporaryEntity
import com.example.logisticsestimate.data.remote.api.BoardRetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.time.LocalDate

/**
 * 새로운 글을 작성, 기존 글 수정, 작성 중 뒤로 이동하면 임시 저장 가능
 */
class NewBoardActivity: AppCompatActivity() {
    private lateinit var binding: ActivityNewBoardBinding

    private var category = 0
    private var boardId: Long = -1
    private var profileImageBase64: String? = null

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
        if(result.resultCode == RESULT_OK) {
            val currentImageUrl = result.data?.data

            val ins: InputStream? = currentImageUrl?.let {
                applicationContext.contentResolver.openInputStream(it)
            }

            val img: Bitmap = BitmapFactory.decodeStream(ins)
            ins?.close()

            val byteArrayOutputStream = ByteArrayOutputStream()

            img.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream)

            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            profileImageBase64 = Base64.encodeToString(byteArray, NO_WRAP)

            binding.activityNewPostIvPreview.setImageURI(currentImageUrl)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.all_ic_arrow_back)

        category = intent.getIntExtra("category", 0)
        boardId = intent.getLongExtra("boardId", -1)
        binding.activityNewPostEtTitle.setText(intent.getStringExtra("title"))
        binding.activityNewPostEtContent.setText(intent.getStringExtra("content"))

        binding.activityNewPostBtnAttach.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)

            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.type = "image/*"
            getResult.launch(intent)
        }

        binding.activityNewPostIvPreview.setOnClickListener {
            if(profileImageBase64 != null) {
                AlertDialog.Builder(this).let {
                    it.setMessage("이미지를 제거하시겠습니까?")
                    it.setPositiveButton("확인") { _, _ ->
                        binding.activityNewPostIvPreview.setImageResource(0)
                        profileImageBase64 = null
                    }
                    it.setNegativeButton("취소") { _, _ -> }
                    it.create()
                    it.show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.new_post_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.create_board -> {
                if(binding.activityNewPostEtTitle.text.toString() == "" || binding.activityNewPostEtContent.text.toString() == "") {
                    Toast.makeText(this@NewBoardActivity, "제목과 내용은 입력해야 합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    val title = binding.activityNewPostEtTitle.text.toString()
                    val content = binding.activityNewPostEtContent.text.toString()
                    val token = App.prefs.getAccessToken()

                    val service = BoardRetrofitClient.getInstance()

                    val call = when(boardId) {
                        (-1).toLong() -> {
                            val boardDto = BoardDto(title, content, category, profileImageBase64)
                            service.putNewBoard(token, boardDto)
                        }
                        else -> {
                            val boardUpdateDto = BoardUpdateDto(title, content, category, boardId)
                            service.updateBoard(token, boardUpdateDto)
                        }
                    }

                    call.enqueue(object: Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if (!response.isSuccessful) {
                                if(response.code() == 401) {
                                    BoardViewActivity.reSignIn(this@NewBoardActivity)
                                } else {
                                    Toast.makeText(this@NewBoardActivity, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@NewBoardActivity, "작성이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                setResult(RESULT_OK)
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText(this@NewBoardActivity, "연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            Log.d("http", t.message!!)
                        }
                    })
                }
            }

            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.activityNewPostEtTitle.text.toString() == "" && binding.activityNewPostEtContent.text.toString() == "") {
            finish()
        } else {
            AlertDialog.Builder(this).let {
                it.setMessage("작성하시던 글이 있습니다.\n글을 저장하시겠습니까?")
                it.setPositiveButton("확인") { _, _ ->
                    val db = AppDatabase.getInstance(this)!!
                    val temporaryPostDao = db.getTemporaryPostDao()

                    Thread {
                        temporaryPostDao.insert(
                            TemporaryEntity(
                                0,
                                category,
                                binding.activityNewPostEtTitle.text.toString(),
                                binding.activityNewPostEtContent.text.toString(),
                                LocalDate.now().toString()
                            )
                        )
                    }.start()

                    finish()
                }
                it.setNegativeButton("취소") { _, _ ->
                    finish()
                }
                it.create()
                it.show()
            }
        }
    }
}