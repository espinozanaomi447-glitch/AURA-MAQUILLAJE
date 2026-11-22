package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.atelierDataStore: DataStore<Preferences> by preferencesDataStore(name = "aura_atelier_prefs")

class AtelierPreferences(private val context: Context) {

    private val completedLessonsKey = stringSetPreferencesKey("completed_lessons")
    private val showBrushGuidesKey = booleanPreferencesKey("show_brush_guides")
    private val lastModelIdKey = stringPreferencesKey("last_model_id")
    private val hasConfiguredFaceSkinKey = booleanPreferencesKey("has_configured_face_skin")

    val completedLessons: Flow<Set<String>> = context.atelierDataStore.data.map { prefs ->
        prefs[completedLessonsKey] ?: emptySet()
    }

    val showBrushGuides: Flow<Boolean> = context.atelierDataStore.data.map { prefs ->
        prefs[showBrushGuidesKey] ?: true
    }

    val lastModelId: Flow<String> = context.atelierDataStore.data.map { prefs ->
        prefs[lastModelIdKey] ?: "sofia"
    }

    val hasConfiguredFaceSkin: Flow<Boolean> = context.atelierDataStore.data.map { prefs ->
        prefs[hasConfiguredFaceSkinKey] ?: false
    }

    suspend fun markLessonCompleted(lessonId: String) {
        context.atelierDataStore.edit { prefs ->
            val current = prefs[completedLessonsKey] ?: emptySet()
            prefs[completedLessonsKey] = current + lessonId
        }
    }

    suspend fun setShowBrushGuides(show: Boolean) {
        context.atelierDataStore.edit { prefs ->
            prefs[showBrushGuidesKey] = show
        }
    }

    suspend fun setLastModelId(modelId: String) {
        context.atelierDataStore.edit { prefs ->
            prefs[lastModelIdKey] = modelId
        }
    }

    suspend fun setHasConfiguredFaceSkin(done: Boolean) {
        context.atelierDataStore.edit { prefs ->
            prefs[hasConfiguredFaceSkinKey] = done
        }
    }
}
