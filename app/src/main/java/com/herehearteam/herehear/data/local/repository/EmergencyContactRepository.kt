package com.herehearteam.herehear.data.local.repository

import android.util.Log
import com.herehearteam.herehear.data.local.dao.EmergencyDao
import com.herehearteam.herehear.data.local.entity.EmergencyEntity
import com.herehearteam.herehear.data.remote.api.ApiConfig

//class EmergencyContactRepository(private val emergencyDao: EmergencyDao) {
//    suspend fun insertEmergencyContact(emergency: EmergencyEntity) {
//        emergencyDao.insertEmergency(emergency)
//    }
//
//    suspend fun updateEmergencyContact(contact: EmergencyEntity) {
//        emergencyDao.updateEmergency(contact)
//    }
//
//    suspend fun getEmergencyContact(userId: String): EmergencyEntity? {
//        return emergencyDao.getEmergencyByUserId(userId)
//    }
//}

class EmergencyContactRepository(private val emergencyDao: EmergencyDao) {
    suspend fun insertOrUpdateEmergencyContact(emergency: EmergencyEntity) {
        // Cari emergency contact yang sudah ada untuk user ini
        val existingContact = emergencyDao.getEmergencyByUserId(emergency.userId)

        if (existingContact != null) {
            // Jika sudah ada, update contact yang sudah ada
            val updatedContact = existingContact.copy(
                namaKontak = emergency.namaKontak,
                nomorTelepon = emergency.nomorTelepon,
                hubungan = emergency.hubungan
            )
            emergencyDao.updateEmergency(updatedContact)
        } else {
            // Jika belum ada, insert sebagai contact baru
            emergencyDao.insertEmergency(emergency)
        }
    }

    suspend fun fetchAndSaveEmergencyContact(userId: String) {
        try {
            val localContact = getEmergencyContact(userId)

            if (localContact == null) {
                val apiService = ApiConfig.getApiService()
                val existingContacts = apiService.getAllEmergencyContacts()

                val userEmergencyContacts = existingContacts.data.filter { contact ->
                    contact.user_id == userId
                }

                if (userEmergencyContacts.isNotEmpty()) {
                    val apiContact = userEmergencyContacts.first()
                    val emergencyEntity = EmergencyEntity(
                        userId = userId,
                        namaKontak = apiContact.emergency_name,
                        nomorTelepon = apiContact.emergency_number,
                        hubungan = apiContact.relationship
                    )

                    insertOrUpdateEmergencyContact(emergencyEntity)
                }
            }
        } catch (e: Exception) {
            Log.e("EmergencyContactRepository", "Error fetching emergency contact from API", e)
        }
    }

    suspend fun updateEmergencyContact(contact: EmergencyEntity) {
        emergencyDao.updateEmergency(contact)
    }

    suspend fun getEmergencyContact(userId: String): EmergencyEntity? {
        return emergencyDao.getEmergencyByUserId(userId)
    }
}