package utils

object CategoryUtility {

    //NOTE: JvmStatic annotation means that the categories variable is static (i.e. we can reference it through the class
    //      name; we don't have to create an object of CategoryUtility to use it.
    @JvmStatic
    val categories = setOf ("Home", "College")  //add more categories in here.

    @JvmStatic
    fun isValidCategory(categoryToCheck: String?): Boolean {
        for (category in categories) {
            if (category.equals(categoryToCheck, ignoreCase = true)) {
                return true
            }
        }
        return false
    }


}