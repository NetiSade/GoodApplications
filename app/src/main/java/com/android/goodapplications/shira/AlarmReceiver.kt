package com.android.goodapplications.shira
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.android.goodapplications.shira.viewModel.ArtworksViewModel
import com.android.goodapplications.shira.viewModel.NotificationsViewModel

class AlarmReceiver: BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent) {
        val artworksViewModel = ArtworksViewModel()
        artworksViewModel.IsThereArtworkForToday {
                if(it != null)
                {
                    val notificationsViewModel = NotificationsViewModel()
                    notificationsViewModel.showNotification(context,it)
                }
            }
        }
    }
