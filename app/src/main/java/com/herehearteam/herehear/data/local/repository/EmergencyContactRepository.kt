package com.herehearteam.herehear.data.local.repository

import com.herehearteam.herehear.data.local.dao.EmergencyDao
import com.herehearteam.herehear.data.local.entity.EmergencyEntity

class EmergencyContactRepository(private val emergencyDao: EmergencyDao) {
    suspend fun insertEmergencyContact(emergency: EmergencyEntity) {
        emergencyDao.insertEmergency(emergency)
    }

    suspend fun updateEmergencyContact(contact: EmergencyEntity) {
        emergencyDao.updateEmergency(contact)
    }

    suspend fun getEmergencyContact(userId: String): EmergencyEntity? {
        return emergencyDao.getEmergencyByUserId(userId)
    }
}