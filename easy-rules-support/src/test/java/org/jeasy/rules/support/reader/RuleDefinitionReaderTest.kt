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
import org.junit.Test
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
        Assertions.assertThat(ruleDefinitions).hasSize(1)
        val adultRuleDefinition = ruleDefinitions[0]
        Assertions.assertThat(adultRuleDefinition).isNotNull
        Assertions.assertThat(adultRuleDefinition.name).isEqualTo("adult rule")
        Assertions.assertThat(adultRuleDefinition.description)
            .isEqualTo("when age is greater than 18, then mark as adult")
        Assertions.assertThat(adultRuleDefinition.priority).isEqualTo(1)
        Assertions.assertThat(adultRuleDefinition.condition).isEqualTo("person.age > 18")
        Assertions.assertThat(adultRuleDefinition.actions).isEqualTo(listOf<String?>("person.setAdult(true);"))
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
        Assertions.assertThat(ruleDefinitions).hasSize(1)
        val adultRuleDefinition = ruleDefinitions[0]
        Assertions.assertThat(adultRuleDefinition).isNotNull
        Assertions.assertThat(adultRuleDefinition.name).isEqualTo("adult rule")
        Assertions.assertThat(adultRuleDefinition.description)
            .isEqualTo("when age is greater than 18, then mark as adult")
        Assertions.assertThat(adultRuleDefinition.priority).isEqualTo(1)
        Assertions.assertThat(adultRuleDefinition.condition).isEqualTo("person.age > 18")
        Assertions.assertThat(adultRuleDefinition.actions).isEqualTo(listOf<String?>("person.setAdult(true);"))
    }

    @Test
    @Throws(Exception::class)
    fun testRuleDefinitionReading_withDefaultValues() {
        // given
        val adultRuleDescriptor = File("src/test/resources/adult-rule-with-default-values.$fileExtension")

        // when
        val ruleDefinitions = ruleDefinitionReader.read(FileReader(adultRuleDescriptor))

        // then
        Assertions.assertThat(ruleDefinitions).hasSize(1)
        val adultRuleDefinition = ruleDefinitions[0]
        Assertions.assertThat(adultRuleDefinition).isNotNull
        Assertions.assertThat(adultRuleDefinition.name).isEqualTo(Rule.DEFAULT_NAME)
        Assertions.assertThat(adultRuleDefinition.description).isEqualTo(Rule.DEFAULT_DESCRIPTION)
        Assertions.assertThat(adultRuleDefinition.priority).isEqualTo(Rule.DEFAULT_PRIORITY)
        Assertions.assertThat(adultRuleDefinition.condition).isEqualTo("person.age > 18")
        Assertions.assertThat(adultRuleDefinition.actions).isEqualTo(listOf<String?>("person.setAdult(true);"))
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
        Assertions.assertThat(ruleDefinitions).hasSize(2)
        var ruleDefinition = ruleDefinitions[0]
        Assertions.assertThat(ruleDefinition).isNotNull
        Assertions.assertThat(ruleDefinition.name).isEqualTo("adult rule")
        Assertions.assertThat(ruleDefinition.description).isEqualTo("when age is greater than 18, then mark as adult")
        Assertions.assertThat(ruleDefinition.priority).isEqualTo(1)
        Assertions.assertThat(ruleDefinition.condition).isEqualTo("person.age > 18")
        Assertions.assertThat(ruleDefinition.actions).isEqualTo(listOf<String?>("person.setAdult(true);"))
        ruleDefinition = ruleDefinitions[1]
        Assertions.assertThat(ruleDefinition).isNotNull
        Assertions.assertThat(ruleDefinition.name).isEqualTo("weather rule")
        Assertions.assertThat(ruleDefinition.description).isEqualTo("when it rains, then take an umbrella")
        Assertions.assertThat(ruleDefinition.priority).isEqualTo(2)
        Assertions.assertThat(ruleDefinition.condition).isEqualTo("rain == true")
        Assertions.assertThat(ruleDefinition.actions)
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
        Assertions.assertThat(ruleDefinitions).hasSize(0)
    }

    @Test
    @Throws(Exception::class)
    fun testRuleDefinitionReading_withCompositeAndBasicRules() {
        // given
        val compositeRuleDescriptor = File("src/test/resources/composite-rules.$fileExtension")

        // when
        val ruleDefinitions = ruleDefinitionReader.read(FileReader(compositeRuleDescriptor))

        // then
        Assertions.assertThat(ruleDefinitions).hasSize(2)

        // then
        var ruleDefinition = ruleDefinitions[0]
        Assertions.assertThat(ruleDefinition).isNotNull
        Assertions.assertThat(ruleDefinition.name).isEqualTo("Movie id rule")
        Assertions.assertThat(ruleDefinition.description).isEqualTo("description")
        Assertions.assertThat(ruleDefinition.priority).isEqualTo(1)
        Assertions.assertThat(ruleDefinition.compositeRuleType).isEqualTo("UnitRuleGroup")
        Assertions.assertThat(ruleDefinition.composingRules).isNotEmpty
        val subrules = ruleDefinition.composingRules
        Assertions.assertThat(subrules).hasSize(2)
        var subrule = subrules[0]
        Assertions.assertThat(subrule.name).isEqualTo("Time is evening")
        Assertions.assertThat(subrule.description).isEqualTo("If it's later than 7pm")
        Assertions.assertThat(subrule.priority).isEqualTo(1)
        subrule = subrules[1]
        Assertions.assertThat(subrule.name).isEqualTo("Movie is rated R")
        Assertions.assertThat(subrule.description).isEqualTo("If the movie is rated R")
        Assertions.assertThat(subrule.priority).isEqualTo(1)
        ruleDefinition = ruleDefinitions[1]
        Assertions.assertThat(ruleDefinition).isNotNull
        Assertions.assertThat(ruleDefinition.name).isEqualTo("weather rule")
        Assertions.assertThat(ruleDefinition.description).isEqualTo("when it rains, then take an umbrella")
        Assertions.assertThat(ruleDefinition.composingRules).isEmpty()
        Assertions.assertThat(ruleDefinition.condition).isEqualTo("rain == True")
        Assertions.assertThat(ruleDefinition.actions)
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
