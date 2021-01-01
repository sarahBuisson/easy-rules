/*
 * The MIT License
 *
 *  Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.jeasy.rules.support.reader

import org.assertj.core.api.Assertions
import org.jeasy.rules.api.Rule
import kotlin.test.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.io.File
import java.io.FileReader
import java.io.StringReader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@RunWith(Parameterized::class)
class RuleDefinitionReaderTest {
    @Parameterized.Parameter(0)
    lateinit var ruleDefinitionReader: RuleDefinitionReader

    @Parameterized.Parameter(1)
    var fileExtension: String? = null
    @Test
    @Throws(Exception::class)
    fun testRuleDefinitionReadingFromFile() {
        // given
        val adultRuleDescriptor = File("src/test/resources/adult-rule.$fileExtension")

        // when
        val ruleDefinitions = ruleDefinitionReader.read(FileReader(adultRuleDescriptor))

        // then
        assertTrue(ruleDefinitions.asMap().size, 1)
        val adultRuleDefinition = ruleDefinitions[0]
        assertTrue(adultRuleDefinition).isNotNull
        assertTrue(adultRuleDefinition.name,"adult rule")
        assertTrue(adultRuleDefinition.description)
            .isEqualTo("when age is greater than 18, then mark as adult")
        assertTrue(adultRuleDefinition.priority,1)
        assertTrue(adultRuleDefinition.condition,"person.age > 18")
        assertTrue(adultRuleDefinition.actions,listOf<String?>("person.setAdult(true);"))
    }

    @Test
    @Throws(Exception::class)
    fun testRuleDefinitionReadingFromString() {
        // given
        val ruleDescriptor = Paths.get("src/test/resources/adult-rule.$fileExtension")
        val adultRuleDescriptor = String(Files.readAllBytes(ruleDescriptor))

        // when
        val ruleDefinitions = ruleDefinitionReader.read(StringReader(adultRuleDescriptor))

        // then
        assertTrue(ruleDefinitions.asMap().size, 1)
        val adultRuleDefinition = ruleDefinitions[0]
        assertTrue(adultRuleDefinition).isNotNull
        assertTrue(adultRuleDefinition.name,"adult rule")
        assertTrue(adultRuleDefinition.description)
            .isEqualTo("when age is greater than 18, then mark as adult")
        assertTrue(adultRuleDefinition.priority,1)
        assertTrue(adultRuleDefinition.condition,"person.age > 18")
        assertTrue(adultRuleDefinition.actions,listOf<String?>("person.setAdult(true);"))
    }

    @Test
    @Throws(Exception::class)
    fun testRuleDefinitionReading_withDefaultValues() {
        // given
        val adultRuleDescriptor = File("src/test/resources/adult-rule-with-default-values.$fileExtension")

        // when
        val ruleDefinitions = ruleDefinitionReader.read(FileReader(adultRuleDescriptor))

        // then
        assertTrue(ruleDefinitions.asMap().size, 1)
        val adultRuleDefinition = ruleDefinitions[0]
        assertTrue(adultRuleDefinition).isNotNull
        assertTrue(adultRuleDefinition.name,Rule.DEFAULT_NAME)
        assertTrue(adultRuleDefinition.description,Rule.DEFAULT_DESCRIPTION)
        assertTrue(adultRuleDefinition.priority,Rule.DEFAULT_PRIORITY)
        assertTrue(adultRuleDefinition.condition,"person.age > 18")
        assertTrue(adultRuleDefinition.actions,listOf<String?>("person.setAdult(true);"))
    }

    @Test(expected = IllegalArgumentException::class)
    @Throws(Exception::class)
    fun testInvalidRuleDefinitionReading_whenNoCondition() {
        // given
        val adultRuleDescriptor = File("src/test/resources/adult-rule-without-condition.$fileExtension")

        // when
        val ruleDefinitions = ruleDefinitionReader.read(FileReader(adultRuleDescriptor))

        // then
        // expected exception
    }

    @Test(expected = IllegalArgumentException::class)
    @Throws(Exception::class)
    fun testInvalidRuleDefinitionReading_whenNoActions() {
        // given
        val adultRuleDescriptor = File("src/test/resources/adult-rule-without-actions.$fileExtension")

        // when
        val ruleDefinitions = ruleDefinitionReader.read(FileReader(adultRuleDescriptor))

        // then
        // expected exception
    }

    @Test
    @Throws(Exception::class)
    fun testRulesDefinitionReading() {
        // given
        val rulesDescriptor = File("src/test/resources/rules.$fileExtension")

        // when
        val ruleDefinitions = ruleDefinitionReader.read(FileReader(rulesDescriptor))

        // then
        assertTrue(ruleDefinitions.asMap().size, 2)
        var ruleDefinition = ruleDefinitions[0]
        assertTrue(ruleDefinition).isNotNull
        assertTrue(ruleDefinition.name,"adult rule")
        assertTrue(ruleDefinition.description,"when age is greater than 18, then mark as adult")
        assertTrue(ruleDefinition.priority,1)
        assertTrue(ruleDefinition.condition,"person.age > 18")
        assertTrue(ruleDefinition.actions,listOf<String?>("person.setAdult(true);"))
        ruleDefinition = ruleDefinitions[1]
        assertTrue(ruleDefinition).isNotNull
        assertTrue(ruleDefinition.name,"weather rule")
        assertTrue(ruleDefinition.description,"when it rains, then take an umbrella")
        assertTrue(ruleDefinition.priority,2)
        assertTrue(ruleDefinition.condition,"rain == true")
        assertTrue(ruleDefinition.actions)
            .isEqualTo(listOf<String?>("System.out.println(\"It rains, take an umbrella!\");"))
    }

    @Test
    @Throws(Exception::class)
    fun testEmptyRulesDefinitionReading() {
        // given
        val rulesDescriptor = File("src/test/resources/rules-empty.$fileExtension")

        // when
        val ruleDefinitions = ruleDefinitionReader.read(FileReader(rulesDescriptor))

        // then
        assertTrue(ruleDefinitions.asMap().size, 0)
    }

    @Test
    @Throws(Exception::class)
    fun testRuleDefinitionReading_withCompositeAndBasicRules() {
        // given
        val compositeRuleDescriptor = File("src/test/resources/composite-rules.$fileExtension")

        // when
        val ruleDefinitions = ruleDefinitionReader.read(FileReader(compositeRuleDescriptor))

        // then
        assertTrue(ruleDefinitions.asMap().size, 2)

        // then
        var ruleDefinition = ruleDefinitions[0]
        assertTrue(ruleDefinition).isNotNull
        assertTrue(ruleDefinition.name,"Movie id rule")
        assertTrue(ruleDefinition.description,"description")
        assertTrue(ruleDefinition.priority,1)
        assertTrue(ruleDefinition.compositeRuleType,"UnitRuleGroup")
        assertTrue(ruleDefinition.composingRules).isNotEmpty
        val subrules = ruleDefinition.composingRules
        assertTrue(subrules.asMap().size, 2)
        var subrule = subrules[0]
        assertTrue(subrule.name,"Time is evening")
        assertTrue(subrule.description,"If it's later than 7pm")
        assertTrue(subrule.priority,1)
        subrule = subrules[1]
        assertTrue(subrule.name,"Movie is rated R")
        assertTrue(subrule.description,"If the movie is rated R")
        assertTrue(subrule.priority,1)
        ruleDefinition = ruleDefinitions[1]
        assertTrue(ruleDefinition).isNotNull
        assertTrue(ruleDefinition.name,"weather rule")
        assertTrue(ruleDefinition.description,"when it rains, then take an umbrella")
        assertTrue(ruleDefinition.composingRules).isEmpty()
        assertTrue(ruleDefinition.condition,"rain == True")
        assertTrue(ruleDefinition.actions)
            .isEqualTo(listOf<String?>("System.out.println(\"It rains, take an umbrella!\");"))
    }

    companion object {
        @Parameterized.Parameters
        fun parameters(): MutableCollection<Array<Any?>?>? {
            return Arrays.asList(
                *arrayOf<Array<Any?>?>(
                    arrayOf(YamlRuleDefinitionReader(), "yml"),
                    arrayOf(JsonRuleDefinitionReader(), "json")
                )
            )
        }
    }
}
