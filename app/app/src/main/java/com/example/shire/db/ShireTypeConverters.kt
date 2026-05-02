package com.example.shire.db

import androidx.room.TypeConverter
import com.example.shire.domain.model.CurrencyOption
import com.example.shire.domain.model.DateFormatOption
import com.example.shire.domain.model.LanguageOption
import com.example.shire.domain.model.TextSizeOption
import com.example.shire.domain.model.ThemeOption
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date
import java.util.LinkedList

class ShireTypeConverters {

    @TypeConverter
    fun fromDate(value: Date?): Long? = value?.time

    @TypeConverter
    fun toDate(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? = value?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    @TypeConverter
    fun fromLocalTime(value: LocalTime?): String? = value?.toString()

    @TypeConverter
    fun toLocalTime(value: String?): LocalTime? = value?.let { LocalTime.parse(it) }

    @TypeConverter
    fun fromStringList(value: List<String>?): String = DbCodec.encodeStringList(value ?: emptyList())

    @TypeConverter
    fun toStringList(value: String?): List<String> = DbCodec.decodeStringList(value.orEmpty())

    @TypeConverter
    fun fromLinkedStringList(value: LinkedList<String>?): String = DbCodec.encodeStringList(value?.toList() ?: emptyList())

    @TypeConverter
    fun toLinkedStringList(value: String?): LinkedList<String> = LinkedList(DbCodec.decodeStringList(value.orEmpty()))

    @TypeConverter
    fun fromIntMap(value: HashMap<Int, Int>?): String = DbCodec.encodeIntMap(value ?: hashMapOf())

    @TypeConverter
    fun toIntMap(value: String?): HashMap<Int, Int> = DbCodec.decodeIntMap(value.orEmpty())

    @TypeConverter
    fun fromIntListMap(value: HashMap<Int, MutableList<Int>>?): String =
        DbCodec.encodeIntListMap(value ?: hashMapOf())

    @TypeConverter
    fun toIntListMap(value: String?): HashMap<Int, MutableList<Int>> = DbCodec.decodeIntListMap(value.orEmpty())

    @TypeConverter
    fun fromLanguage(value: LanguageOption): String = value.name

    @TypeConverter
    fun toLanguage(value: String): LanguageOption = runCatching {
        LanguageOption.valueOf(value)
    }.getOrDefault(LanguageOption.ENGLISH)

    @TypeConverter
    fun fromCurrency(value: CurrencyOption): String = value.name

    @TypeConverter
    fun toCurrency(value: String): CurrencyOption = runCatching {
        CurrencyOption.valueOf(value)
    }.getOrDefault(CurrencyOption.EUR)

    @TypeConverter
    fun fromDateFormat(value: DateFormatOption): String = value.name

    @TypeConverter
    fun toDateFormat(value: String): DateFormatOption = runCatching {
        DateFormatOption.valueOf(value)
    }.getOrDefault(DateFormatOption.DD_MM_YYYY)

    @TypeConverter
    fun fromTheme(value: ThemeOption): String = value.name

    @TypeConverter
    fun toTheme(value: String): ThemeOption = runCatching {
        ThemeOption.valueOf(value)
    }.getOrDefault(ThemeOption.LIGHT)

    @TypeConverter
    fun fromTextSize(value: TextSizeOption): String = value.name

    @TypeConverter
    fun toTextSize(value: String): TextSizeOption = runCatching {
        TextSizeOption.valueOf(value)
    }.getOrDefault(TextSizeOption.NORMAL)
}
