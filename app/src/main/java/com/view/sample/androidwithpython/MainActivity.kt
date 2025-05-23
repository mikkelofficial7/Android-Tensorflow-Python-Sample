package com.view.sample.androidwithpython

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }

        val tokenizerJson = loadTokenizerJson(this)
        val py = Python.getInstance()
        val pyObj = py.getModule("str_tokenize")
        val result = pyObj.callAttr("tokenize_text", "halo apa kabar?", tokenizerJson)

        runTFLite(result)
    }

    private fun loadTokenizerJson(context: Context): String {
        val inputStream = context.assets.open("model_tokenizer.json")
        return inputStream.bufferedReader().use { it.readText() }
    }

    private fun runTFLite(tokenized: PyObject) {
        val tfliteModel = loadMappedFile(this, "betawi_word_model.tflite")
        val interpreter = Interpreter(tfliteModel)

        val tokenList = tokenized.asList().map { it.toFloat() }.toFloatArray()
        val input = Array(1) { tokenList }  // Shape: [1, 50] as Float

        val output = Array(1) { FloatArray(1) }  // Assuming binary classification with sigmoid
        interpreter.run(input, output)

        val prediction = output[0][0]
        val label = if (prediction > 0.55555f) "Positive" else "Negative"

        Log.d("Prediction", "Score: $prediction, Sentiment: $label")
    }

    private fun loadMappedFile(context: Context, filename: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(filename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}