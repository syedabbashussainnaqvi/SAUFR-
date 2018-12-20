package bscs.com.projecttest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.*
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import dmax.dialog.SpotsDialog
import com.microsoft.projectoxford.face.*;
import com.microsoft.projectoxford.face.contract.*;
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*
import com.microsoft.projectoxford.face.contract.FaceRectangle


class MainActivity : AppCompatActivity() {
    companion object {
        private val API_KEY = "73a339d4f6484ff1aa6a2e9fc14d89a2"
        private val API_LINE = "https://eastasia.api.cognitive.microsoft.com/face/v1.0"
        private val personGroupId ="bscs6a"
    }
    val faceServiceClient = FaceServiceRestClient(API_LINE, API_KEY)
    var faceDetected:Array<Face>? = null;
    var bitmap: Bitmap?=null;

    inner class detectTask:AsyncTask<InputStream,String,Array<Face>>(){
        val alertDialog = SpotsDialog.Builder().setContext(this@MainActivity)
                .setCancelable(false)
                .build()

        override fun onPreExecute() {
            alertDialog.show();
        }

        override fun onProgressUpdate(vararg values: String?) {
            alertDialog.setMessage(values[0])
        }

        override fun doInBackground(vararg params: InputStream?): Array<Face>? {
            try {
                publishProgress("Detecting")
                val result = faceServiceClient.detect(params[0],
                        true,
                        false,
                        null)
                if (result == null)
                {
                    Log.d("ERROR", "Detect finished. Nothing detected")
                    return null
                }
                else
                {
                    return result
                }
            }catch(ex:Exception)
            {
                Log.d("ERROR", ex.message)
                return null
            }
        }

        override fun onPostExecute(result: Array<Face>?) {
            if(alertDialog != null && alertDialog.isShowing)
                alertDialog.dismiss()
            if(result != null)
            {
                imageView.setImageBitmap(drawFaceRectangleOnBitmap(bitmap,result, Color.YELLOW))
                faceDetected = result
                identifyButton.isEnabled = true
            }
        }

        private fun drawFaceRectangleOnBitmap(mBitmap: Bitmap?, facesDetected: Array<Face>, color: Int): Bitmap? {
            val bitmap = mBitmap!!.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(bitmap)
            //Rectangle
            val paint = Paint()
            paint.setAntiAlias(true)
            paint.setStyle(Paint.Style.STROKE)
            paint.setColor(color)
            paint.setStrokeWidth(12F)
            if (facesDetected != null) {
                for (face in facesDetected) {
                    val faceRectangle = face.faceRectangle
                    canvas.drawRect(faceRectangle.left.toFloat(),
                            faceRectangle.top.toFloat(),
                            (faceRectangle.left + faceRectangle.width).toFloat(),
                            (faceRectangle.top + faceRectangle.height).toFloat(),
                            paint)
                }
            }
            return bitmap
        }

    }

    inner class identificationTask:AsyncTask<UUID,String,Array<IdentifyResult>>(){
        val alertDialog = SpotsDialog.Builder().setContext(this@MainActivity)
                .setCancelable(false)
                .build()

        override fun onPreExecute() {
            alertDialog.show();
        }

        override fun onProgressUpdate(vararg values: String?) {
            alertDialog.setMessage(values[0])
        }

        override fun doInBackground(vararg params: UUID?): Array<IdentifyResult>? {
            try {
                publishProgress("Getting person group status")
                val trainingStatus = faceServiceClient.getPersonGroupTrainingStatus(personGroupId)
                if(trainingStatus.status != TrainingStatus.Status.Succeeded)
                {
                    return null
                }
                else{
                    publishProgress("Identifying")
                    return faceServiceClient.identity(personGroupId,params,40)
                }
            }catch(ex:Exception) {
                Log.d("ERROR", ex.message)
                return null
            }
        }

        override fun onPostExecute(result: Array<IdentifyResult>?) {
            alertDialog.dismiss()
            for(identifyResult in result!!)
                personDetectionTask().execute(identifyResult.candidates[0].personId)

        }
    }

    inner class personDetectionTask:AsyncTask<UUID,String, Person>(){
        val alertDialog = SpotsDialog.Builder().setContext(this@MainActivity)
                .setCancelable(false)
                .build()

        override fun onPreExecute() {
            alertDialog.show();
        }

        override fun onProgressUpdate(vararg values: String?) {
            alertDialog.setMessage(values[0])
        }

        override fun doInBackground(vararg params: UUID?): Person? {
            alertDialog.dismiss()
            try{
                publishProgress("Getting Person Group Status")
                return faceServiceClient.getPerson(personGroupId,params[0])
            }catch(ex:Exception){
                Log.d("Error", ex.message)
                return null
            }
        }

        override fun onPostExecute(person: Person?) {
            alertDialog.dismiss()
            Log.d("chicken",person!!.name)
            //imageView.setImageBitmap(drawFaceRectangleWithTextOnBitmap(bitmap,
            //      faceDetected,
            //    person!!.name,
            //  Color.YELLOW,
            // 100))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bitmap = BitmapFactory.decodeResource(resources,R.drawable.rizwan)
        imageView.setImageBitmap(bitmap)

        detectButton.setOnClickListener{
            val outputStream = ByteArrayOutputStream()
            bitmap!!.compress(Bitmap.CompressFormat.JPEG,100,outputStream)
            val inputStream = ByteArrayInputStream(outputStream.toByteArray())
            detectTask().execute(inputStream)
        }

        identifyButton.setOnClickListener{
            if(faceDetected!!.size > 0){
                val faceIds = arrayOfNulls<UUID>(faceDetected!!.size)
                for(i in faceDetected!!.indices)
                    faceIds[i] = faceDetected!![i].faceId
                identificationTask().execute(*faceIds)
            }
        }
    }
}
