package utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import utils.CategoryUtility.categories
import utils.CategoryUtility.isValidCategory

internal class CategoryUtilityTest {
    @Test
    fun categoriesReturnsFullCategoriesSet(){
        Assertions.assertEquals(2, categories.size)
        Assertions.assertTrue(categories.contains("Home"))
        Assertions.assertTrue(categories.contains("College"))
        Assertions.assertFalse(categories.contains(""))
    }

    @Test
    fun isValidCategoryTrueWhenCategoryExists(){
        Assertions.assertTrue(isValidCategory("Home"))
        Assertions.assertTrue(isValidCategory("home"))
        Assertions.assertTrue(isValidCategory("COLLEGE"))
    }

    @Test
    fun isValidCategoryFalseWhenCategoryDoesNotExist(){
        Assertions.assertFalse(isValidCategory("Hom"))
        Assertions.assertFalse(isValidCategory("college"))
        Assertions.assertFalse(isValidCategory(""))
    }
}