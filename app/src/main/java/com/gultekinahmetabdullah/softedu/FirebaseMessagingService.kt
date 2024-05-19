package com.gultekinahmetabdullah.softedu

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            // Assuming the message contains a "title" and "body" in the data payload
            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]

            // Display a notification
            showNotification(title, body)
        }
    }

    private fun showNotification(title: String?, body: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("DailyNotifications", "Daily Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, "DailyNotifications")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("fcmTokens")
                .whereEqualTo("token", token)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // If the query finds a document with the same token, it means the token already exists in the database
                        if (task.result?.isEmpty == true) {
                            // The token does not exist in the database, so we can add it
                            val data = hashMapOf(
                                "token" to token
                            )

                            db.collection("fcmTokens")
                                    .add(data)
                                    .addOnSuccessListener { documentReference ->
                                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                                        Toast.makeText(baseContext, "Token added to the database", Toast.LENGTH_LONG).show()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w(TAG, "Error adding document", e)
                                        Toast.makeText(baseContext, "Error adding document", Toast.LENGTH_LONG).show()
                                    }
                        } else {
                            Log.d(TAG, "Token already exists in the database")
                            Toast.makeText(baseContext, "Token already exists in the database", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Log.w(TAG, "Error checking for token", task.exception)
                        Toast.makeText(baseContext, "Error checking for token", Toast.LENGTH_LONG).show()
                    }
                }
    }


    companion object {
        fun getMessageToken(context: Context) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (! task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                // Log and toast
                val msg = context.getString(R.string.msg_token_fmt, token)
                Log.d(TAG, msg)
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                sendRegistrationToServer(token)
            })
        }


        private fun sendRegistrationToServer(token: String) {
            val db = FirebaseFirestore.getInstance()

            db.collection("fcmTokens")
                    .whereEqualTo("token", token)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // If the query finds a document with the same token, it means the token already exists in the database
                            if (task.result?.isEmpty == true) {
                                // The token does not exist in the database, so we can add it
                                val data = hashMapOf(
                                    "token" to token
                                )

                                db.collection("fcmTokens")
                                        .add(data)
                                        .addOnSuccessListener { documentReference ->
                                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w(TAG, "Error adding document", e)
                                        }
                            } else {
                                Log.d(TAG, "Token already exists in the database")
                            }
                        } else {
                            Log.w(TAG, "Error checking for token", task.exception)
                        }
                    }
        }
    }
}