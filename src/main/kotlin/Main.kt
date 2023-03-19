import controllers.NoteAPI
import models.Note
import mu.KotlinLogging
import org.fusesource.jansi.Ansi.ansi
import persistence.JSONSerializer
import persistence.XMLSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}

/**Uncomment and Comment to alternate between the two*/
private val noteAPI = NoteAPI(XMLSerializer(File("notes.xml")))
//private val noteAPI = NoteAPI(JSONSerializer(File("notes.json")))

fun main() {
    runMenu()
}

fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> addNote()
            2 -> listNotes()
            3 -> updateNote()
            4 -> deleteNote()
            5 -> archiveNote()
            6 -> searchForNote()
            7 -> numNotesByPriority()
            20 -> save()
            21 -> load()
            0 -> exitApp()
            else -> println((ansi().render("@|red \n Invalid option entered: $option \n |@")))
        }
    } while (true)
}

fun mainMenu(): Int {
    return readNextInt(
        ansi().render(
            """ 
                 > @|cyan ----------------------------------|@
                 > @|white,bold,underline          NOTE KEEPER APP        |@
                 > @|cyan |--------------------------------||@
                 > @|white |   NOTE MENU                      |@
                 > @|green |   1.) Add a note                |@
                 > @|green |   2.) List all notes            |@
                 > @|yellow |   3.) Update a note             |@
                 > @|yellow |   4.) Delete a note             |@
                 > @|green |   5.) Archive a note            |@
                 > @|blue |   6.) Search for a note         |@
                 > @|blue |   7.) Sort By Most Important         |@
                 > @|cyan ----------------------------------|@
                 > @|magenta |   20.) Save notes               |@
                 > @|magenta |   21.) Load notes               |@
                 > @|red |   0.) Exit                      |@
                 > @|cyan ----------------------------------|@
                 > ==>> """.trimMargin(">")).toString()
    )
}


fun addNote(){
    //logger.info { "addNote() function invoked" }
    val noteTitle = readNextLine("Enter a title for the note: ")
    val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val noteCategory = readNextLine("Enter a category for the note: ")
    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, false))

    if (isAdded) {
        println((ansi().render("@|green \n Added Successfully \n |@")))
    } else {
        println((ansi().render("@|red \n Add Failed \n |@")))
    }
}

fun listNotes() {
    if (noteAPI.numberOfNotes() > 0) {
        val option = readNextInt(
            ansi().render(
                """
                          > ----------------------------------------
                          > |   @|green 1) View ALL notes      |@
                          > |   @|green 2) View ACTIVE notes   |@
                          > |   @|green 3) View ARCHIVED notes |@
                          > ----------------------------------------
                          >     @|magenta 0) Back |@
                          > ----------------------------------------
                 > @|blue ==>> |@ """.trimMargin(">")).toString()
        )

        when (option) {
            1 -> {
                listAllNotes()
                return // exit the function after calling listAllNotes()
            }
            2 -> {
                listActiveNotes()
                return // exit the function after calling listActiveNotes()
            }
            3 -> {
                listArchivedNotes()
                return // exit the function after calling listArchivedNotes()
            }
            0 -> return // exit the function if option 0 (back) is selected
            else -> println((ansi().render("@|red \n Invalid option entered: $option \n |@")))
        }
    } else {
        println((ansi().render("@|red \n Option Invalid - No notes stored \n |@")))
    }
}

fun updateNote() {
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note if notes exist
        val indexToUpdate = readNextInt(ansi().render("@|green Enter the index of the note to update:|@ ").toString())
        if (noteAPI.isValidIndex(indexToUpdate)) {
            val noteTitle = readNextLine(ansi().render("@|green Enter a title for the note:|@ ").toString())
            val notePriority = readNextInt(ansi().render("@|green Enter a priority (1-low, 2, 3, 4, 5-high):|@ ").toString())
            val noteCategory = readNextLine(ansi().render("@|green Enter a category for the note:|@ ").toString())

            //pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, notePriority, noteCategory, false))){
                println(ansi().render("@|green Update Successful|@"))
            } else {
                println(ansi().render("@|red Update Failed|@"))
            }
        } else {
            println(ansi().render("@|red There are no notes for this index number|@"))
        }
    }
}

fun deleteNote(){
    //logger.info { "deleteNotes() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note to delete if notes exist
        val indexToDelete = readNextInt("Enter the index of the note to delete: ")
        //pass the index of the note to NoteAPI for deleting and check for success.
        val noteToDelete = noteAPI.deleteNote(indexToDelete)
        if (noteToDelete != null) {
            println("Delete Successful! Deleted note: ${noteToDelete.noteTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun listActiveNotes() {
    println(noteAPI.listActiveNotes())
}

fun archiveNote() {
    listActiveNotes()
    if (noteAPI.numberOfActiveNotes() > 0) {
        //only ask the user to choose the note to archive if active notes exist
        val indexToArchive = readNextInt("Enter the index of the note to archive: ")
        //pass the index of the note to NoteAPI for archiving and check for success.
        if (noteAPI.archiveNote(indexToArchive)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}

fun listAllNotes() {
    println(noteAPI.listAllNotes())
}

fun listArchivedNotes() {
    println(noteAPI.listArchivedNotes())
}

fun searchForNote() {
    val searchTitle = readNextLine("Please enter the details you want to look for: ")
    val searchResults = noteAPI.searchByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("Sorry, no notes with those details were found!")
    } else {
        println(searchResults)
    }
}

fun numNotesByPriority() {
    println(noteAPI.listNotesBySelectedPriority(5)) // Max priority is = 5, so it is the most important
}

fun save() {
    try {
        noteAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        noteAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

fun exitApp(){
    logger.info { "exitApp() function invoked" }
    exitProcess(0)
}