package com.pokeskies.skiesannouncements.config

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import com.pokeskies.skiesannouncements.SkiesAnnouncements
import com.pokeskies.skiesannouncements.config.requirements.ComparisonType
import com.pokeskies.skiesannouncements.config.requirements.Requirement
import com.pokeskies.skiesannouncements.config.requirements.RequirementType
import com.pokeskies.skiesannouncements.utils.Utils
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvent
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class ConfigManager(private val configDir: File) {
    lateinit var config: MainConfig
    var gson: Gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
        .registerTypeAdapter(ComparisonType::class.java, ComparisonType.ComparisonTypeAdaptor())
        .registerTypeAdapter(Requirement::class.java, RequirementType.RequirementTypeAdaptor())
        .registerTypeAdapter(AnnouncementOrder::class.java, AnnouncementOrder.Adaptor())
        .registerTypeHierarchyAdapter(SoundEvent::class.java, Utils.RegistrySerializer(BuiltInRegistries.SOUND_EVENT))
        .create()

    companion object {
        var GROUPS: BiMap<String, AnnouncementGroup> = HashBiMap.create()
    }

    init {
        reload()
    }

    fun reload() {
        copyDefaults()
        config = loadFile("config.json", MainConfig::class.java)!!
        loadGroups()
    }

    fun copyDefaults() {
        val classLoader = SkiesAnnouncements::class.java.classLoader

        configDir.mkdirs()

        // Main Config
        val configFile = configDir.resolve("config.json")
        if (!configFile.exists()) {
            try {
                val inputStream: InputStream = classLoader.getResourceAsStream("assets/skiesannouncements/config.json")
                Files.copy(inputStream, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            } catch (e: Exception) {
                Utils.printError("Failed to copy the default config file: $e - ${e.message}")
            }
        }

        // If the 'groups' directory does not exist, create it and copy the default example group message
        val groupDir = configDir.resolve("groups")
        if (!groupDir.exists()) {
            groupDir.mkdirs()
            val file = groupDir.resolve("example_group.json")
            try {
                val resourceFile: Path =
                    Path.of(classLoader.getResource("assets/skiesannouncements/groups/example_group.json").toURI())
                Files.copy(resourceFile, file.toPath(), StandardCopyOption.REPLACE_EXISTING)
            } catch (e: Exception) {
                Utils.printError("Failed to copy the default group file: " + e.message)
            }
        }
    }

    private fun loadGroups() {
        GROUPS.clear()

        val dir = configDir.resolve("groups")
        if (dir.exists() && dir.isDirectory) {
            val files = dir.listFiles()
            if (files != null) {
                for (file in files) {
                    val fileName = file.name
                    if (file.isFile && fileName.contains(".json")) {
                        val id = fileName.substring(0, fileName.lastIndexOf(".json"))
                        val jsonReader = JsonReader(InputStreamReader(FileInputStream(file), Charsets.UTF_8))
                        try {
                            GROUPS[id] = gson.fromJson(JsonParser.parseReader(jsonReader), AnnouncementGroup::class.java)
                            Utils.printInfo("Successfully read and loaded the file $fileName!")
                        } catch (ex: Exception) {
                            Utils.printError("Error while trying to parse the file $fileName as an Announcement Group!")
                            ex.printStackTrace()
                        }
                    } else {
                        Utils.printError("File $fileName is either not a file or is not a .json file!")
                    }
                }
            }
        } else {
            Utils.printError("The groups directory either does not exist or is not a directory!")
        }
    }

    fun <T : Any> loadFile(filename: String, classObject: Class<T>): T? {
        val file = File(configDir, filename)
        var value: T? = null
        try {
            Files.createDirectories(configDir.toPath())
            try {
                FileReader(file).use { reader ->
                    val jsonReader = JsonReader(reader)
                    value = gson.fromJson(jsonReader, classObject)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return value
    }

    fun <T> saveFile(filename: String, `object`: T) {
        val file = File(configDir, filename)
        try {
            FileWriter(file).use { fileWriter ->
                fileWriter.write(gson.toJson(`object`))
                fileWriter.flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
