package ir.jatlin.topmarket.service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct

const val newestProductsNotificationId = 0
private const val newestProductsChannelId = "syncNewestProductsChannelId"

fun Context.sendNewestProductsNotification(
    products: List<NetworkProduct>
) {
    createChannel(
        id = newestProductsChannelId,
        name = R.string.sync_newest_proudcts_channel_name,
        description = R.string.sync_newest_proudcts_channel_description
    )

    val notification = NotificationCompat.Builder(
        this,
        newestProductsChannelId
    )
        .setSmallIcon(R.drawable.ic_digi_smile_24)
        .setContentTitle(getString(R.string.newest_products_notification_title))
        .setContentText(getString(R.string.newest_products_notification_content, products.size))
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    with(NotificationManagerCompat.from(this)) {
        notify(newestProductsNotificationId, notification)
    }
}

fun Context.createChannel(
    id: String,
    @StringRes name: Int,
    @StringRes description: Int
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            id,
            getString(name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = getString(description)

        val notificationManager: NotificationManager? =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        notificationManager?.createNotificationChannel(channel)
    }
}
