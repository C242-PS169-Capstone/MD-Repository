package com.herehearteam.herehear.data.local.repository

import com.herehearteam.herehear.data.local.dao.EmergencyDao
import com.herehearteam.herehear.data.local.entity.EmergencyEntity

class EmergencyContactRepository(private val emergencyDao: EmergencyDao) {
    suspend fun insertEmergencyContact(emergency: EmergencyEntity) {
        emergencyDao.insertEmergency(emergency)
    }

    suspend fun updateEmergencyContact(emergency: EmergencyEntity) {
        emergencyDao.updateEmergency(emergency)
    }

    suspend fun getEmergencyContact(userId: String): EmergencyEntity? {
        return emergencyDao.getEmergency()
    }
}